package org.davincischools.leo.server.controllers;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import jakarta.servlet.http.MappingMatch;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Log;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.protos.error_service.ReportErrorRequest;
import org.davincischools.leo.protos.error_service.ReportErrorResponse;
import org.davincischools.leo.protos.jira.Assignee;
import org.davincischools.leo.protos.jira.CreateIssueRequest;
import org.davincischools.leo.protos.jira.CreateIssueRequest.Fields;
import org.davincischools.leo.protos.jira.CreateIssueResponse;
import org.davincischools.leo.protos.jira.FindUsersAndGroupsRequest;
import org.davincischools.leo.protos.jira.FindUsersAndGroupsResponse;
import org.davincischools.leo.protos.jira.IssueType;
import org.davincischools.leo.protos.jira.Parent;
import org.davincischools.leo.protos.jira.Project;
import org.davincischools.leo.protos.jira.Reporter;
import org.davincischools.leo.server.utils.JiraUtils;
import org.davincischools.leo.server.utils.ProtoDaoUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorLog;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Anonymous;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletMapping;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class ErrorService {

  private static final Logger logger = LogManager.getLogger();
  private static Map<String, Optional<String>> accountIds =
      Collections.synchronizedMap(new HashMap<>());

  @Value("${" + JiraUtils.ATLASSIAN_USERNAME_EMAIL_PROP_NAME + ":}")
  String atlassianUsernameEmail;

  @Value("${" + JiraUtils.ATLASSIAN_REPORTER_EMAIL_PROP_NAME + ":}")
  String atlassianReporterEmail;

  @Value("${" + JiraUtils.ATLASSIAN_ASSIGNEE_EMAIL_PROP_NAME + ":}")
  String atlassianAssigneeEmail;

  private final JiraUtils jiraUtils;
  private final DispatcherServlet dispatcherServlet;

  public ErrorService(
      @Autowired JiraUtils jiraUtils, @Autowired DispatcherServlet dispatcherServlet) {
    this.jiraUtils = jiraUtils;
    this.dispatcherServlet = dispatcherServlet;

    if (Strings.isNullOrEmpty(atlassianAssigneeEmail)) {
      logger.atWarn().log("Not creating bugs for errors because no JIRA assignee is specified.");
    }
  }

  @PostMapping(value = "/api/protos/ErrorService/ReportError")
  @ResponseBody
  public ReportErrorResponse reportError(
      @Anonymous HttpUserX userX,
      @RequestBody Optional<ReportErrorRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(ReportErrorRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = ReportErrorResponse.newBuilder();

              log.setStatus(Log.StatusType.ERROR);
              try {
                if (!Strings.isNullOrEmpty(atlassianAssigneeEmail)) {
                  CreateIssueResponse issueResponse = createIssueForError(log, userX, request);
                  String issueLink =
                      "https://projectleo.atlassian.net/browse/" + issueResponse.getKey();
                  response.setIssueLink(issueLink);
                  log.setIssueLink(issueLink);
                }
              } catch (Exception e) {
                log.addNote(Throwables.getStackTraceAsString(e));
                response.setFailureReason(e.getMessage());
              }

              return response.build();
            })
        .finish();
  }

  /** This is a best effort conversion of the request body into a proto string for a bug report. */
  private String requestBodyToText(
      byte[] bytes, Optional<HandlerExecutionChain> handlerExecutionChain) {
    if (handlerExecutionChain.isPresent()
        && handlerExecutionChain.get().getHandler() instanceof HandlerMethod handlerMethod) {
      return bytesToText(bytes, List.of(handlerMethod.getMethod().getGenericParameterTypes()));
    }
    return bytesToText(bytes, List.of());
  }

  /** This is a best effort conversion of the response body into a proto string for a bug report. */
  private String responseBodyToText(
      byte[] bytes, Optional<HandlerExecutionChain> handlerExecutionChain) {
    if (handlerExecutionChain.isPresent()
        && handlerExecutionChain.get().getHandler() instanceof HandlerMethod handlerMethod) {
      return bytesToText(bytes, List.of(handlerMethod.getMethod().getGenericReturnType()));
    }
    return bytesToText(bytes, List.of());
  }

  private static String bytesToText(byte[] bytes, Iterable<Type> possibleProtoParamTypes) {
    if (bytes.length == 0) {
      return "None";
    }

    // Check for a proto type.
    var paramTypes = Lists.newLinkedList(possibleProtoParamTypes);
    while (!paramTypes.isEmpty()) {
      var type = paramTypes.removeFirst();
      if (type instanceof ParameterizedType parameterizedType) {
        paramTypes.addAll(0, List.of(parameterizedType.getActualTypeArguments()));
        continue;
      }
      try {
        var typeClass = Class.forName(type.getTypeName());
        if (Message.class.isAssignableFrom(typeClass)) {
          var protoBuilder = (Message.Builder) typeClass.getMethod("newBuilder").invoke(null);
          Message proto = protoBuilder.mergeFrom(bytes).build();
          return proto.getClass().getSimpleName() + ": {\n" + proto.toString().trim() + "\n}";
        }
      } catch (Exception e) {
        // This wasn't the proto type we were looking for. Skip to the next type.
      }
    }

    // Check for UTF-8 encoded text.
    String text = new String(bytes, StandardCharsets.UTF_8);
    if (Arrays.compare(text.getBytes(StandardCharsets.UTF_8), bytes) == 0) {
      return "text: " + text.translateEscapes();
    }

    // Fall back to base64.
    return "base64: " + Base64.getEncoder().encodeToString(bytes);
  }

  /** This is a best effort search for the handler for this request. */
  private Optional<HandlerExecutionChain> getHandler(URI requestUri) {
    try {
      MockHttpServletRequest mockServletRequest = new MockHttpServletRequest();
      mockServletRequest.setRequestURI(requestUri.getPath());
      mockServletRequest.setHttpServletMapping(
          new MockHttpServletMapping("", "/", "dispatchServlet", MappingMatch.DEFAULT));

      // This can be slow and exhaustive since it's only for when an error occurs.
      for (HandlerMapping mapping :
          Optional.ofNullable(dispatcherServlet.getHandlerMappings())
              .orElse(Collections.emptyList())) {
        for (var method : List.of("POST", "GET")) {
          mockServletRequest.setMethod(method);
          try {
            HandlerExecutionChain handler = mapping.getHandler(mockServletRequest);
            if (handler != null) {
              return Optional.of(handler);
            }
          } catch (HttpRequestMethodNotSupportedException e) {
            // Ignore. We'll try both methods.
          }
        }
      }
    } catch (Exception e) {
      logger.atWarn().withThrowable(e).log("Failed to get handler for request URI: {}", requestUri);
    }
    return Optional.empty();
  }

  public CreateIssueResponse createIssueForError(
      HttpExecutorLog log, HttpUserX userX, ReportErrorRequest request) {
    Optional<String> reporterAccountId =
        getAccountIdForEmailAddress(userX.get().map(UserX::getEmailAddress).orElse(""))
            .or(() -> getAccountIdForEmailAddress(atlassianReporterEmail))
            .or(() -> getAccountIdForEmailAddress(atlassianUsernameEmail));
    Optional<String> assigneeAccountId =
        getAccountIdForEmailAddress(atlassianAssigneeEmail)
            .or(() -> getAccountIdForEmailAddress(atlassianUsernameEmail));

    Optional<HandlerExecutionChain> handlerExecutionChain =
        getHandler(URI.create(request.getRequestUrl()));
    String requestBodyText =
        requestBodyToText(request.getRequestBody().toByteArray(), handlerExecutionChain);
    String responseBodyText =
        responseBodyToText(request.getResponseBody().toByteArray(), handlerExecutionChain);

    if (reporterAccountId.isEmpty() || assigneeAccountId.isEmpty()) {
      throw new IllegalArgumentException("Unable to identify JIRA account Ids to create a bug.");
    } else {
      StringBuilder description = new StringBuilder();
      description.append("*Name:* ").append(request.getName()).append("\n\n");
      description
          .append("*UserX:*\n```\n")
          .append(
              ProtoDaoUtils.toUserXProto(
                      userX.get().orElse(null),
                      org.davincischools.leo.protos.pl_types.UserX::newBuilder)
                  .map(Object::toString)
                  .orElse("Unknown")
                  .replaceAll(Pattern.quote("`"), "\\`")
                  .replaceAll(Pattern.quote("*"), "\\*")
                  .replaceAll("_", "\\_")
                  .trim())
          .append("\n```\n\n");
      description
          .append("*Request:*\n```\n")
          .append(
              requestBodyText
                  .replaceAll(Pattern.quote("`"), "\\`")
                  .replaceAll(Pattern.quote("*"), "\\*")
                  .replaceAll("_", "\\_"))
          .append("\n```\n\n");
      description
          .append("*Response:*\n```\n")
          .append(
              responseBodyText
                  .replaceAll(Pattern.quote("`"), "\\`")
                  .replaceAll(Pattern.quote("*"), "\\*")
                  .replaceAll("_", "\\_"))
          .append("\n```\n\n");
      description
          .append("*ReportErrorRequest:*\n```\n")
          .append(
              request.toBuilder()
                  .clearRequestBody()
                  .clearResponseBody()
                  .build()
                  .toString()
                  .translateEscapes()
                  .replaceAll(Pattern.quote("`"), "\\`")
                  .replaceAll(Pattern.quote("*"), "\\*")
                  .replaceAll("_", "\\_")
                  .trim())
          .append("\n```\n\n");

      CreateIssueResponse issueResponse =
          jiraUtils.createIssue(
              CreateIssueRequest.newBuilder()
                  .setFields(
                      Fields.newBuilder()
                          .setProject(Project.newBuilder().setKey("PL"))
                          .setReporter(Reporter.newBuilder().setId(reporterAccountId.get()))
                          .setAssignee(Assignee.newBuilder().setId(assigneeAccountId.get()))
                          .setIssuetype(IssueType.newBuilder().setName("Bug").build())
                          .setParent(Parent.newBuilder().setKey(/* Automated Bugs */ "PL-85"))
                          .setSummary("Error: " + request.getSourceUrl() + "  --  " + Instant.now())
                          .setDescription(description.toString())
                          .addLabels("ErrorService")));
      log.addNote(TextFormat.printer().printToString(issueResponse));
      if (issueResponse.hasProjectLeoError()) {
        throw new IllegalArgumentException("Failed to create issue: " + issueResponse);
      } else {
        return issueResponse;
      }
    }
  }

  public Optional<String> getAccountIdForEmailAddress(String emailAddress) {
    if (Strings.isNullOrEmpty(emailAddress)) {
      return Optional.empty();
    }

    return accountIds.computeIfAbsent(
        emailAddress,
        key -> {
          FindUsersAndGroupsResponse response =
              jiraUtils.sendGetRequest(
                  "/rest/api/3/groupuserpicker",
                  FindUsersAndGroupsRequest.newBuilder()
                      .setQuery(atlassianAssigneeEmail)
                      .setMaxResults(2),
                  FindUsersAndGroupsResponse.class);
          if (response.hasProjectLeoError()) {
            logger
                .atWarn()
                .log(
                    "Unable to identify a JIRA account for '{}'. Search result: {}", key, response);
            return Optional.empty();
          } else if (response.getUsers().getUsersList().size() != 1) {
            logger
                .atWarn()
                .log(
                    "Unable to identify a single JIRA account for '{}'. Search results: {}",
                    key,
                    response);
            return Optional.empty();
          } else {
            return Optional.of(response.getUsers().getUsers(0).getAccountId());
          }
        });
  }
}
