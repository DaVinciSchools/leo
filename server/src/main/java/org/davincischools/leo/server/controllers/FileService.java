package org.davincischools.leo.server.controllers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.davincischools.leo.database.daos.FileX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.FileXRepository;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FileService {

  private static final String FILE_PART = "file";
  private static final String ID_FIELD = "id";
  private static final String KEY_FIELD = "key";
  public static final int MAX_FILE_SIZE = 1024 * 1024 * 16 - 1; // 16 MB

  private static final SecureRandom random = new SecureRandom();

  @Autowired Database db;
  @Autowired FileXRepository fileXRepository;

  @GetMapping(value = "/api/FileService/GetFile")
  public void getFile(
      HttpServletRequest request, HttpServletResponse response, @Authenticated HttpUserX userX)
      throws IOException {
    if (!userX.isAuthenticated()) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    int fileId = Integer.parseInt(request.getParameter(ID_FIELD));
    String fileKey = request.getParameter(KEY_FIELD);

    FileX fileX = fileXRepository.findByIdWithUserX(fileId).orElse(null);
    if (fileX == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    if (!Objects.equals(fileX.getFileKey(), fileKey)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // Byte range support
    String rangeHeader = request.getHeader("Range");
    byte[] fileContent = fileX.getFileContent();

    if (rangeHeader != null) {
      String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
      long start = Long.parseLong(ranges[0]);
      long end =
          ranges.length > 1 && !ranges[1].isEmpty()
              ? Long.parseLong(ranges[1])
              : fileContent.length - 1;

      if (start > end || start < 0 || end >= fileContent.length) {
        response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
        return;
      }

      response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileContent.length);
      response.setHeader("Accept-Ranges", "bytes");
      response.setContentType(fileX.getMimeType());
      response.setContentLength((int) (end - start + 1));
      response.getOutputStream().write(fileContent, (int) start, (int) (end - start + 1));
      response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    } else {
      response.setContentType(fileX.getMimeType());
      response.setContentLength(fileContent.length);
      response.getOutputStream().write(fileContent);
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  @PostMapping(value = "/api/FileService/PostFile")
  public void postFile(
      HttpServletRequest request, HttpServletResponse response, @Authenticated HttpUserX userX)
      throws IOException, ServletException {
    if (!userX.isAuthenticated()) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    Part filePart = request.getPart(FILE_PART);
    if (filePart == null || filePart.getContentType() == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    if (filePart.getSize() > MAX_FILE_SIZE) {
      response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
      return;
    }

    MediaType mediaType = MediaType.parse(filePart.getContentType());
    Set<String> fileExtensions = ReactResourceController.MIME_TYPES_TO_EXTENSIONS.get(mediaType);
    if (fileExtensions.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
      return;
    }

    var fileName =
        Optional.ofNullable(filePart.getSubmittedFileName())
            .orElseGet(() -> "file." + Iterables.get(fileExtensions, 0));
    if (ReactResourceController.EXTENSIONS_TO_MIME_TYPES.get(
            Files.getFileExtension(fileName).toLowerCase())
        == null) {
      response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
      return;
    }

    byte[] randId = new byte[15];
    random.nextBytes(randId);
    String fileKey = Base64.getEncoder().encodeToString(randId);

    // TODO: Work around for a Froala URL encoding bug.
    fileKey = fileKey.replaceAll("[+/=]", "_");

    FileX fileX =
        fileXRepository.save(
            new FileX()
                .setCreationTime(Instant.now())
                .setUserX(userX.getUserXOrNull())
                .setFileName(fileName)
                .setMimeType(mediaType.toString())
                .setFileContent(ByteStreams.toByteArray(filePart.getInputStream()))
                .setFileKey(fileKey));

    String fileUrl =
        ImmutableMap.<String, String>builder()
            .put(ID_FIELD, Integer.toString(fileX.getId()))
            .put(KEY_FIELD, fileKey)
            .build()
            .entrySet()
            .stream()
            .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&", "/api/FileService/GetFile?", ""));

    response.getWriter().write(new JSONObject().put("link", fileUrl).toString());
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
