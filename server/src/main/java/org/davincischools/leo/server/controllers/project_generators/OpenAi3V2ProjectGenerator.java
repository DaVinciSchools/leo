package org.davincischools.leo.server.controllers.project_generators;

import com.fasterxml.jackson.datatype.jdk8.WrappedIOException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.LogRepository.Status;
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiProjectsWithSteps;
import org.davincischools.leo.protos.open_ai.OpenAiProjectsWithSteps.OpenAiProjectWithSteps;
import org.davincischools.leo.protos.open_ai.OpenAiProjectsWithSteps.OpenAiProjectWithSteps.Milestone;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
import org.davincischools.leo.protos.open_ai.OpenAiResponse.CreateCompletionChoice;
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorLog;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V2ProjectGenerator implements ProjectGenerator {

  private static final Logger logger = LogManager.getLogger();

  private final Database db;
  private final OpenAiUtils openAiUtils;

  OpenAi3V2ProjectGenerator(Database db, OpenAiUtils openAiUtils) {
    this.db = db;
    this.openAiUtils = openAiUtils;
  }

  @Bean
  public static OpenAi3V2ProjectGenerator createOpenAi3V2ProjectGenerator(
      Database db, OpenAiUtils openAiUtils) {
    return new OpenAi3V2ProjectGenerator(db, openAiUtils);
  }

  public List<Project> generateAndSaveProjects(
      GenerateProjectsState state, HttpExecutors httpExecutors, int numberOfProjects)
      throws HttpExecutorException {
    OpenAiRequest.Builder aiRequest =
        OpenAi3V1ProjectGenerator.getInitialAiRequest(state, OpenAiRequest.newBuilder());

    aiRequest
        .addMessagesBuilder()
        .setRole("user")
        .setContent(
            String.format(
                """
                    The response to the following must be constructed as a JSON object. Wrap
                    strings in double quotes. Double quotes within strings must be escaped with a
                    backslash. Single quotes within strings will not be escaped.

                    If there is an error at any point, return a JSON object with an single "error"
                    property that has the description of the error in plain text (be sure to escape
                    any double quotation marks in the JSON string value).

                    The request is to generate %s projects that would fit the system criteria. For
                    each project in the JSON "projects" array object, return:

                    1) a "name" property containing a short declarative command statement that
                    summarizes the project in plain text (be sure to escape any double quotation
                    marks in the JSON string value), then

                    2) a "short_descr" property that contains a sentence describing the project in
                    plain text (be sure to escape any double quotation marks in the JSON string
                    value), then

                    3) a "long_descr_html" property containing multiple paragraphs with a very
                    detailed explanation of the entire project formatted with HTML (be sure to
                    escape any double quotation marks in the JSON string value), then

                    4) a "milestones" array property that contains objects representing milestones.
                    Each milestone object will have:

                        a) a "name" property in plain text (be sure to escape any double quotation
                        marks in the JSON string value), and

                        b) a "steps" array property with a list of plain text strings (be sure to
                        escape any double quotation marks in the JSON string values, and be sure
                        to return an array of strings here, not just a single string).

                    Do not include any text other than the json object.
""",
                numberOfProjects));

    return httpExecutors
        .start(aiRequest)
        .retryNextStep(3, 1000)
        .andThen(
            (request, log) -> {
              OpenAiResponse aiResponse =
                  openAiUtils
                      .sendOpenAiRequest(
                          aiRequest.build(), OpenAiResponse.newBuilder(), httpExecutors)
                      .build();

              return aiResponse.getChoicesList().stream()
                  .map(CreateCompletionChoice::getMessage)
                  .map(OpenAiMessage::getContent)
                  .flatMap(
                      content -> {
                        try {
                          log.addNote(content);
                          return extractProjects(db, log, content, state.input()).stream();
                        } catch (IOException e) {
                          throw new WrappedIOException(e);
                        }
                      })
                  .toList();
            })
        .onError(
            (error, log) -> {
              logger
                  .atError()
                  .withThrowable(error.throwables().get(0))
                  .log(
                      "Failed to generate projects in {} with OpenAI.",
                      OpenAi3V2ProjectGenerator.class.getSimpleName());
              return Optional.empty();
            })
        .finish();
  }

  private static List<Project> extractProjects(
      Database db, HttpExecutorLog log, String responseContent, ProjectInput projectInput)
      throws InvalidProtocolBufferException {
    try {
      // We have to do a little pre-formatting of response content because OpenAI's API will
      // send back inconsistent results sometimes.

      // Remove a prefix that appears to be for formatting purposes for chate responses.
      if (responseContent.startsWith("```json")) {
        log.addNote("- Removed ```json prefix from response.");
      }
      String cleanedResponse = responseContent.replaceAll("^(?s:```json\\s*)", "");

      // Add the outside object declaration if it's missing. Sometimes, there's just an array by
      // itself, which is invalid.
      if (!cleanedResponse.startsWith("{")) {
        log.addNote("- Added JSON {} to response.");
        cleanedResponse = "{ projects: " + cleanedResponse.trim() + " }";
      }

      // Sometimes, last entries will have an incorrect comma at the end. Remove them.
      if (cleanedResponse.matches(".*(?s:([]}])\\s*,\\s*([]}])).*")) {
        log.addNote("- Removed trailing commas from response.");
        cleanedResponse = cleanedResponse.replaceAll("(?s:([]}])\\s*,\\s*([]}]))", "\\1\\2");
      }

      log.addNote("- Cleaned response:\n\n%s\n", cleanedResponse);

      OpenAiProjectsWithSteps.Builder projectsWithSteps = OpenAiProjectsWithSteps.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(cleanedResponse, projectsWithSteps);

      List<Project> projects = new ArrayList<>();
      AtomicInteger position = new AtomicInteger(0);

      for (OpenAiProjectWithSteps projectWithSteps : projectsWithSteps.getProjectsList()) {
        Project project =
            db.getProjectRepository()
                .save(
                    new Project()
                        .setCreationTime(Instant.now())
                        .setProjectInput(projectInput)
                        .setGenerator(OpenAi3V2ProjectGenerator.class.getName())
                        .setName(projectWithSteps.getName())
                        .setShortDescr(projectWithSteps.getShortDescr())
                        .setLongDescrHtml(projectWithSteps.getLongDescrHtml()));
        for (Milestone milestone : projectWithSteps.getMilestonesList()) {
          ProjectMilestone projectMilestone =
              db.getProjectMilestoneRepository()
                  .save(
                      new ProjectMilestone()
                          .setCreationTime(Instant.now())
                          .setPosition((float) position.incrementAndGet())
                          .setName(milestone.getName())
                          .setProject(project));
          for (String step : milestone.getStepsList()) {
            db.getProjectMilestoneStepRepository()
                .save(
                    new ProjectMilestoneStep()
                        .setCreationTime(Instant.now())
                        .setPosition((float) position.incrementAndGet())
                        .setName(step)
                        .setProjectMilestone(projectMilestone));
          }
        }

        projects.add(project);
      }

      return projects;
    } catch (InvalidProtocolBufferException e) {
      log.setStatus(Status.ERROR);
      log.addNote(
          "Failed to parse project in %s from OpenAI response: %s",
          OpenAi3V2ProjectGenerator.class.getSimpleName(), responseContent);
      logger
          .atError()
          .withThrowable(e)
          .log(
              "Failed to parse project in {} from OpenAI response: {}",
              OpenAi3V2ProjectGenerator.class.getSimpleName(),
              responseContent);
      throw e;
    }
  }
}
