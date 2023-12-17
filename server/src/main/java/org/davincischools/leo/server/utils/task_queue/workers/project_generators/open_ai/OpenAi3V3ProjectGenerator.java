package org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject.AiProjects;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGenerator;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V3ProjectGenerator implements ProjectGenerator {

  private final OpenAiUtils openAiUtils;

  OpenAi3V3ProjectGenerator(@Autowired OpenAiUtils openAiUtils) {
    this.openAiUtils = openAiUtils;
  }

  public List<Project> generateProjects(ProjectGeneratorInput generatorInput, int numberOfProjects)
      throws JsonProcessingException {

    var initialChatMessage = OpenAi3V1ProjectGenerator.getInitialChatMessage(generatorInput);

    var describeProjectFn =
        generatorInput.getFillInProject() != null
            ? ChatFunction.builder()
                .name("describe_project")
                .description(
                    "Describe the project with the missing information that a student can"
                        + " do that meet the given criteria.")
                .executor(AiProject.class, a -> a)
                .build()
            : ChatFunction.builder()
                .name("describe_projects")
                .description(
                    "Describe projects that a student can do that meet the given criteria.")
                .executor(AiProjects.class, a -> a)
                .build();
    var functionExecutor = new FunctionExecutor(List.of(describeProjectFn));

    var messages = new ArrayList<ChatMessage>();
    messages.add(initialChatMessage.chatMessage());

    if (generatorInput.getFillInProject() != null) {
      // Convert the existing project to JSON.
      ObjectMapper jsonObjectMapper = new ObjectMapper();
      String projectJson =
          jsonObjectMapper.writeValueAsString(
              AiProject.projectToAiProject(generatorInput.getFillInProject()));

      messages.add(
          new ChatMessage(
              ChatMessageRole.ASSISTANT.value(),
              "You have already created a partial project description. This description's"
                  + " fields map to the function executor's output fields: "
                  + projectJson));
      messages.add(
          new ChatMessage(
              ChatMessageRole.USER.value(),
              "Fill in the missing project information for the partial project description."
                  + " Return the complete project description with all fields populated."));
    } else {
      messages.add(
          new ChatMessage(
              ChatMessageRole.USER.value(),
              String.format(
                  "Provide %s projects that would fit the system criteria.", numberOfProjects)));
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
    var retrofit = OpenAiService.defaultRetrofit(okHttpClient, OpenAiService.defaultObjectMapper());
    OpenAiService openAiService = new OpenAiService(retrofit.create(OpenAiApi.class));
    try {
      return openAiService.createChatCompletion(chatCompletionRequest).getChoices().stream()
          .map(ChatCompletionChoice::getMessage)
          .map(ChatMessage::getFunctionCall)
          .map(functionExecutor::execute)
          .map(AiProjects.class::cast)
          .flatMap(p -> p.projects.stream())
          .map(p -> AiProject.aiProjectToProject(generatorInput, initialChatMessage, p))
          .toList();
    } finally {
      try {
        openAiService.shutdownExecutor();
      } catch (NullPointerException e) {
        // An otherwise successful transaction throws an NPE.
      }
    }
  }
}
