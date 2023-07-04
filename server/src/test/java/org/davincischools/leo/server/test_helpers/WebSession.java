package org.davincischools.leo.server.test_helpers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Iterables;
import com.google.common.net.HttpHeaders;
import com.google.protobuf.Message;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.davincischools.leo.protos.pl_types.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebSession {

  public static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";
  public static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
  public static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
  public static final String DEFAULT_JSESSIONID_COOKIE_NAME = "JSESSIONID";
  public static final String PROTOBUF_MIME_TYPE = "application/x-protobuf";

  public static String bodyAsString(ResponseSpec spec) {
    byte[] body = spec.bodyToMono(byte[].class).block();
    assertThat(body).isNotNull();
    return new String(body, StandardCharsets.UTF_8);
  }

  public static <T extends Message> T bodyAsProto(ResponseSpec spec, Class<T> protoClass) {
    byte[] body = spec.bodyToMono(byte[].class).block();
    assertThat(body).isNotNull();
    try {
      return (T) protoClass.getMethod("parseFrom", byte[].class).invoke(null, body);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String bodyAsString(WebTestClient.ResponseSpec spec) {
    byte[] body = spec.expectBody().returnResult().getResponseBodyContent();
    assertThat(body).isNotNull();
    return new String(body, StandardCharsets.UTF_8);
  }

  public static <T extends Message> T bodyAsProto(
      WebTestClient.ResponseSpec spec, Class<T> protoClass) {
    byte[] body = spec.expectBody().returnResult().getResponseBodyContent();
    assertThat(body).isNotNull();
    try {
      return (T) protoClass.getMethod("parseFrom", byte[].class).invoke(null, body);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private class WebClientProxy implements WebClient {

    final WebClient client;

    public WebClientProxy(WebClient client) {
      this.client = client;
    }

    public RequestHeadersUriSpec<?> get() {
      return wrap(client.get(), RequestHeadersUriSpec.class);
    }

    public RequestHeadersUriSpec<?> head() {
      return wrap(client.head(), RequestHeadersUriSpec.class);
    }

    public RequestBodyUriSpec post() {
      return wrap(client.post(), RequestBodyUriSpec.class);
    }

    public RequestBodyUriSpec put() {
      return wrap(client.put(), RequestBodyUriSpec.class);
    }

    public RequestBodyUriSpec patch() {
      return wrap(client.patch(), RequestBodyUriSpec.class);
    }

    public RequestHeadersUriSpec<?> delete() {
      return wrap(client.delete(), RequestHeadersUriSpec.class);
    }

    public RequestHeadersUriSpec<?> options() {
      return wrap(client.options(), RequestHeadersUriSpec.class);
    }

    public RequestBodyUriSpec method(HttpMethod method) {
      return wrap(client.method(method), RequestBodyUriSpec.class);
    }

    public Builder mutate() {
      throw new UnsupportedOperationException();
    }
  }

  private class RequestHeadersSpecProxy implements InvocationHandler {

    RequestHeadersSpec<?> requestHeadersSpec;

    public RequestHeadersSpecProxy(RequestHeadersSpec<?> requestHeadersSpec) {
      this.requestHeadersSpec = requestHeadersSpec;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws InvocationTargetException, IllegalAccessException {
      try {
        Method thisMethod = getClass().getMethod(method.getName(), method.getParameterTypes());
        checkArgument(
            thisMethod.getReturnType() == method.getReturnType(),
            "Return types don't match. Expected: %s",
            method.getReturnType());
        return thisMethod.invoke(this, args);
      } catch (NoSuchMethodException e) {
        Object o = method.invoke(requestHeadersSpec, args);
        if (o == requestHeadersSpec) {
          return proxy;
        }
        return o;
      }
    }

    @Nullable
    private ClientResponse extractSession(@Nullable ClientResponse clientResponse) {
      if (clientResponse != null && clientResponse.statusCode().is2xxSuccessful()) {
        csrfToken = null;
        Optional.ofNullable(clientResponse.cookies().get(DEFAULT_CSRF_COOKIE_NAME))
            .ifPresent(value -> csrfToken = Iterables.getOnlyElement(value).getValue());

        jSessionId = null;
        Optional.ofNullable(clientResponse.cookies().get(DEFAULT_JSESSIONID_COOKIE_NAME))
            .ifPresent(value -> jSessionId = Iterables.getOnlyElement(value).getValue());
      }
      return clientResponse;
    }

    public ResponseSpec retrieve() {
      ResponseSpec responseSpec = requestHeadersSpec.retrieve();
      responseSpec.onStatus(
          HttpStatusCode::is2xxSuccessful,
          clientResponse -> {
            extractSession(clientResponse);
            return Mono.empty();
          });
      return responseSpec;
    }

    @Deprecated
    public Mono<ClientResponse> exchange() {
      Mono<ClientResponse> clientResponse = requestHeadersSpec.exchange();
      extractSession(clientResponse.block());
      return clientResponse;
    }

    public <V> Flux<V> exchangeToFlux(Function<ClientResponse, ? extends Flux<V>> responseHandler) {
      return requestHeadersSpec.exchangeToFlux(
          clientResponse -> responseHandler.apply(extractSession(clientResponse)));
    }

    public <V> Mono<V> exchangeToMono(Function<ClientResponse, ? extends Mono<V>> responseHandler) {
      return requestHeadersSpec.exchangeToMono(
          clientResponse -> responseHandler.apply(extractSession(clientResponse)));
    }
  }

  String csrfToken;
  String jSessionId;

  public String getCsrfToken() {
    return csrfToken;
  }

  public String getJSessionId() {
    return jSessionId;
  }

  public WebClient wrap(WebClient client) {
    if (csrfToken != null) {
      client.head().header(DEFAULT_CSRF_HEADER_NAME, csrfToken);
    }
    if (jSessionId != null) {
      client.head().cookie(DEFAULT_JSESSIONID_COOKIE_NAME, jSessionId);
    }

    return new WebClientProxy(client);
  }

  public WebTestClient wrap(WebTestClient client) {
    if (csrfToken != null) {
      client.head().header(DEFAULT_CSRF_HEADER_NAME, csrfToken);
    }
    if (jSessionId != null) {
      client.head().cookie(DEFAULT_JSESSIONID_COOKIE_NAME, jSessionId);
    }

    return client;
  }

  private <T extends RequestHeadersSpec<?>> T wrap(T requestHeadersSpec, Class<T> type) {
    return (T)
        Proxy.newProxyInstance(
            WebClient.class.getClassLoader(),
            new Class[] {type},
            new RequestHeadersSpecProxy(requestHeadersSpec));
  }

  public User login(WebClient client, String username, String password) {
    return client
        .post()
        .uri("/api/login.html")
        .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .body(BodyInserters.fromFormData("username", username).with("password", password))
        .retrieve()
        .bodyToMono(User.class)
        .block();
  }

  public <S extends Message, R extends Message> R protoRequest(
      WebClient webClient, String uri, S request, Class<R> responseType) {
    ResponseSpec response =
        webClient
            .post()
            .uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, PROTOBUF_MIME_TYPE)
            .accept(MediaType.parseMediaType(PROTOBUF_MIME_TYPE))
            .body(BodyInserters.fromValue(request.toByteArray()))
            .retrieve();
    return bodyAsProto(response, responseType);
  }
}
