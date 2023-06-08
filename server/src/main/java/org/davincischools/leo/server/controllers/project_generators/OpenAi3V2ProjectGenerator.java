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
import org.davincischools.leo.database.daos.ProjectCycle;
import org.davincischools.leo.database.daos.ProjectCycleStep;
import org.davincischools.leo.database.daos.ProjectInput;
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
                "Generate %s projects that would fit the system criteria. Return a JSON array of"
                    + " project entries. For each project entry, return 1) a \"title\" property"
                    + " containing a title, then 2) a \"name\" property containing a short"
                    + " declarative command statement that summarizes the project, then"
                    + " 3) a \"short_descr\" property that contains a sentence or two"
                    + " describing the project, then 4) a \"long_descr\" property"
                    + " containing a multiple paragraphs with a very detailed explanation"
                    + " of the entire project, then 5) a \"milestones\" array property that"
                    + " contains objects representing milestones with a \"name\" property"
                    + " and a \"steps\" array property. Do not include any text outside"
                    + " of the json object.",
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
      OpenAiProjectsWithSteps.Builder projectsWithSteps = OpenAiProjectsWithSteps.newBuilder();
      JsonFormat.parser()
          .ignoringUnknownFields()
          .merge("{ projects: " + responseContent + " }", projectsWithSteps);

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
                        .setLongDescr(projectWithSteps.getLongDescr()));
        for (Milestone milestone : projectWithSteps.getMilestonesList()) {
          ProjectCycle projectCycle =
              db.getProjectCycleRepository()
                  .save(
                      new ProjectCycle()
                          .setCreationTime(Instant.now())
                          .setPosition(position.incrementAndGet())
                          .setName(milestone.getName())
                          .setProject(project));
          for (String step : milestone.getStepsList()) {
            db.getProjectCycleStepRepository()
                .save(
                    new ProjectCycleStep()
                        .setCreationTime(Instant.now())
                        .setPosition(position.incrementAndGet())
                        .setName(step)
                        .setProjectCycle(projectCycle));
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
