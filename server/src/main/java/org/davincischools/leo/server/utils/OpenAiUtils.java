package org.davincischools.leo.server.utils;

import com.fasterxml.jackson.datatype.jdk8.WrappedIOException;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class OpenAiUtils {

  private static final Logger logger = LogManager.getLogger();

  public static final String OPENAI_API_KEY_PROP_NAME = "openai.api.key";
  public static final String OPENAI_URL_PROP_NAME = "openai.url";
  public static final String OPENAI_API_KEY_ENV_NAME =
      OPENAI_API_KEY_PROP_NAME.toUpperCase().replaceAll("\\.", "_");
  public static final String GPT_3_5_TURBO_MODEL = "gpt-3.5-turbo";
  public static final String GPT_4_MODEL = "gpt-4";
  public static final int TIMEOUT_MIN = 15;

  private final String openAiKey;
  private final String openAiUrl;

  private OpenAiUtils(
      @Value("${" + OPENAI_API_KEY_PROP_NAME + ":}") String openAiKey,
      @Value("${" + OPENAI_URL_PROP_NAME + ":}") String openAiUrl) {
    this.openAiKey = openAiKey;
    this.openAiUrl = openAiUrl;
  }

  public Optional<String> getOpenAiKey() {
    return openAiKey.isEmpty() ? Optional.empty() : Optional.of(openAiKey);
  }

  // Makes a call to OpenAI. If no key is available, returns an unmodified response.
  public <T extends Builder<?>> T sendOpenAiRequest(
      Message request, T responseBuilder, HttpExecutors httpExecutors) throws IOException {
    return httpExecutors
        .start(request)
        .andThen(
            (unused, log) -> {
              // The OPENAI_API_KEY is required.
              if (openAiKey.isEmpty() || openAiKey.equals("<your_open_ai_api_key>")) {
                logger
                    .atError()
                    .log(
                        "OpenAI not called due to missing '"
                            + OPENAI_API_KEY_PROP_NAME
                            + "' property and missing '"
                            + OPENAI_API_KEY_ENV_NAME
                            + "' environment variable. Request: {}",
                        TextFormat.printer().printToString(request));
                throw new IOException(
                    "OpenAI key missing. See:"
                        + " https://github.com/DaVinciSchools/leo/blob/main/BUILDING.md#external-dependencies");
              }

              return null;
            })
        .retryNextStep(3, (int) Duration.ofSeconds(20).toMillis())
        .andThen(
            (unused, log) -> {
              // Make the call to OpenAI.
              HttpClient client =
                  HttpClient.create()
                      .responseTimeout(Duration.ofMinutes(TIMEOUT_MIN))
                      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_MIN * 60 * 1000)
                      .option(ChannelOption.SO_KEEPALIVE, true)
                      // .option(ChannelOption.SO_TIMEOUT, 0)
                      // .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPIDLE), 3 * 60)
                      // .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPINTERVAL), 60)
                      // .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPCOUNT), 65535)
                      // .option(EpollChannelOption.TCP_KEEPIDLE, 3 * 60)
                      // .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                      // .option(EpollChannelOption.TCP_KEEPCNT, 65535)
                      // .wiretap(true)
                      // .wiretap(
                      // "reactor.netty.http.client.HttpClient",
                      // LogLevel.DEBUG,
                      // AdvancedByteBufFormat.HEX_DUMP)
                      .doOnConnected(
                          conn ->
                              conn.addHandlerFirst(
                                      new ReadTimeoutHandler(TIMEOUT_MIN, TimeUnit.MINUTES))
                                  .addHandlerFirst(
                                      new WriteTimeoutHandler(TIMEOUT_MIN, TimeUnit.MINUTES)));

              // Stream the response body because the buffer is limited.
              ImmutableList<byte[]> streamedBytes =
                  WebClient.builder()
                      .clientConnector(new ReactorClientHttpConnector(client))
                      .build()
                      .post()
                      .uri(URI.create(openAiUrl + "/v1/chat/completions"))
                      .contentType(MediaType.APPLICATION_JSON)
                      .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
                      .header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,max-age=0")
                      .header(HttpHeaders.PRAGMA, "No-Cache")
                      .header(HttpHeaders.EXPIRES, "0") // I.e., now.
                      .bodyValue(JsonFormat.printer().print(request))
                      .retrieve()
                      .onStatus(
                          httpStatusCode -> true,
                          response -> {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            pw.println("OpenAI returned status: " + response.statusCode());
                            response
                                .headers()
                                .asHttpHeaders()
                                .forEach(
                                    (key, values) -> {
                                      pw.println(" - Header: " + key + ": " + values);
                                    });
                            pw.println("Done logging OpenAI status.");
                            pw.flush();
                            log.addNote(sw.toString());
                            if (response.statusCode().isError()) {
                              return Mono.error(
                                  new HttpClientErrorException(
                                      response.statusCode(),
                                      "OpenAI returned status: " + response.statusCode()));
                            } else {
                              return Mono.empty();
                            }
                          })
                      .bodyToFlux(DataBuffer.class)
                      .map(
                          buffer -> {
                            try (InputStream in = buffer.asInputStream(true)) {
                              return in.readAllBytes();
                            } catch (IOException e) {
                              throw new WrappedIOException(e);
                            }
                          })
                      .collect(ImmutableList.toImmutableList())
                      .block();

              return Bytes.concat(Objects.requireNonNull(streamedBytes).toArray(byte[][]::new));
            })
        .logInitialResponse()
        .andThen(
            (bytes, log) -> {
              JsonFormat.parser()
                  .ignoringUnknownFields()
                  .merge(new String(bytes, StandardCharsets.UTF_8), responseBuilder);
              return responseBuilder;
            })
        .finish();
  }
}
