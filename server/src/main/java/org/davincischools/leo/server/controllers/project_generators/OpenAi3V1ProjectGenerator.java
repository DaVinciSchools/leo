package org.davincischools.leo.server.controllers.project_generators;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputCategory;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.LogRepository.Status;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
import org.davincischools.leo.protos.open_ai.OpenAiResponse.CreateCompletionChoice;
import org.davincischools.leo.server.controllers.ProjectManagementService;
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorLog;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V1ProjectGenerator implements ProjectGenerator {

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");

  private static final Pattern REMOVE_FLUFF =
      Pattern.compile(
          // Match newlines with the '.' character.
          "(?s)"
              // Match the start of the line.
              + "^"
              // A number, e.g., "1: " or "1. "
              + "([0-9]+[:.])?\\s*"
              // A label, e.g., "Title - " or "Full Description: "
              + "(([Pp]roject\\s*[0-9]*|[Tt]itle|[Ss]hort|[Ff]ull|[Ss]ummary)?\\s*"
              + "([Dd]escription)?)?\\s*"
              // The end of the label, e.g., the ":" or " - " from above.
              + "(\\.|:| -)?\\s*" //
              // A second number for, e.g., "Project: 1. "
              + "([0-9]+[:.])?\\s*" //
              // A title after the label, e.g., "Project 1 - Title: "
              + "(Title:)?\\s*" //
              // The main text, either quoted or not. Quotes are removed.
              + "([\"“”](?<quotedText>.*)[\"“”]|(?<unquotedText>.*))"
              // Match the end of the line.
              + "$");
  private static final ImmutableList<String> TEXT_GROUPS =
      ImmutableList.of("quotedText", "unquotedText");

  // Do NOT have any special Regex characters in these delimiters.
  static final String START_OF_PROJECT = "~~prj:ProjectLeoDelimiter~~";
  static final String END_OF_TITLE = "~~title:ProjectLeoDelimiter~~";
  static final String END_OF_SHORT = "~~short:ProjectLeoDelimiter~~";

  private static final Logger logger = LogManager.getLogger();

  private final Database db;
  private final OpenAiUtils openAiUtils;

  OpenAi3V1ProjectGenerator(Database db, OpenAiUtils openAiUtils) {
    this.db = db;
    this.openAiUtils = openAiUtils;
  }

  @Bean
  public static OpenAi3V1ProjectGenerator createOpenAi3V1ProjectGenerator(
      Database db, OpenAiUtils openAiUtils) {
    return new OpenAi3V1ProjectGenerator(db, openAiUtils);
  }

  // Initialize OpenAI query by adding the system role content.
  static OpenAiRequest.Builder getInitialAiRequest(
      GenerateProjectsState state, OpenAiRequest.Builder request) {
    StringBuilder sb = new StringBuilder();
    sb.append("You are a senior student who wants to spend 60 hours to build a project. ");
    for (int i = 0; i < state.definition().inputCategories().size(); ++i) {
      ProjectInputCategory category = state.definition().inputCategories().get(i);
      ImmutableList<ProjectInputValue> values = state.values().get(i);

      sb.append(category.getQueryPrefix()).append(' ');
      switch (ValueType.valueOf(category.getValueType())) {
        case FREE_TEXT -> sb.append(
            COMMA_AND_JOINER.join(
                values.stream()
                    .map(ProjectInputValue::getFreeTextValue)
                    .map(ProjectManagementService::quoteAndEscape)
                    .toList()));
        case EKS, XQ_COMPETENCY -> sb.append(
            COMMA_AND_JOINER.join(
                values.stream()
                    .map(ProjectInputValue::getKnowledgeAndSkillValue)
                    .map(KnowledgeAndSkill::getShortDescr)
                    .map(ProjectManagementService::quoteAndEscape)
                    .toList()));
        case MOTIVATION -> sb.append(
            COMMA_AND_JOINER.join(
                values.stream()
                    .map(ProjectInputValue::getMotivationValue)
                    .map(Motivation::getShortDescr)
                    .map(ProjectManagementService::quoteAndEscape)
                    .toList()));
      }
      sb.append(". ");
    }

    request
        .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
        .addMessagesBuilder()
        .setRole("system")
        .setContent(sb.toString());
    return request;
  }

  public List<Project> generateAndSaveProjects(
      GenerateProjectsState state, HttpExecutors httpExecutors, int numberOfProjects)
      throws HttpExecutorException {
    OpenAiRequest.Builder aiRequest = getInitialAiRequest(state, OpenAiRequest.newBuilder());

    aiRequest
        .addMessagesBuilder()
        .setRole("user")
        .setContent(
            String.format(
                // Notes:
                //
                // "then a declarative summary of the project in one
                // sentence" caused the short description to be skipped.
                //
                // Ending the block with the project delimiter caused it
                // to be included prematurely, before the full description
                // was finished generating.
                "Generate %s projects that would fit the criteria. For each"
                    + " project, return: 1) the text \"%s\", 2) then a"
                    + " title, 3) then the text \"%s\", 4) then a short"
                    + " declarative command statement that summarizes the"
                    + " project, 5) then the text \"%s\", 6) then a detailed"
                    + " description of the project followed by major steps"
                    + " to complete it. Do not return any text before the"
                    + " first project and do not format the output.",
                numberOfProjects,
                StringEscapeUtils.escapeJava(START_OF_PROJECT),
                StringEscapeUtils.escapeJava(END_OF_TITLE),
                StringEscapeUtils.escapeJava(END_OF_SHORT)));

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
                  .flatMap(content -> extractProjects(log, content, state.input()).stream())
                  .toList();
            })
        .onError(
            (error, log) -> {
              logger
                  .atError()
                  .withThrowable(error.throwables().get(0))
                  .log(
                      "Failed to generate projects in {} with OpenAI.",
                      OpenAi3V1ProjectGenerator.class.getSimpleName());
              return Optional.empty();
            })
        .finish();
  }

  static String normalizeAndCheckString(String input) {
    input = input.trim().replaceAll("\\n\\n", "\n");
    Matcher fluffMatcher = REMOVE_FLUFF.matcher(input);
    if (fluffMatcher.matches()) {
      for (String groupName : TEXT_GROUPS) {
        String groupText = fluffMatcher.group(groupName);
        if (groupText != null) {
          input = groupText;
          break;
        }
      }
    }
    return input.trim().replaceAll("\\n\\n", "\n");
  }

  private List<Project> extractProjects(
      HttpExecutorLog log, String responseContent, ProjectInput projectInput) {
    List<Project> projects = new ArrayList<>();
    for (String projectText : responseContent.split(START_OF_PROJECT)) {
      try {
        if (normalizeAndCheckString(projectText).isEmpty()) {
          continue;
        }
        String[] pieces_of_information =
            projectText.trim().split(END_OF_TITLE + "|" + END_OF_SHORT);
        checkArgument(pieces_of_information.length == 3);

        projects.add(
            db.getProjectRepository()
                .save(
                    new Project()
                        .setCreationTime(Instant.now())
                        .setProjectInput(projectInput)
                        .setGenerator(OpenAi3V1ProjectGenerator.class.getName())
                        .setName(normalizeAndCheckString(pieces_of_information[0]))
                        .setShortDescr(normalizeAndCheckString(pieces_of_information[1]))
                        .setLongDescrHtml(
                            escapeHtml4(normalizeAndCheckString(pieces_of_information[2])))));
      } catch (Throwable e) {
        log.setStatus(Status.ERROR);
        log.addNote("Could not parse project text because \"%s\": %s", e.getMessage(), projectText);
        logger
            .atError()
            .withThrowable(e)
            .log(
                "Failed to parse project in {} from OpenAI response: {}",
                OpenAi3V1ProjectGenerator.class.getSimpleName(),
                responseContent);
      }
    }

    return projects;
  }
}
