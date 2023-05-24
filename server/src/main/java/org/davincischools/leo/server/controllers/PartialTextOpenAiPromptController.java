package org.davincischools.leo.server.controllers;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
import org.davincischools.leo.protos.open_ai.OpenAiResponse.CreateCompletionChoice;
import org.davincischools.leo.protos.partial_text_openai_prompt.GetSuggestionsRequest;
import org.davincischools.leo.protos.partial_text_openai_prompt.GetSuggestionsRequest.Prompt;
import org.davincischools.leo.protos.partial_text_openai_prompt.GetSuggestionsResponse;
import org.davincischools.leo.server.utils.HttpUserProvider.Authenticated;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/** Handles requests for the DynamicEditableList service in partial_text_openai_prompt.proto. */
@Controller
public class PartialTextOpenAiPromptController {

  private static final Pattern PROMPT_VARIABLE = Pattern.compile("\\{\\}");
  private static final String ENTRY_GROUP_NAME = "entry";
  private static final Pattern LIST_ENTRY_TITLE =
      Pattern.compile("(?m)^\\s*[0-9]+\\.\\s*(?<" + ENTRY_GROUP_NAME + ">[^\\r\\n]+)$");

  // The prompt should have "{}" in it. That will be replaced with the partial_text.
  ImmutableMap<Prompt, String> PROMPTS =
      new ImmutableMap.Builder<Prompt, String>()
          .put(
              Prompt.SUGGEST_THINGS_YOU_LOVE,
              "Give me a diverse list of 10 activity names related to {}. Do not include"
                  + " descriptions.")
          .put(
              Prompt.SUGGEST_THINGS_YOU_ARE_GOOD_AT,
              "Give me a diverse list of 10 abilities related to {}. Do not include"
                  + " descriptions.")
          .build();

  @Autowired OpenAiUtils openAiUtils;
  @Autowired Database db;

  // If the client sends an empty GetSuggestionsRequest then the body of the
  // request will have no bytes. Spring will then send either an optional
  // value that's empty, or a null value if the parameter is @Nullable.
  // So, handlers need to accept this type of input.
  @PostMapping(value = "/api/protos/PartialTextOpenAiPromptService/GetSuggestions")
  @ResponseBody
  public GetSuggestionsResponse getSuggestions(
      @Authenticated UserX userX, @RequestBody Optional<GetSuggestionsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(GetSuggestionsRequest.getDefaultInstance()),
            (request, log) -> {
              if (!PROMPTS.containsKey(request.getPrompt())) {
                throw new IllegalArgumentException("Invalid prompt: " + request.getPrompt());
              }

              OpenAiRequest aiRequest =
                  OpenAiRequest.newBuilder()
                      .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("user")
                              .setContent(
                                  PROMPT_VARIABLE
                                      .matcher(
                                          Objects.requireNonNull(PROMPTS.get(request.getPrompt())))
                                      .replaceAll(
                                          "\""
                                              + StringEscapeUtils.escapeJava(
                                                  request.getPartialText())
                                              + "\"")))
                      .build();

              OpenAiResponse aiResponse =
                  openAiUtils.sendOpenAiRequest(aiRequest, OpenAiResponse.newBuilder()).build();

              List<String> suggestions =
                  aiResponse.getChoicesList().stream()
                      .map(CreateCompletionChoice::getMessage)
                      .map(OpenAiMessage::getContent)
                      .flatMap(content -> parseContentIntoList(content).stream())
                      .distinct()
                      .sorted()
                      .toList();

              return GetSuggestionsResponse.newBuilder().addAllSuggestions(suggestions).build();
            })
        .finish();
  }

  public static List<String> parseContentIntoList(String content) {
    List<String> entries = new ArrayList<>();
    Matcher matcher = LIST_ENTRY_TITLE.matcher(content + '\n');
    while (matcher.find()) {
      String entry = matcher.group(ENTRY_GROUP_NAME);
      if (entry != null) {
        entries.add(entry);
      }
    }

    return entries;
  }
}
