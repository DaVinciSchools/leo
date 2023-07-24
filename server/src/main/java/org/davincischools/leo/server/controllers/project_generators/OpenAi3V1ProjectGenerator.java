package org.davincischools.leo.server.controllers.project_generators;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.server.controllers.ProjectManagementService;
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V1ProjectGenerator {
  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");

  // Initialize OpenAI query by adding the system role content.
  static OpenAiRequest.Builder getInitialAiRequest(
      GenerateProjectsState state, OpenAiRequest.Builder request) {
    StringBuilder sb = new StringBuilder();
    sb.append("You are a senior student who wants to spend 60 hours to build a project. ");
    for (int i = 0; i < state.definition().categories().size(); ++i) {
      ProjectDefinitionCategory category = state.definition().categories().get(i);
      ProjectDefinitionCategoryType type = category.getProjectDefinitionCategoryType();
      ImmutableList<ProjectInputValue> values = state.values().get(i);

      sb.append(type.getQueryPrefix()).append(' ');
      switch (ValueType.valueOf(type.getValueType())) {
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
}
