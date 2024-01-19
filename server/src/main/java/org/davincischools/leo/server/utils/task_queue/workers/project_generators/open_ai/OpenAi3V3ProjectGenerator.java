package org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.davincischools.leo.server.utils.HtmlUtils.stripOutHtml;
import static org.davincischools.leo.server.utils.TextUtils.quoteAndEscape;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput.ExistingProjectUseType;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject.AiProjects;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGenerator;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorInput;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V1ProjectGenerator.InitialChatMessage;

public class OpenAi3V3ProjectGenerator implements ProjectGenerator {

  private static final Logger logger = LogManager.getLogger();

  // The parameters below should generally be set in the same order.
  private final OpenAiUtils openAiUtils;
  private int numberOfProjects;
  private ProjectGeneratorInput generatorInput;
  private InitialChatMessage initialChatMessage;
  private ChatFunction describeProjectFn;
  private FunctionExecutor functionExecutor;
  private final List<ChatMessage> messages = new ArrayList<>();

  // This class should be used one time and then discarded.
  public OpenAi3V3ProjectGenerator(OpenAiUtils openAiUtils) {
    this.openAiUtils = openAiUtils;
  }

  @Override
  public List<Project> generateProjects(ProjectGeneratorInput generatorInput, int numberOfProjects)
      throws JsonProcessingException {
    checkNotNull(generatorInput);
    checkState(this.generatorInput == null);

    this.numberOfProjects = numberOfProjects;
    this.generatorInput = generatorInput;

    initialChatMessage = OpenAi3V1ProjectGenerator.getInitialChatMessage(generatorInput);
    messages.add(initialChatMessage.chatMessage());

    describeProjectFn =
        ChatFunction.builder()
            .name("describe_projects")
            .description("Describe the projects that result from the query.")
            .executor(AiProjects.class, a -> a)
            .build();
    functionExecutor = new FunctionExecutor(List.of(describeProjectFn));

    if (generatorInput.getFillInProject() != null) {
      configureFulfillmentQuery();
    } else if (generatorInput.getExistingProject() != null) {
      switch (generatorInput.getExistingProjectUseType()) {
        case SUB_PROJECTS:
          configureSubProjectsQuery();
          break;
        case MORE_LIKE_THIS:
          configureMoreLikeThisQuery();
          break;
        case USE_CONFIGURATION:
          configureGenericQuery();
          break;
      }
    } else {
      configureGenericQuery();
    }

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .model(OpenAiUtils.CURRENT_GPT_MODEL)
            .messages(messages)
            .functions(functionExecutor.getFunctions())
            .functionCall(new ChatCompletionRequestFunctionCall(describeProjectFn.getName()))
            .build();

    var timeout = Duration.ofMinutes(20);
    var okHttpClient =
        OpenAiService.defaultClient(openAiUtils.getOpenAiKey().orElseThrow(), timeout)
            .newBuilder()
            .connectTimeout(timeout)
            .build();
    var retrofit =
        OpenAiService.defaultRetrofit(
            okHttpClient,
            OpenAiService.defaultObjectMapper()
                .setDefaultLeniency(true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    OpenAiService openAiService = new OpenAiService(retrofit.create(OpenAiApi.class));
    ChatCompletionResult chatCompletionResponse = null;
    try {
      logger.atDebug().log("Chat completion request: {}", chatCompletionRequest);
      chatCompletionResponse = openAiService.createChatCompletion(chatCompletionRequest);
      logger.atDebug().log("Chat completion response: {}", chatCompletionResponse);
      return chatCompletionResponse.getChoices().stream()
          .map(ChatCompletionChoice::getMessage)
          .map(ChatMessage::getFunctionCall)
          .map(functionExecutor::execute)
          .map(AiProjects.class::cast)
          .flatMap(p -> p.projects.stream())
          .map(p -> AiProject.aiProjectToProject(generatorInput, initialChatMessage, p))
          .toList();
    } catch (Exception e) {
      logger
          .atError()
          .withThrowable(e)
          .log("Failed to generate projects: {}", chatCompletionResponse);
      throw new RuntimeException("Failed to generate projects: " + chatCompletionResponse, e);
    } finally {
      try {
        openAiService.shutdownExecutor();
      } catch (NullPointerException e) {
        // An otherwise successful transaction throws an NPE for some reason.
      }
    }
  }

  private void configureGenericQuery() {
    checkState(generatorInput != null);
    checkState(initialChatMessage != null);

    checkState(generatorInput.getFillInProject() == null);
    checkState(
        generatorInput.getExistingProject() == null
            || generatorInput.getExistingProjectUseType()
                == ExistingProjectUseType.USE_CONFIGURATION);

    messages.add(
        new ChatMessage(
            ChatMessageRole.USER.value(),
            String.format(
                "Provide %s projects that would fulfill the system criteria.", numberOfProjects)));
  }

  private void configureFulfillmentQuery() throws JsonProcessingException {
    checkState(generatorInput != null);
    checkState(initialChatMessage != null);

    checkState(generatorInput.getFillInProject() != null);

    // Convert the existing project to JSON.
    String projectJson =
        new ObjectMapper()
            .writeValueAsString(AiProject.projectToAiProject(generatorInput.getFillInProject()));

    messages.add(
        new ChatMessage(
            ChatMessageRole.ASSISTANT.value(),
            "You have already created an existing project that meets the given criteria."
                + " But, some of its details are missing. The project is described in the"
                + " following JSON. All of the JSON fields represent the same information as"
                + " in the function output: "
                + projectJson));
  }

  private void configureSubProjectsQuery() throws JsonProcessingException {
    checkState(generatorInput != null);
    checkState(initialChatMessage != null);

    checkState(generatorInput.getFillInProject() == null);
    checkState(generatorInput.getExistingProject() != null);
    checkState(generatorInput.getExistingProjectUseType() == ExistingProjectUseType.SUB_PROJECTS);

    messages.add(
        new ChatMessage(
            ChatMessageRole.USER.value(),
            String.format(
                "You have already created an existing \"parent\" project that is to %s Create %s"
                    + " sub-projects that are related to the parent project and possibly help the"
                    + " student complete some part of the parent project. But that also meet the"
                    + " new system criteria.",
                summarizeExistingProject(generatorInput.getExistingProject()), numberOfProjects)));
  }

  private void configureMoreLikeThisQuery() throws JsonProcessingException {
    checkState(generatorInput != null);
    checkState(initialChatMessage != null);

    checkState(generatorInput.getFillInProject() == null);
    checkState(generatorInput.getExistingProject() != null);
    checkState(generatorInput.getExistingProjectUseType() == ExistingProjectUseType.MORE_LIKE_THIS);

    messages.add(
        new ChatMessage(
            ChatMessageRole.USER.value(),
            String.format(
                "You have already crated an \"existing\" project that is to %s Create %s projects"
                    + " that are strongly related to the existing project. But, that meet the new"
                    + " system criteria.",
                summarizeExistingProject(generatorInput.getExistingProject()), numberOfProjects)));
  }

  private String summarizeExistingProject(Project project) {
    StringBuilder sb =
        new StringBuilder() /*"The project that is to "*/
            .append(quoteAndEscape(stripOutHtml(project.getLongDescrHtml())))
            .append(". ");

    sb.append("Its milestones are ");
    int i = 0;
    for (var milestone : DaoUtils.listIfInitialized(project.getProjectMilestones())) {
      sb.append(i > 0 ? ", " : "")
          .append(++i)
          .append(") ")
          .append(quoteAndEscape(milestone.getName()));
    }
    sb.append(".");

    return sb.toString();
  }
}
