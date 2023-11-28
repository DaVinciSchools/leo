package org.davincischools.leo.server.controllers.project_generators;

import static org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator.saveAndGetProjects;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator.AiProjects;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
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

    var initialState = OpenAi3V1ProjectGenerator.getInitialState(state);
    state.criteriaToInputValue().putAll(initialState.criteriaToInputValue());

    var describeProjectFn =
        ChatFunction.builder()
            .name("describe_projects")
            .description("Describe projects that a student can do that meet the given criteria.")
            .executor(AiProjects.class, a -> a)
            .build();
    var functionExecutor = new FunctionExecutor(List.of(describeProjectFn));

    var messages = new ArrayList<ChatMessage>();
    messages.add(initialState.chatMessage());
    messages.add(
        new ChatMessage(
            ChatMessageRole.USER.value(),
            String.format(
                "Provide %s projects that would fit the system criteria.", numberOfProjects)));

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .model(OpenAiUtils.CURRENT_GPT_MODEL)
            .messages(messages)
            .functions(functionExecutor.getFunctions())
            .functionCall(new ChatCompletionRequestFunctionCall(describeProjectFn.getName()))
            .build();

    return httpExecutors
        .start(chatCompletionRequest)
        .andThen(
            (request, log) -> {
              log.addNote("OpenAI Request Messages:");
              request
                  .getMessages()
                  .forEach(m -> log.addNote("%s: %s", m.getRole(), m.getContent()));
              return request;
            })
        .retryNextStep(3, (int) Duration.ofMinutes(20).toMillis())
        .andThen(
            (request, log) -> {
              long startTime = System.currentTimeMillis();
              try {
                return openAiService.createChatCompletion(request).getChoices().stream()
                    .map(ChatCompletionChoice::getMessage)
                    .map(ChatMessage::getFunctionCall)
                    .map(functionExecutor::execute)
                    .map(AiProjects.class::cast)
                    .flatMap(p -> p.projects.stream())
                    .toList();
              } catch (Throwable e) {
                log.addNote(
                    "Failed to generate projects with OpenAI after "
                        + Duration.ofMillis(System.currentTimeMillis() - startTime).toSeconds()
                        + " seconds.");
                throw e;
              } finally {
                try {
                  openAiService.shutdownExecutor();
                } catch (NullPointerException npe) {
                  // This exception seems to always be thrown despite successful execution.
                  logger.atWarn().withThrowable(npe).log("Failed to shutdown OpenAI executor.");
                }
              }
            })
        .andThen((aiProjects, log) -> saveAndGetProjects(db, state, aiProjects))
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
}
