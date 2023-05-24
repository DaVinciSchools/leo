package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static org.davincischools.leo.server.utils.HttpUserProvider.isAdmin;
import static org.davincischools.leo.server.utils.HttpUserProvider.isStudent;
import static org.davincischools.leo.server.utils.HttpUserProvider.isTeacher;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.Database.KNOWLEDGE_AND_SKILL_TYPE;
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
import org.davincischools.leo.protos.pl_types.Eks;
import org.davincischools.leo.protos.pl_types.XqCompetency;
import org.davincischools.leo.protos.project_management.GenerateProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateProjectsResponse;
import org.davincischools.leo.protos.project_management.GetEksRequest;
import org.davincischools.leo.protos.project_management.GetEksResponse;
import org.davincischools.leo.protos.project_management.GetProjectsRequest;
import org.davincischools.leo.protos.project_management.GetProjectsResponse;
import org.davincischools.leo.protos.project_management.GetXqCompetenciesRequest;
import org.davincischools.leo.protos.project_management.GetXqCompetenciesResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.HttpUserProvider.Authenticated;
import org.davincischools.leo.server.utils.HttpUserProvider.Student;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.davincischools.leo.server.utils.LogUtils.LogOperations;
import org.davincischools.leo.server.utils.LogUtils.Status;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectManagementService {

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");

  private static final Pattern REMOVE_FLUFF =
      Pattern.compile(
          // Match newlines with the '.' character.
          "(?s)"
              // Match the start of the line.
              + "^"
              // A number, e.g., "1: " or "1. "
              + "([0-9]+[:.])?\\s*"
              // A label, e.g., "Title - " or "Full Description: "
              + "(([Pp]roject\\s*[0-9]*|[Tt]itle|[Ss]hort|[Ff]ull|[Ss]ummary)?\\s*"
              + "([Dd]escription)?)?\\s*"
              // The end of the label, e.g., the ":" or " - " from above.
              + "(\\.|:| -)?\\s*" //
              // A second number for, e.g., "Project: 1. "
              + "([0-9]+[:.])?\\s*" //
              // A title after the label, e.g., "Project 1 - Title: "
              + "(Title:)?\\s*" //
              // The main text, either quoted or not. Quotes are removed.
              + "([\"“”](?<quotedText>.*)[\"“”]|(?<unquotedText>.*))"
              // Match the end of the line.
              + "$");
  private static final ImmutableList<String> TEXT_GROUPS =
      ImmutableList.of("quotedText", "unquotedText");

  // Do NOT have any special Regex characters in these delimiters.
  static final String START_OF_PROJECT = "~~prj:ProjectLeoDelimiter~~";
  static final String END_OF_TITLE = "~~title:ProjectLeoDelimiter~~";
  static final String END_OF_SHORT = "~~short:ProjectLeoDelimiter~~";

  @Autowired Database db;
  @Autowired OpenAiUtils openAiUtils;

  @PostMapping(value = "/api/protos/ProjectManagementService/GetEks")
  @ResponseBody
  public GetEksResponse getEks(
      @Authenticated UserX userX, @RequestBody Optional<GetEksRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(db, optionalRequest.orElse(GetEksRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetEksResponse.newBuilder();

              response.addAllEks(
                  Iterables.transform(
                      db.getKnowledgeAndSkillRepository()
                          .findAll(KNOWLEDGE_AND_SKILL_TYPE.EKS.name()),
                      DataAccess::getProtoEks));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetXqCompetencies")
  @ResponseBody
  public GetXqCompetenciesResponse getXqCompetencies(
      @Authenticated UserX userX, @RequestBody Optional<GetXqCompetenciesRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GetXqCompetenciesRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetXqCompetenciesResponse.newBuilder();

              response.addAllXqCompentencies(
                  Iterables.transform(
                      db.getKnowledgeAndSkillRepository()
                          .findAll(KNOWLEDGE_AND_SKILL_TYPE.XQ_COMPETENCY.name()),
                      DataAccess::orProtoXqCompetency));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GenerateProjects")
  @ResponseBody
  public GenerateProjectsResponse generateProjects(
      @Student UserX userX, @RequestBody Optional<GenerateProjectsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GenerateProjectsRequest.getDefaultInstance()))
        .retryNextStep(2, 1000)
        .andThen(
            (request, log) -> {
              var response = GenerateProjectsResponse.newBuilder();

              // Save the Project input settings.
              ProjectInput projectInput =
                  db.getProjectInputRepository()
                      .save(
                          new ProjectInput()
                              .setCreationTime(Instant.now())
                              .setStudent(userX.getStudent()));
              log.addProjectInput(projectInput);

              // Query OpenAI for projects.
              // TODO: Is there an asynchronous way to do this?
              OpenAiRequest aiRequest =
                  OpenAiRequest.newBuilder()
                      .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("system")
                              .setContent(
                                  String.format(
                                      "You are a senior student who wants to spend 60 hours to"
                                          + " build a project that demonstrates your mastery of"
                                          + " %s. You are passionate about %s and careers of %s."
                                          + " You want to improve your ability to %s.",
                                      COMMA_AND_JOINER.join(
                                          request.getEksList().stream()
                                              .map(Eks::getShortDescr)
                                              .map(ProjectManagementService::quoteAndEscape)
                                              .toList()),
                                      COMMA_AND_JOINER.join(
                                          request.getInterestsList().stream()
                                              .map(ProjectManagementService::quoteAndEscape)
                                              .toList()),
                                      COMMA_AND_JOINER.join(
                                          request.getCareerList().stream()
                                              .map(ProjectManagementService::quoteAndEscape)
                                              .toList()),
                                      COMMA_AND_JOINER.join(
                                          request.getXqCompentenciesList().stream()
                                              .map(XqCompetency::getShortDescr)
                                              .map(ProjectManagementService::quoteAndEscape)
                                              .toList()))))
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("user")
                              .setContent(
                                  String.format(
                                      // Notes:
                                      //
                                      // "then a declarative summary of the project in one
                                      // sentence" caused the short description to be skipped.
                                      //
                                      // Ending the block with the project delimiter caused it
                                      // to be included prematurely, before the full description
                                      // was finished generating.
                                      "Generate 5 projects that would fit the criteria. For each"
                                          + " project, return: 1) the text \"%s\", 2) then a"
                                          + " title, 3) then the text \"%s\", 4) then a short"
                                          + " declarative command statement that summarizes the"
                                          + " project, 5) then the text \"%s\", 6) then a detailed"
                                          + " description of the project followed by major steps"
                                          + " to complete it. Do not return any text before the"
                                          + " first project and do not format the output.",
                                      StringEscapeUtils.escapeJava(START_OF_PROJECT),
                                      StringEscapeUtils.escapeJava(END_OF_TITLE),
                                      StringEscapeUtils.escapeJava(END_OF_SHORT))))
                      .build();

              OpenAiResponse aiResponse =
                  openAiUtils.sendOpenAiRequest(aiRequest, OpenAiResponse.newBuilder()).build();

              List<Project> projects =
                  extractProjects(
                      log,
                      response,
                      projectInput,
                      aiResponse.getChoices(0).getMessage().getContent());
              db.getProjectRepository().saveAll(projects);
              projects.forEach(log::addProject);

              return response.build();
            })
        .finish();
  }

  private static String quoteAndEscape(String s) {
    return "\"" + StringEscapeUtils.escapeJava(s) + "\"";
  }

  static String normalizeAndCheckString(String input) {
    input = input.trim().replaceAll("\\n\\n", "\n");
    Matcher fluffMatcher = REMOVE_FLUFF.matcher(input);
    if (fluffMatcher.matches()) {
      for (String groupName : TEXT_GROUPS) {
        String groupText = fluffMatcher.group(groupName);
        if (groupText != null) {
          input = groupText;
          break;
        }
      }
    }
    return input.trim().replaceAll("\\n\\n", "\n");
  }

  static List<Project> extractProjects(
      LogOperations log,
      GenerateProjectsResponse.Builder response,
      ProjectInput projectInput,
      String aiResponse) {
    List<Project> projects = new ArrayList<>();
    for (String projectText : aiResponse.split(START_OF_PROJECT)) {
      try {
        if (normalizeAndCheckString(projectText).isEmpty()) {
          continue;
        }
        String[] pieces_of_information =
            projectText.trim().split(END_OF_TITLE + "|" + END_OF_SHORT);
        checkArgument(pieces_of_information.length == 3);

        projects.add(
            new Project()
                .setCreationTime(Instant.now())
                .setProjectInput(projectInput)
                .setName(normalizeAndCheckString(pieces_of_information[0]))
                .setShortDescr(normalizeAndCheckString(pieces_of_information[1]))
                .setLongDescr(normalizeAndCheckString(pieces_of_information[2])));
        response.addProjects(DataAccess.convertProjectToProto(projects.get(projects.size() - 1)));
      } catch (Throwable e) {

        log.setStatus(Status.ERROR);
        log.addNote("Could not parse project text because \"%s\": %s", e.getMessage(), projectText);
      }
    }
    return projects;
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjects")
  @ResponseBody
  public GetProjectsResponse getProjects(
      @Authenticated UserX userX, @RequestBody Optional<GetProjectsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(GetProjectsRequest.getDefaultInstance()),
            (request, log) -> {
              int userId = request.getUserXId();
              if (isAdmin(userX)) {
                // Do nothing.
              } else if (isTeacher(userX)) {
                // TODO: Verify the requested user is in their class.
              } else if (isStudent(userX)) {
                // Make sure the student is only querying about their own projects.
                checkArgument(userId == userX.getId(), "access denied");
              }

              UserX student = db.getUserXRepository().findById(userId).orElseThrow();
              var response = GetProjectsResponse.newBuilder();

              response.addAllProjects(
                  Streams.stream(
                          db.getProjectRepository()
                              .findAllByStudentId(student.getStudent().getId()))
                      .map(DataAccess::convertProjectToProto)
                      .toList());

              return response.build();
            })
        .finish();
  }
}
