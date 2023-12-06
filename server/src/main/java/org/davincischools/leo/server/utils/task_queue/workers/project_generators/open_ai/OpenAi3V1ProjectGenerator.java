package org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.theokanning.openai.completion.chat.ChatMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
import org.davincischools.leo.server.utils.TextUtils;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorInput;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V1ProjectGenerator {

  public record InitialChatMessage(
      ChatMessage chatMessage, Map<Integer, Integer> criteriaIdToProjectInputValueId) {}

  // This uses the project input value id as the criteria number if a mapping is not provided.
  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");
  private static final Joiner SENTENCE_JOINER = Joiner.on(". ");

  public static InitialChatMessage getInitialChatMessage(ProjectGeneratorInput projectInput) {
    AtomicInteger previousCriteriaNumber = new AtomicInteger(0);
    Map<Integer, Integer> criteriaToInputValue = new HashMap<>();

    List<String> requirements = new ArrayList<>();
    for (var categoryInputs : projectInput.getSortedProjectInputs()) {
      var categoryType = categoryInputs.getDefinitionCategory().getProjectDefinitionCategoryType();

      var completeRequirements = new StringBuilder();
      completeRequirements.append(categoryType.getQueryPrefix()).append(' ');
      switch (ValueType.valueOf(categoryType.getValueType())) {
        case FREE_TEXT -> {
          List<StringBuilder> allCriteria = new ArrayList<>();
          for (var value : categoryInputs.getInputValues()) {
            int criteriaNumber = previousCriteriaNumber.incrementAndGet();
            criteriaToInputValue.put(criteriaNumber, value.getId());
            allCriteria.add(
                new StringBuilder()
                    .append("Criteria ")
                    .append(criteriaNumber)
                    .append(") ")
                    .append(TextUtils.quoteAndEscape(value.getFreeTextValue())));
          }
          completeRequirements.append(COMMA_AND_JOINER.join(allCriteria));
        }
        case MOTIVATION -> {
          List<StringBuilder> allCriteria = new ArrayList<>();
          for (var value : categoryInputs.getInputValues()) {
            int criteriaNumber = previousCriteriaNumber.incrementAndGet();
            criteriaToInputValue.put(criteriaNumber, value.getId());
            allCriteria.add(
                new StringBuilder()
                    .append("Criteria ")
                    .append(criteriaNumber)
                    .append(") ")
                    .append(TextUtils.quoteAndEscape(value.getMotivationValue().getShortDescr())));
          }
          completeRequirements.append(COMMA_AND_JOINER.join(allCriteria));
        }
        default -> {
          List<StringBuilder> allCriteria = new ArrayList<>();
          for (var value : categoryInputs.getInputValues()) {
            int criteriaNumber = previousCriteriaNumber.incrementAndGet();
            criteriaToInputValue.put(criteriaNumber, value.getId());
            var ks = value.getKnowledgeAndSkillValue();
            var ksDescr =
                Stream.of(ks.getShortDescr(), ks.getName())
                    .filter(s -> !Strings.isNullOrEmpty(s))
                    .findFirst()
                    .orElse(null);
            if (ksDescr == null) {
              continue;
            }
            allCriteria.add(
                new StringBuilder()
                    .append("Criteria ")
                    .append(criteriaNumber)
                    .append(") ")
                    .append(TextUtils.quoteAndEscape(ksDescr)));
          }
          completeRequirements.append(COMMA_AND_JOINER.join(allCriteria));
        }
      }
      requirements.add(completeRequirements.toString());
    }

    return new InitialChatMessage(
        new ChatMessage(
            "system",
            "You are a senior student who wants to spend 60 hours to build a project. "
                + SENTENCE_JOINER.join(requirements)
                + "."),
        criteriaToInputValue);
  }
}
