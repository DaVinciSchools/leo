package org.davincischools.leo.server.utils.task_queue.workers;

import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;
import static org.davincischools.leo.database.utils.DaoUtils.sortByPosition;
import static org.davincischools.leo.database.utils.DaoUtils.streamIfInitialized;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.GetProjectsParams;
import org.davincischools.leo.protos.project_management.GenerateProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateProjectsResponse;
import org.davincischools.leo.protos.task_service.FillInMissingProjectInfoTask;
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator.AiProject;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator.GetInitialChatMessageResponse;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class FillInMissingProjectInfoWorker
    extends TaskQueue<FillInMissingProjectInfoTask, DefaultTaskMetadata> {

  private static final Logger logger = LogManager.getLogger();

  private final Database db;
  private final OpenAiUtils openAiUtils;

  public FillInMissingProjectInfoWorker(
      @Autowired Database db, @Autowired OpenAiUtils openAiUtils) {
    super(20);
    this.db = db;
    this.openAiUtils = openAiUtils;
  }

  @Override
  protected DefaultTaskMetadata createDefaultMetadata() {
    return new DefaultTaskMetadata().setRetries(2);
  }

  @Override
  protected void scanForTasks() {
    db.getProjectRepository()
        .getProjects(new GetProjectsParams().setIncludeFulfillments(true).setIncludeInactive(true))
        .forEach(
            p -> {
              if (listIfInitialized(p.getProjectInputFulfillments()).isEmpty()) {
                submitTask(
                    FillInMissingProjectInfoTask.newBuilder().setProjectId(p.getId()).build());
              }
            });
  }

  @Override
  protected void processTask(FillInMissingProjectInfoTask task, DefaultTaskMetadata metadata)
      throws IOException {
    for (var project :
        db.getProjectRepository()
            .getProjects(
                new GetProjectsParams()
                    .setProjectIds(List.of(task.getProjectId()))
                    .setIncludeInputs(
                        new GetProjectInputsParams()
                            .setIncludeProcessing(true)
                            .setIncludeComplete(true))
                    .setIncludeMilestones(true)
                    .setIncludeFulfillments(true)
                    .setIncludeInactive(true))) {
      if (!listIfInitialized(project.getProjectInputFulfillments()).isEmpty()) {
        continue;
      }

      // Recreate the state that was used to generate the project.
      var state =
          new GenerateProjectsState(
              GenerateProjectsRequest.getDefaultInstance(),
              project.getProjectInput().getProjectDefinition(),
              project.getProjectInput(),
              new ArrayList<>(),
              new HashMap<>(),
              GenerateProjectsResponse.newBuilder());
      var projectInputValuesByCategoryId =
          Multimaps.index(
              sortByPosition(project.getProjectInput().getProjectInputValues()),
              p -> p.getProjectDefinitionCategory().getId());
      for (ProjectDefinitionCategory category :
          sortByPosition(
              project.getProjectInput().getProjectDefinition().getProjectDefinitionCategories())) {
        state
            .values()
            .add(ImmutableList.copyOf(projectInputValuesByCategoryId.get(category.getId())));
      }

      // Recreate the initial chat message that generated the project.
      GetInitialChatMessageResponse projectRequestDescription =
          OpenAi3V1ProjectGenerator.getInitialState(state);

      // Create OpenAI client connection.
      var timeout = Duration.ofMinutes(20);
      var okHttpClient =
          OpenAiService.defaultClient(openAiUtils.getOpenAiKey().orElseThrow(), timeout)
              .newBuilder()
              .connectTimeout(timeout)
              .build();
      var retrofit =
          OpenAiService.defaultRetrofit(okHttpClient, OpenAiService.defaultObjectMapper());

      // Convert the existing project to JSON.
      ObjectMapper jsonObjectMapper = new ObjectMapper();
      String projectJson =
          jsonObjectMapper.writeValueAsString(
              OpenAi3V1ProjectGenerator.copyToAiProject(
                  project, Optional.of(state.criteriaToInputValue())));

      // Add a message with the existing project.
      var messages = new ArrayList<ChatMessage>();
      messages.add(projectRequestDescription.chatMessage());
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
                  + " And return the complete project description with all fields populated."));

      // Create the function to get the result.
      var describeProjectFn =
          ChatFunction.builder()
              .name("describe_project")
              .description(
                  "Describe the project with the missing information that a student can"
                      + " do that meet the given criteria.")
              .executor(AiProject.class, a -> a)
              .build();
      var functionExecutor = new FunctionExecutor(List.of(describeProjectFn));

      // Send the request to OpenAI.
      AiProject completedAiProject;
      ChatCompletionRequest chatCompletionRequest =
          ChatCompletionRequest.builder()
              .model(OpenAiUtils.GPT_3_5_TURBO_16K_MODEL)
              .messages(messages)
              .functions(functionExecutor.getFunctions())
              .functionCall(new ChatCompletionRequestFunctionCall(describeProjectFn.getName()))
              .build();

      OpenAiService openAiService = new OpenAiService(retrofit.create(OpenAiApi.class));
      try {
        completedAiProject =
            Iterables.getOnlyElement(
                openAiService.createChatCompletion(chatCompletionRequest).getChoices().stream()
                    .map(ChatCompletionChoice::getMessage)
                    .map(ChatMessage::getFunctionCall)
                    .map(functionExecutor::execute)
                    .map(AiProject.class::cast)
                    .toList());
      } finally {
        try {
          openAiService.shutdownExecutor();
        } catch (NullPointerException ignored) {
          // Shutdown always results in an NPE.
        }
      }

      // Parse response and augment the existing project.
      var convertedProject = OpenAi3V1ProjectGenerator.convertToProject(completedAiProject, state);
      db.getProjectInputFulfillmentRepository()
          .saveAll(
              streamIfInitialized(convertedProject.getProjectInputFulfillments())
                  .map(f -> f.setProject(project))
                  .toList());
    }
  }
}
