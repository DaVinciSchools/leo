package org.davincischools.leo.server.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This forwards a HttpServletRequest to another server and then translates its response back into
 * an HttpServletResponse. It rewrites the host and port in the request and response for the other
 * server for it to handle the request correctly.
 */
public class HttpServletProxy {

  // A helper to replace the host and port with the external server's host and port, and vice versa.
  static class URIRewriter {
    private final String springHostPort;
    private final String externalHostPort;

    public URIRewriter(URI springServerUri, int externalPort) {
      this.springHostPort =
          springServerUri.getHost()
              + (springServerUri.getPort() >= 0 ? ":" + springServerUri.getPort() : "");
      this.externalHostPort = "localhost:" + externalPort;
    }

    public String rewriteForClient(String str) {
      return str.replaceAll(Pattern.quote(springHostPort), externalHostPort);
    }

    public String rewriteForSpring(String str) {
      return str.replaceAll(externalHostPort, springHostPort);
    }
  }

  // Http header name constants.
  private static final ImmutableSet<String> COOKIE_HEADERS =
      ImmutableSet.of(HttpHeaders.SET_COOKIE.toLowerCase(), HttpHeaders.SET_COOKIE2.toLowerCase());
  private static final ImmutableSet<String> HEADERS_THAT_WILL_BE_AUTO_POPULATED =
      ImmutableSet.of(HttpHeaders.ETAG.toLowerCase(), HttpHeaders.CONTENT_LENGTH.toLowerCase());

  // Send the request and populates the response.
  public static void sendExternalRequest(
      URI uri,
      int externalPort,
      Optional<MediaType> mediaType,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    try {
      URIRewriter uriRewriter = new URIRewriter(uri, externalPort);

      HttpRequest.Builder externalRequest =
          HttpRequest.newBuilder()
              .uri(URI.create(uriRewriter.rewriteForClient(uri.toString())))
              .version(Version.HTTP_2)
              .timeout(Duration.ofSeconds(5))
              .GET();

      copyHeaders(request, externalRequest, uriRewriter);

      callAndProcessExternalResponse(
          uri, mediaType, uriRewriter, request, response, externalRequest.build());
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  public static void copyHeaders(HttpServletRequest fromRequest, Builder toRequest) {
    copyHeaders(fromRequest, toRequest, null);
  }

  public static void copyHeaders(
      HttpServletRequest fromRequest, Builder toRequest, URIRewriter uriRewriter) {
    if (fromRequest.getHeaderNames() != null) {
      for (String name : Lists.newArrayList(fromRequest.getHeaderNames().asIterator())) {
        if (HEADERS_THAT_WILL_BE_AUTO_POPULATED.contains(name.toLowerCase())) {
          continue;
        }
        if (fromRequest.getHeaders(name) != null) {
          for (String value : Lists.newArrayList(fromRequest.getHeaders(name).asIterator())) {
            toRequest.header(
                name, uriRewriter != null ? uriRewriter.rewriteForClient(value) : value);
          }
        }
      }
    }
  }

  public static <T> void copyHeaders(HttpResponse<T> fromResponse, HttpServletResponse toResponse) {
    copyHeaders(fromResponse, toResponse, null);
  }

  public static <T> void copyHeaders(
      HttpResponse<T> fromResponse, HttpServletResponse toResponse, URIRewriter uriRewriter) {
    for (Entry<String, List<String>> header : fromResponse.headers().map().entrySet()) {
      String name = header.getKey();
      if (HEADERS_THAT_WILL_BE_AUTO_POPULATED.contains(name.toLowerCase())) {
        continue;
      } else if (COOKIE_HEADERS.contains(name.toLowerCase())) {
        for (String value : header.getValue()) {
          for (HttpCookie externalCookie :
              HttpCookie.parse(uriRewriter != null ? uriRewriter.rewriteForSpring(value) : value)) {
            toResponse.addCookie(convertCookie(externalCookie));
          }
        }
      } else {
        header
            .getValue()
            .forEach(
                value ->
                    toResponse.addHeader(
                        name, uriRewriter != null ? uriRewriter.rewriteForSpring(value) : value));
      }
    }
  }

  // Call the external server and translate its response into the HttpServletResponse, and URLs to
  // originalHostPort.
  private static void callAndProcessExternalResponse(
      URI uri,
      Optional<MediaType> mediaType,
      URIRewriter uriRewriter,
      HttpServletRequest request,
      HttpServletResponse response,
      HttpRequest externalRequest)
      throws IOException {
    try {

      // Send the request to the external server.
      HttpResponse<byte[]> externalResponse =
          HttpClient.newBuilder()
              .followRedirects(Redirect.ALWAYS)
              .build()
              .send(externalRequest, BodyHandlers.ofByteArray());

      // Handle the status. Return if there was an error.
      if (externalResponse.statusCode() != HttpURLConnection.HTTP_OK) {
        response.sendError(externalResponse.statusCode(), uri.getPath());
        return;
      }
      response.setStatus(externalResponse.statusCode());

      copyHeaders(externalResponse, response, uriRewriter);

      // If the response is in text format, replace externalHostPort with originalHostPort.
      byte[] externalBody = externalResponse.body();
      if (mediaType.isPresent() && mediaType.get().type().equals(MediaType.ANY_TEXT_TYPE.type())) {
        byte[] decodedBytes =
            decodeBody(
                externalResponse.headers().allValues(HttpHeaders.CONTENT_ENCODING), externalBody);
        String body =
            new String(decodedBytes, mediaType.get().charset().or(StandardCharsets.UTF_8));
        externalBody =
            uriRewriter
                .rewriteForSpring(body)
                .getBytes(mediaType.get().charset().or(StandardCharsets.UTF_8));
        response
            .getOutputStream()
            .write(
                encodeBody(
                    externalResponse.headers().allValues(HttpHeaders.CONTENT_ENCODING),
                    decodedBytes));
      } else {
        response.getOutputStream().write(externalBody);
      }

      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  private static Cookie convertCookie(HttpCookie externalCookie) {
    Cookie cookie = new Cookie(externalCookie.getName(), externalCookie.getValue());
    cookie.setHttpOnly(externalCookie.isHttpOnly());
    cookie.setSecure(externalCookie.getSecure());
    cookie.setPath(externalCookie.getPath());
    cookie.setDomain(externalCookie.getDomain());
    cookie.setMaxAge((int) externalCookie.getMaxAge());
    return cookie;
  }

  // Http responses can be encoded (e.g., compressed). So the page may come back as a series
  // of compressed bytes. This checks the encoding used and decodes the byte array.
  private static byte[] decodeBody(List<String> encoding, byte[] body) throws IOException {
    // Get the encoding or return the unmodified byte array.
    if (encoding == null || encoding.isEmpty()) {
      return body;
    }

    // Decode the body using the given encoding.
    switch (Iterables.getOnlyElement(encoding)) {
      case "gzip":
        return ByteStreams.toByteArray(new GZIPInputStream(new ByteArrayInputStream(body)));
      default:
        throw new IOException("Encoding not supported: " + encoding.get(0));
    }
  }

  // Http responses can be encoded (e.g., compressed). This applies the encoding to the byte array
  // to send in the response.
  private static byte[] encodeBody(List<String> encoding, byte[] decodedBody) throws IOException {
    // Get the encoding or return the unmodified byte array.
    if (encoding == null || encoding.isEmpty()) {
      return decodedBody;
    }

    // Encode the body using the given encoding.
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    switch (Iterables.getOnlyElement(encoding)) {
      case "gzip":
        try (OutputStream out = new GZIPOutputStream(byteOutputStream)) {
          out.write(decodedBody);
        }
        break;
      default:
        throw new IOException("Encoding not supported: " + encoding.get(0));
    }
    return byteOutputStream.toByteArray();
  }
}
