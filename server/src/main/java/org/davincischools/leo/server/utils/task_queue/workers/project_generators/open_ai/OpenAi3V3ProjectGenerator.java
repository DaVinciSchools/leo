package org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.davincischools.leo.server.utils.HtmlUtils.stripOutHtml;
import static org.davincischools.leo.server.utils.TextUtils.quoteAndEscape;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
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
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput.ExistingProjectUseType;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject.AiProjects;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGenerator;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorIo;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V1ProjectGenerator.InitialChatMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAi3V3ProjectGenerator implements ProjectGenerator {

  private static final Logger logger = LogManager.getLogger();
  private static final Joiner EOL_JOINER = Joiner.on("\n\n");
  private static final ObjectWriter OBJECT_WRITER =
      new ObjectMapper().writer().withDefaultPrettyPrinter();

  private final OpenAiUtils openAiUtils;

  @Override
  public void generateProjects(ProjectGeneratorIo generatorIo) throws JsonProcessingException {
    checkNotNull(generatorIo);

    List<ChatMessage> messages = new ArrayList<>();

    InitialChatMessage initialChatMessage =
        OpenAi3V1ProjectGenerator.getInitialChatMessage(generatorIo);
    messages.add(initialChatMessage.chatMessage());

    ChatFunction describeProjectFn =
        ChatFunction.builder()
            .name("describe_projects")
            .description("Describe the projects that result from the query.")
            .executor(AiProjects.class, a -> a)
            .build();
    FunctionExecutor functionExecutor = new FunctionExecutor(List.of(describeProjectFn));

    if (generatorIo.getFillInProject() != null) {
      messages.add(createFulfillmentQueryMessage(generatorIo));
    } else if (generatorIo.getExistingProject() != null) {
      switch (generatorIo.getExistingProjectUseType()) {
        case SUB_PROJECTS:
          messages.add(createSubProjectsQueryMessage(generatorIo));
          break;
        case MORE_LIKE_THIS:
          messages.add(createMoreLikeThisQueryMessage(generatorIo));
          break;
        case USE_CONFIGURATION:
          messages.add(createGenericQueryMessage(generatorIo));
          break;
      }
    } else {
      messages.add(createGenericQueryMessage(generatorIo));
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
    AiProjects aiProjects = null;
    try {
      logger.atDebug().log("Chat completion request: {}", chatCompletionRequest);
      chatCompletionResponse = openAiService.createChatCompletion(chatCompletionRequest);
      logger.atDebug().log("Chat completion response: {}", chatCompletionResponse);
      aiProjects =
          Iterables.getOnlyElement(
              chatCompletionResponse.getChoices().stream()
                  .map(ChatCompletionChoice::getMessage)
                  .map(ChatMessage::getFunctionCall)
                  .map(functionExecutor::execute)
                  .map(AiProjects.class::cast)
                  .toList());
    } finally {
      generatorIo.setAiPrompt(chatCompletionRequest.toString()).setAiProjects(aiProjects);
      if (chatCompletionResponse != null) {
        generatorIo.setAiResponse(
            EOL_JOINER
                .join(
                    chatCompletionResponse.toString(),
                    aiProjects != null ? OBJECT_WRITER.writeValueAsString(aiProjects) : "")
                .trim());
      }
      try {
        openAiService.shutdownExecutor();
      } catch (NullPointerException e) {
        // An otherwise successful transaction throws an NPE for some reason.
      }
    }
  }

  private ChatMessage createGenericQueryMessage(ProjectGeneratorIo generatorIo) {
    checkState(generatorIo != null);

    checkState(generatorIo.getFillInProject() == null);
    checkState(
        generatorIo.getExistingProject() == null
            || generatorIo.getExistingProjectUseType() == ExistingProjectUseType.USE_CONFIGURATION);

    return new ChatMessage(
        ChatMessageRole.USER.value(),
        String.format(
            "Provide %s projects that would fulfill the system criteria.",
            generatorIo.getNumberOfProjects()));
  }

  private ChatMessage createFulfillmentQueryMessage(ProjectGeneratorIo generatorIo)
      throws JsonProcessingException {
    checkState(generatorIo != null);

    checkState(generatorIo.getFillInProject() != null);

    // Convert the existing project to JSON.
    String projectJson =
        new ObjectMapper()
            .writeValueAsString(AiProject.projectToAiProject(generatorIo.getFillInProject()));

    return new ChatMessage(
        ChatMessageRole.ASSISTANT.value(),
        "You have already created an existing project that meets the given criteria."
            + " But, some of its details are missing. The project is described in the"
            + " following JSON. All of the JSON fields represent the same information as"
            + " in the function output: "
            + projectJson);
  }

  private ChatMessage createSubProjectsQueryMessage(ProjectGeneratorIo generatorIo)
      throws JsonProcessingException {
    checkState(generatorIo != null);

    checkState(generatorIo.getFillInProject() == null);
    checkState(generatorIo.getExistingProject() != null);
    checkState(generatorIo.getExistingProjectUseType() == ExistingProjectUseType.SUB_PROJECTS);

    return new ChatMessage(
        ChatMessageRole.USER.value(),
        String.format(
            "You have already created an existing \"parent\" project that is to %s Create %s"
                + " sub-projects that are related to the parent project and possibly help the"
                + " student complete some part of the parent project. But that also meet the"
                + " new system criteria.",
            summarizeExistingProject(generatorIo.getExistingProject()),
            generatorIo.getNumberOfProjects()));
  }

  private ChatMessage createMoreLikeThisQueryMessage(ProjectGeneratorIo generatorIo)
      throws JsonProcessingException {
    checkState(generatorIo != null);

    checkState(generatorIo.getFillInProject() == null);
    checkState(generatorIo.getExistingProject() != null);
    checkState(generatorIo.getExistingProjectUseType() == ExistingProjectUseType.MORE_LIKE_THIS);

    return new ChatMessage(
        ChatMessageRole.USER.value(),
        String.format(
            "You have already crated an \"existing\" project that is to %s Create %s projects"
                + " that are strongly related to the existing project. But, that meet the new"
                + " system criteria.",
            summarizeExistingProject(generatorIo.getExistingProject()),
            generatorIo.getNumberOfProjects()));
  }

  private String summarizeExistingProject(Project project) {
    StringBuilder sb =
        new StringBuilder()
            .append(" The existing project is the following: ")
            .append(quoteAndEscape(stripOutHtml(project.getLongDescrHtml())))
            .append(". ");

    sb.append("Its milestones are: ");
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
