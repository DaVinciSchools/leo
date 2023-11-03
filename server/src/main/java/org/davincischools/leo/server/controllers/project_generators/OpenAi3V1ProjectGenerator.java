package org.davincischools.leo.server.controllers.project_generators;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.theokanning.openai.completion.chat.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
import org.davincischools.leo.server.controllers.ProjectManagementService;
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V1ProjectGenerator {

  public static class AiProject {

    public static class Milestone {
      @JsonPropertyDescription(
          "A short declarative command statement that summarizes the milestone.")
      @JsonProperty(required = true)
      public String name;

      @JsonPropertyDescription(
          "Small steps, expressed as short declarative command statements, that are necessary to"
              + " complete the milestone.")
      @JsonProperty(required = true)
      public List<String> steps;
    }

    @JsonPropertyDescription("A short declarative command statement that summarizes the project.")
    @JsonProperty(required = true)
    public String name;

    @JsonPropertyDescription("A single sentence describing the project.")
    @JsonProperty(required = true)
    public String shortDescr;

    @JsonPropertyDescription(
        "Multiple paragraphs with a very detailed explanation of the entire project formatted as"
            + " HTML.")
    @JsonProperty(required = true)
    public String longDescrHtml;

    @JsonPropertyDescription("Milestones to complete the project.")
    @JsonProperty(required = true)
    public List<Milestone> milestones;
  }

  public static class AiProjects {
    @JsonPropertyDescription("The resulting list of projects.")
    @JsonProperty(required = true)
    List<AiProject> projects;
  }

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");
  private static final Joiner SENTENCE_JOINER = Joiner.on(". ");

  static ChatMessage getInitialChatMessage(GenerateProjectsState state) {
    List<String> requirements = new ArrayList<>();
    for (int i = 0; i < state.definition().categories().size(); ++i) {
      ProjectDefinitionCategory category = state.definition().categories().get(i);
      ProjectDefinitionCategoryType type = category.getProjectDefinitionCategoryType();
      ImmutableList<ProjectInputValue> values = state.values().get(i);

      StringBuilder sb = new StringBuilder();
      sb.append(i + 1).append(") ").append(type.getQueryPrefix()).append(' ');
      switch (ValueType.valueOf(type.getValueType())) {
        case FREE_TEXT -> sb.append(
            COMMA_AND_JOINER.join(
                values.stream()
                    .map(ProjectInputValue::getFreeTextValue)
                    .map(ProjectManagementService::quoteAndEscape)
                    .toList()));
        case MOTIVATION -> sb.append(
            COMMA_AND_JOINER.join(
                values.stream()
                    .map(ProjectInputValue::getMotivationValue)
                    .map(Motivation::getShortDescr)
                    .map(ProjectManagementService::quoteAndEscape)
                    .toList()));
        default -> sb.append(
            COMMA_AND_JOINER.join(
                values.stream()
                    .map(ProjectInputValue::getKnowledgeAndSkillValue)
                    .map(
                        ks ->
                            Stream.of(ks.getShortDescr(), ks.getName())
                                .filter(s -> !Strings.isNullOrEmpty(s))
                                .findFirst()
                                .orElse("Unknown"))
                    .map(ProjectManagementService::quoteAndEscape)
                    .toList()));
      }
      requirements.add(sb.toString());
    }

    return new ChatMessage(
        "system",
        "You are a senior student who wants to spend 60 hours to build a project. "
            + SENTENCE_JOINER.join(requirements)
            + ".");
  }
}
