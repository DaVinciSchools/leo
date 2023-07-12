package org.davincischools.leo.server.utils;

import static java.util.stream.Collectors.joining;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.davincischools.leo.protos.jira.CreateIssueRequestOrBuilder;
import org.davincischools.leo.protos.jira.CreateIssueResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class JiraUtils {

  public static final String ATLASSIAN_USERNAME_EMAIL_PROP_NAME = "atlassian.username.email";
  public static final String ATLASSIAN_API_TOKEN_PROP_NAME = "atlassian.api.token";

  public static final String ATLASSIAN_REPORTER_EMAIL_PROP_NAME = "atlassian.reporter.email";
  public static final String ATLASSIAN_ASSIGNEE_EMAIL_PROP_NAME = "atlassian.assignee.email";

  public static final String JIRA_API_URL = "https://projectleo.atlassian.net";

  public static final String ERROR_FIELD = "project_leo_error";

  private final String authorizationHeaderValue;

  private interface WrapExceptionRunner {
    void run() throws Exception;
  }

  public JiraUtils(
      @Value("${" + ATLASSIAN_USERNAME_EMAIL_PROP_NAME + ":}") String atlassianUsername,
      @Value("${" + ATLASSIAN_API_TOKEN_PROP_NAME + ":}") String atlassianApiToken) {

    authorizationHeaderValue =
        "Basic "
            + Base64.getEncoder()
                .encodeToString(
                    (atlassianUsername + ":" + atlassianApiToken).getBytes(StandardCharsets.UTF_8));
  }

  private HttpClient createBasicClient() {
    return HttpClient.create()
        .responseTimeout(Duration.ofSeconds(120))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120 * 1000)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .doOnConnected(
            conn ->
                conn.addHandlerFirst(new ReadTimeoutHandler(600, TimeUnit.SECONDS))
                    .addHandlerFirst(new WriteTimeoutHandler(600, TimeUnit.SECONDS)));
  }

  @SuppressWarnings("unchecked")
  public <T extends Message> T sendGetRequest(
      String apiMethod, MessageOrBuilder params, Class<T> responseType) {
    try {
      Builder responseBuilder = (Builder) responseType.getMethod("newBuilder").invoke(null);
      FieldDescriptor errorField =
          responseBuilder.getDescriptorForType().findFieldByName(ERROR_FIELD);
      if (errorField == null) {
        throw new IllegalArgumentException(
            "Proto "
                + responseBuilder.getDescriptorForType().getName()
                + " doesn't have a(n) "
                + ERROR_FIELD
                + " field.");
      }

      String encodedURL =
          JIRA_API_URL
              + apiMethod
              + params.getDescriptorForType().getFields().stream()
                  .map(
                      field ->
                          params.hasField(field)
                              ? field.getName()
                                  + "="
                                  + URLEncoder.encode(
                                      Objects.toString(params.getField(field)),
                                      StandardCharsets.UTF_8)
                              : null)
                  .filter(Objects::nonNull)
                  .collect(joining("&", "?", ""));

      WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(createBasicClient()))
          .build()
          .get()
          .uri(URI.create(encodedURL))
          .accept(MediaType.APPLICATION_JSON)
          .header(HttpHeaders.AUTHORIZATION, authorizationHeaderValue)
          .header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,max-age=0")
          .header(HttpHeaders.PRAGMA, "No-Cache")
          .header(HttpHeaders.EXPIRES, "0") // I.e., now.
          .exchangeToMono(
              clientResponse -> parseClientResponse(clientResponse, responseBuilder, errorField))
          .block();

      return (T) responseBuilder.build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends Message> T sendPostRequest(
      String apiMethod, MessageOrBuilder payload, Class<T> responseType) {
    try {
      Builder responseBuilder = (Builder) responseType.getMethod("newBuilder").invoke(null);
      FieldDescriptor errorField =
          responseBuilder.getDescriptorForType().findFieldByName(ERROR_FIELD);
      if (errorField == null) {
        throw new IllegalArgumentException(
            "Proto "
                + responseBuilder.getDescriptorForType().getName()
                + " doesn't have a(n) "
                + ERROR_FIELD
                + " field.");
      }

      WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(createBasicClient()))
          .build()
          .post()
          .uri(URI.create(JIRA_API_URL + apiMethod))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .header(HttpHeaders.AUTHORIZATION, authorizationHeaderValue)
          .header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,max-age=0")
          .header(HttpHeaders.PRAGMA, "No-Cache")
          .header(HttpHeaders.EXPIRES, "0") // I.e., now.
          .bodyValue(JsonFormat.printer().print(payload))
          .exchangeToMono(
              clientResponse -> parseClientResponse(clientResponse, responseBuilder, errorField))
          .block();

      return (T) responseBuilder.build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Mono<byte[]> parseClientResponse(
      ClientResponse clientResponse, Builder responseBuilder, FieldDescriptor errorField) {
    if (clientResponse.statusCode().isError()) {
      return clientResponse
          .bodyToMono(byte[].class)
          .doOnNext(
              bytes ->
                  wrapException(
                      () ->
                          JsonFormat.parser()
                              .ignoringUnknownFields()
                              .merge(
                                  new String(bytes, StandardCharsets.UTF_8),
                                  responseBuilder.getFieldBuilder(errorField))));
    } else {
      return clientResponse
          .bodyToMono(byte[].class)
          .doOnNext(
              bytes ->
                  wrapException(
                      () ->
                          JsonFormat.parser()
                              .ignoringUnknownFields()
                              .merge(new String(bytes, StandardCharsets.UTF_8), responseBuilder)));
    }
  }

  private void wrapException(WrapExceptionRunner runner) {
    try {
      runner.run();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public CreateIssueResponse createIssue(CreateIssueRequestOrBuilder request) {
    return sendPostRequest("/rest/api/2/issue", request, CreateIssueResponse.class);
  }
}
