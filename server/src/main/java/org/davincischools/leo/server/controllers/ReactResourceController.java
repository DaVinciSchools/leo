package org.davincischools.leo.server.controllers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.utils.HttpServletProxy;
import org.davincischools.leo.server.utils.URIBuilder;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves static React content from the java classpath or proxies requests to a development React
 * web server if the <code>--react_port</code> flag is present.
 *
 * <p>In addition to building the React server as part of the Maven configuration, the built files
 * are copied into <code>/target/classes/.../www</code>. This places the built content in the <code>
 * *.jar</code> and makes it available via <code>ClassLoader.getSystemResourceAsStream</code>.
 */
@RestController
public class ReactResourceController {

  // Optional port of running React web development server.
  @Value("${react_port:0}")
  private int reactPort;

  public static final ImmutableMap<String, MediaType> EXTENSIONS_TO_MIME_TYPES =
      ImmutableMap.<String, MediaType>builder()
          .put("apng", MediaType.create("image", "apng"))
          .put("avi", MediaType.create("video", "x-msvideo"))
          .put("avif", MediaType.create("image", "avif"))
          .put("css", MediaType.CSS_UTF_8)
          .put("csv", MediaType.CSV_UTF_8)
          .put("gif", MediaType.GIF)
          .put("html", MediaType.HTML_UTF_8)
          .put("ico", MediaType.ICO)
          .put("jfif", MediaType.JPEG)
          .put("jpeg", MediaType.JPEG)
          .put("jpg", MediaType.JPEG)
          .put("js", MediaType.JAVASCRIPT_UTF_8)
          .put("json", MediaType.JSON_UTF_8)
          .put("m2v", MediaType.MPEG_VIDEO)
          .put("m4v", MediaType.create("video", "x-m4v"))
          .put("map", MediaType.JSON_UTF_8)
          .put("mov", MediaType.QUICKTIME)
          .put("movie", MediaType.QUICKTIME)
          .put("mp4", MediaType.MP4_VIDEO)
          .put("mpe", MediaType.MPEG_VIDEO)
          .put("mpeg", MediaType.MPEG_VIDEO)
          .put("mpg", MediaType.MPEG_VIDEO)
          .put("ogv", MediaType.OGG_VIDEO)
          .put("pdf", MediaType.PDF)
          .put("pjp", MediaType.JPEG)
          .put("pjpeg", MediaType.JPEG)
          .put("png", MediaType.PNG)
          .put("qt", MediaType.QUICKTIME)
          .put("svg", MediaType.SVG_UTF_8)
          .put("txt", MediaType.PLAIN_TEXT_UTF_8)
          .put("webm", MediaType.WEBM_VIDEO)
          .put("webp", MediaType.WEBP)
          .build();

  public static final ImmutableSetMultimap<MediaType, String> MIME_TYPES_TO_EXTENSIONS =
      ImmutableSetMultimap.copyOf(
          EXTENSIONS_TO_MIME_TYPES.entrySet().stream()
              .flatMap(
                  entry ->
                      Stream.of(
                          Maps.immutableEntry(entry.getKey(), entry.getValue()),
                          Maps.immutableEntry(
                              entry.getKey().toUpperCase(), entry.getValue().withoutParameters())))
              .collect(
                  Multimaps.toMultimap(
                      Entry::getValue,
                      Entry::getKey,
                      MultimapBuilder.hashKeys().linkedHashSetValues()::build))
              .entries());

  @Autowired private Database db;

  @RequestMapping({
    // This needs to be kept in sync with ServerApplication.SecurityConfigurer.
    "/",
    "/admin/**",
    "/classes/**",
    "/dashboards/**",
    "/demos/**",
    "/docs/**",
    "/favicon.*",
    "/gtag.js",
    "/images/**",
    "/index.html",
    "/manifest.json",
    "/profiles/**",
    "/projects/**",
    "/robots.txt",
    "/schools/**",
    "/static/**",
    "/users/**",
    // TODO: Move these out of the prod server.
    // React developer tools plugin.
    "/installHooks.js",
    // Webpack server hot reload files:
    // https://github.com/webpack/webpack-dev-server.
    "/main.*.hot-update.js",
    "/main.*.hot-update.js.map",
    "/main.*.hot-update.json"
  })
  public void getResource(
      HttpServletRequest originalRequest, HttpServletResponse response, HttpExecutors httpExecutors)
      throws IOException {
    httpExecutors
        .start(originalRequest)
        .setOnlyLogOnFailure(true)
        .andThen(
            (request, log) -> {
              URI uri = getUri(request);
              Optional<MediaType> mediaType = getResponseMimeType(uri);
              if (uri.getPath().equals("/gtag.js")) {
                // Forward the request to the Google Analytics server.
                HttpRequest.Builder gtagRequest =
                    HttpRequest.newBuilder()
                        .uri(URI.create("https://www.googletagmanager.com/gtag/js?id=G-NLHRTMZB65"))
                        .version(Version.HTTP_2)
                        .timeout(Duration.ofSeconds(5))
                        .GET();
                HttpResponse<String> gtagResponse =
                    HttpClient.newBuilder()
                        .followRedirects(Redirect.ALWAYS)
                        .build()
                        .send(gtagRequest.build(), BodyHandlers.ofString(StandardCharsets.UTF_8));
                // Handle the status. Return if there was an error.
                if (gtagResponse.statusCode() != HttpURLConnection.HTTP_OK) {
                  response.sendError(gtagResponse.statusCode(), uri.getPath());
                } else {
                  response.setContentType(
                      gtagResponse
                          .headers()
                          .firstValue("content-type")
                          .orElse(MediaType.JAVASCRIPT_UTF_8.toString()));
                  response.getOutputStream().write(gtagResponse.body().getBytes());
                  response.getOutputStream().flush();
                  response.getOutputStream().close();
                  response.setStatus(gtagResponse.statusCode());
                }
              } else if (reactPort > 0) {
                // Forward the request to the React server running locally.
                HttpServletProxy.sendExternalRequest(uri, reactPort, mediaType, request, response);
              } else {
                // Get and copy a resource from the classpath.
                mediaType.map(Object::toString).ifPresent(response::setContentType);
                try (InputStream in = getSystemResourceAsStreamOrIndex(uri)) {
                  if (in == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, uri.getPath());
                    throw new IOException("Resource not found: " + uri.getPath());
                  }
                  ByteStreams.copy(in, response.getOutputStream());
                  response.getOutputStream().flush();
                  response.getOutputStream().close();
                }
              }

              return response;
            })
        .finish();
  }

  private InputStream getSystemResourceAsStreamOrIndex(URI uri) {
    InputStream in =
        ClassLoader.getSystemResourceAsStream("org/davincischools/leo/server/www" + uri.getPath());
    if (in != null) {
      return in;
    }
    return ClassLoader.getSystemResourceAsStream("org/davincischools/leo/server/www/index.html");
  }

  private static URI getUri(HttpServletRequest request) throws IOException {
    try {
      // Get the normalized request path (e.g., removing "..").
      URI uri = URI.create(request.getRequestURL().toString()).normalize();
      // If a path was not provided, return the index page.
      if (uri.getPath().equals("/")) {
        uri = URIBuilder.fromUri(uri).setPath("/index.html").build();
      }
      return uri;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  private static Optional<MediaType> getResponseMimeType(URI uri) {
    // Get resource type from filename type.
    String fileExtension = Files.getFileExtension(uri.getPath());
    return Optional.ofNullable(EXTENSIONS_TO_MIME_TYPES.get(fileExtension));
  }
}
