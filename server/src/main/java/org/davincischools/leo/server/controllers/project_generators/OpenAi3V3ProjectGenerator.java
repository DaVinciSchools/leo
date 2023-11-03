package org.davincischools.leo.server.controllers.project_generators;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
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
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator.AiProject;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator.AiProjects;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V3ProjectGenerator implements ProjectGenerator {

  private static final Logger logger = LogManager.getLogger();

  private final Database db;
  private final OpenAiUtils openAiUtils;

  OpenAi3V3ProjectGenerator(Database db, OpenAiUtils openAiUtils) {
    this.db = db;
    this.openAiUtils = openAiUtils;
  }

  @Bean
  public static OpenAi3V3ProjectGenerator createOpenAi3V2ProjectGenerator(
      Database db, OpenAiUtils openAiUtils) {
    return new OpenAi3V3ProjectGenerator(db, openAiUtils);
  }

  public List<Project> generateAndSaveProjects(
      GenerateProjectsState state, HttpExecutors httpExecutors, int numberOfProjects)
      throws HttpExecutorException {

    var timeout = Duration.ofMinutes(20);
    var okHttpClient =
        OpenAiService.defaultClient(openAiUtils.getOpenAiKey().orElseThrow(), timeout)
            .newBuilder()
            .connectTimeout(timeout)
            .build();
    var retrofit = OpenAiService.defaultRetrofit(okHttpClient, OpenAiService.defaultObjectMapper());
    OpenAiService openAiService = new OpenAiService(retrofit.create(OpenAiApi.class));

    var describeProjectFn =
        ChatFunction.builder()
            .name("describe_projects")
            .description("Describe projects that a student can do that meets the given criteria.")
            .executor(AiProjects.class, a -> a)
            .build();
    var functionExecutor = new FunctionExecutor(List.of(describeProjectFn));

    var messages = new ArrayList<ChatMessage>();
    messages.add(OpenAi3V1ProjectGenerator.getInitialChatMessage(state));
    messages.add(
        new ChatMessage(
            ChatMessageRole.USER.value(),
            String.format(
                "Provide %s projects that would fit the system criteria.", numberOfProjects)));

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .model(OpenAiUtils.GPT_3_5_TURBO_MODEL)
            .messages(messages)
            .functions(functionExecutor.getFunctions())
            .functionCall(new ChatCompletionRequestFunctionCall(describeProjectFn.getName()))
            .build();

    return httpExecutors
        .start(chatCompletionRequest)
        .retryNextStep(3, 1000)
        .andThen(
            (request, log) -> {
              try {
                return openAiService.createChatCompletion(request).getChoices().stream()
                    .map(ChatCompletionChoice::getMessage)
                    .map(ChatMessage::getFunctionCall)
                    .map(functionExecutor::execute)
                    .map(AiProjects.class::cast)
                    .flatMap(p -> p.projects.stream())
                    .toList();
              } finally {
                try {
                  openAiService.shutdownExecutor();
                } catch (Throwable t) {
                  logger.atWarn().withThrowable(t).log("Failed to shutdown OpenAI executor.");
                }
              }
            })
        .andThen(
            (aiProjects, log) -> {
              var projects =
                  db.getProjectRepository()
                      .saveAll(
                          aiProjects.stream()
                              .map(p -> convertToProject(p, state.input()))
                              .toList());
              var milestones =
                  db.getProjectMilestoneRepository()
                      .saveAll(
                          projects.stream()
                              .flatMap(p -> p.getProjectMilestones().stream())
                              .collect(toImmutableSet()));
              db.getProjectMilestoneStepRepository()
                  .saveAll(
                      milestones.stream()
                          .flatMap(m -> m.getProjectMilestoneSteps().stream())
                          .collect(toImmutableSet()));
              return projects;
            })
        .onError(
            (error, log) -> {
              logger
                  .atError()
                  .withThrowable(error.throwables().get(0))
                  .log(
                      "Failed to generate projects in {} with OpenAI.",
                      OpenAi3V3ProjectGenerator.class.getSimpleName());
              return Optional.empty();
            })
        .finish();
  }

  @NotNull
  private static Project convertToProject(AiProject aiProject, ProjectInput projectInput) {
    AtomicInteger position = new AtomicInteger(0);
    var project =
        new Project()
            .setGenerator(OpenAi3V3ProjectGenerator.class.getName())
            .setCreationTime(Instant.now())
            .setProjectInput(projectInput)
            .setName(aiProject.name)
            .setShortDescr(aiProject.shortDescr)
            .setLongDescrHtml(aiProject.longDescrHtml);
    project.setProjectMilestones(
        aiProject.milestones.stream()
            .map(
                m -> {
                  var milestone =
                      new ProjectMilestone()
                          .setCreationTime(Instant.now())
                          .setPosition((float) position.incrementAndGet())
                          .setName(m.name)
                          .setProject(project);
                  milestone.setProjectMilestoneSteps(
                      m.steps.stream()
                          .map(
                              s ->
                                  new ProjectMilestoneStep()
                                      .setCreationTime(Instant.now())
                                      .setPosition((float) position.incrementAndGet())
                                      .setName(s)
                                      .setProjectMilestone(milestone))
                          .collect(toImmutableSet()));
                  return milestone;
                })
            .collect(toImmutableSet()));
    return project;
  }
}
