package org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.open_ai;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
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
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.server.utils.HtmlUtils;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.AiComment;
import org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.AiCommentGenerator;
import org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.CommentGeneratorIo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiCommentGenerator implements AiCommentGenerator {

  private static final Logger logger = LogManager.getLogger();
  private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());
  private static final Joiner EOL_JOINER = Joiner.on("\n\n");
  private static final ObjectWriter OBJECT_WRITER =
      new ObjectMapper().writer().withDefaultPrettyPrinter();

  private final OpenAiUtils openAiUtils;

  private static String normalizeAndQuote(String text) {
    return "\"" + StringEscapeUtils.escapeJava(text.replaceAll("\\s+", " ").trim()) + "\"";
  }

  @Override
  public void generateComment(CommentGeneratorIo generatorIo) throws IOException {

    // Build system message.

    // Include project information.
    StringBuilder sys = new StringBuilder();
    sys.append(
            "You are a teacher who is replying to a student's new post about the project they"
                + " are working on."
                + " You will reply in single person narrative using \"I\" and \"me\"."
                + " Variables in this request and the function parameters are referenced in"
                + " brackets, e.g., [variable_name]."
                + " All text in quotes is escaped as a String in the Java language."
                + " The student's project is: ")
        .append(normalizeAndQuote(generatorIo.getProjectSummary()))
        .append(".\n\n");

    // Include goal information.
    sys.append(
        "There are a number of goals for the project that need to be completed."
            + " Each goal has a unique id and a description of what needs to be completed."
            + " No one knows the id of each goal, so they should be referred to by their"
            + " 'what_needs_to_be_completed' description."
            + " These goals are:\n");
    generatorIo
        .getGoals()
        .forEach(
            goal ->
                sys.append("Goal: id=")
                    .append(goal.getGoalIdNumber())
                    .append(", what_needs_to_be_completed=")
                    .append(
                        normalizeAndQuote(
                            goal.getHowTheProjectFulfillsThisGoal()
                                + " "
                                + goal.getWhatToLookForToShowCompletionOfThisGoal()))
                    .append("\n"));
    sys.append("\n");

    // Include post information.
    sys.append(
        "Information about the new post, as well as context information, is expressed in"
            + " variables."
            + " These variables are referenced in brackets, e.g., [variable_name] throughout this"
            + " request and in the function parameter descriptions."
            + " The name of each variable indicates what it contains."
            + " These variables are:\n");
    ImmutableMap.<String, String>builder()
        .put("new_post_content", generatorIo.getNewPostContent())
        .put("new_post_questions", generatorIo.getNewPostFeedbackRequest())
        .put("previous_posts_summaries", generatorIo.getPreviousPostsSummary())
        .put("previous_positive_feedback", generatorIo.getPreviousPositiveFeedback())
        .put(
            "previous_things_to_improve_feedback", generatorIo.getPreviousThingsToImproveFeedback())
        .put(
            "previous_what_things_have_improved_feedback",
            generatorIo.getPreviousHowImprovedFeedback())
        .put("previous_new_post_question_responses", generatorIo.getPreviousFeedbackResponses())
        .build()
        .forEach(
            (variable, value) ->
                sys.append("Variable: [")
                    .append(variable)
                    .append("]=")
                    .append(normalizeAndQuote(value))
                    .append("\n"));
    sys.append("\n");

    // Prepare OpenAI client.

    // Create the client.
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
    var openAiService = new OpenAiService(retrofit.create(OpenAiApi.class));

    // Initialize the function and request.
    var createCommentFn =
        ChatFunction.builder()
            .name("response_comment_and_metadata")
            .description(
                "This describes the comment and additional metadata about the post that should"
                    + " be returned."
                    + " All parameters in this function are required and must be filled."
                    + " Do not leave any parameters blank. Provide a value for all parameters.")
            .executor(
                org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators
                    .AiComment.class,
                a -> a)
            .build();
    var functionExecutor = new FunctionExecutor(List.of(createCommentFn));
    var chatCompletionRequest =
        ChatCompletionRequest.builder()
            .model(OpenAiUtils.CURRENT_GPT_MODEL)
            .messages(
                List.of(
                    new ChatMessage(ChatMessageRole.SYSTEM.value(), sys.toString()),
                    new ChatMessage(
                        ChatMessageRole.USER.value(),
                        "Respond by populating the response_comment_and_metadata_function"
                            + " parameters.")))
            .functions(functionExecutor.getFunctions())
            .functionCall(new ChatCompletionRequestFunctionCall(createCommentFn.getName()))
            .build();

    // Query AI and return comment.
    long count = counter.incrementAndGet();
    ChatCompletionResult chatCompletionResponse = null;
    AiComment aiComment = null;
    try {
      logger.atDebug().log("Chat completion request [[{}]]: {}", count, chatCompletionRequest);
      chatCompletionResponse = openAiService.createChatCompletion(chatCompletionRequest);
      logger.atDebug().log("Chat completion response: [[{}]] {}", count, chatCompletionResponse);
      aiComment =
          Iterables.getOnlyElement(
              chatCompletionResponse.getChoices().stream()
                  .map(ChatCompletionChoice::getMessage)
                  .map(ChatMessage::getFunctionCall)
                  .map(functionExecutor::execute)
                  .map(AiComment.class::cast)
                  .toList());

      if (!HtmlUtils.stripOutHtml(Strings.nullToEmpty(aiComment.getFeedbackSummary()))
          .trim()
          .isEmpty()) {
        return;
      }
      throw new IOException("AI comment was empty [[" + count + "]]");
    } finally {
      generatorIo.setAiPrompt(chatCompletionRequest.toString()).setAiComment(aiComment);
      if (chatCompletionResponse != null) {
        generatorIo.setAiResponse(
            EOL_JOINER
                .join(
                    chatCompletionResponse.toString(),
                    aiComment != null ? OBJECT_WRITER.writeValueAsString(aiComment) : "")
                .trim());
      }
      try {
        openAiService.shutdownExecutor();
      } catch (NullPointerException e) {
        // An otherwise successful transaction throws an NPE for some reason.
      }
    }
  }
}
