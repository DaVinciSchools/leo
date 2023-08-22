package org.davincischools.leo.server.controllers;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.protobuf.TextFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.LogRepository.Status;
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
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorLog;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Anonymous;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

  @Autowired Database db;
  @Autowired JiraUtils jiraUtils;

  public ErrorService(@Autowired Database db, @Autowired JiraUtils jiraUtils) {
    this.db = db;
    this.jiraUtils = jiraUtils;

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

              log.setStatus(Status.ERROR);
              try {
                if (!Strings.isNullOrEmpty(atlassianAssigneeEmail)) {
                  CreateIssueResponse issueResponse = createBugForError(log, userX, request);
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

  public CreateIssueResponse createBugForError(
      HttpExecutorLog log, HttpUserX userX, ReportErrorRequest request) {
    Optional<String> reporterAccountId =
        getAccountIdForEmailAddress(userX.get().map(UserX::getEmailAddress).orElse(""))
            .or(() -> getAccountIdForEmailAddress(atlassianReporterEmail))
            .or(() -> getAccountIdForEmailAddress(atlassianUsernameEmail));
    Optional<String> assigneeAccountId =
        getAccountIdForEmailAddress(atlassianAssigneeEmail)
            .or(() -> getAccountIdForEmailAddress(atlassianUsernameEmail));

    if (reporterAccountId.isEmpty() || assigneeAccountId.isEmpty()) {
      throw new IllegalArgumentException("Unable to identify JIRA account Ids to create a bug.");
    } else {
      StringBuilder description = new StringBuilder();
      description.append("*Name:* " + request.getName() + "\n\n");
      description.append(
          "*URL:*\n"
              + "_From:_ "
              + request.getUrl()
              + "\n"
              + "_To:_ "
              + request.getRequest().getUrl()
              + "\n"
              + "_Via:_ "
              + request.getResponse().getUrl()
              + "\n"
              + "\n\n");
      description.append(
          "*Message:*\n"
              + request
                  .getMessage()
                  .translateEscapes()
                  .replaceAll(Pattern.quote("*"), "\\*")
                  .replaceAll("_", "\\_")
              + "\n\n");
      description.append("*Stack:* " + request.getStack() + "\n\n");
      description.append(
          "*User ID:* " + userX.get().map(UserX::getId).map(Object::toString).orElse("Unknown"));

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
                          .setSummary(
                              org.apache.logging.log4j.util.Strings.left(
                                  (request.getName() + ": " + request.getUrl())
                                      .replaceAll("(?s)\\s+", " ")
                                      .trim(),
                                  255))
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
