package org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.theokanning.openai.completion.chat.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.davincischools.leo.server.utils.TextUtils;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorInput;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V1ProjectGenerator {

  public record InitialChatMessage(ChatMessage chatMessage) {}

  // This uses the project input value id as the criteria number if a mapping is not provided.
  private static final Joiner GOAL_CRITERIA_JOINER = Joiner.on("\n * ");
  private static final Joiner GOAL_JOINER = Joiner.on(".\n\n");

  public static InitialChatMessage getInitialChatMessage(ProjectGeneratorInput projectInput) {
    List<String> goals = new ArrayList<>();
    for (var categoryInputs : projectInput.getSortedProjectInputs()) {
      var categoryType = categoryInputs.getDefinitionCategory().getProjectDefinitionCategoryType();

      List<StringBuilder> goal = new ArrayList<>();
      goal.add(
          new StringBuilder().append("Goal: ").append(categoryType.getQueryPrefix()).append(":"));
      switch (categoryType.getValueType()) {
        case FREE_TEXT -> {
          for (var value : categoryInputs.getInputValues()) {
            goal.add(
                new StringBuilder()
                    .append("Criteria: id=")
                    .append(value.getId())
                    .append(" requirement=")
                    .append(TextUtils.quoteAndEscape(value.getFreeTextValue())));
          }
        }
        case MOTIVATION -> {
          List<StringBuilder> allCriteria = new ArrayList<>();
          for (var value : categoryInputs.getInputValues()) {
            allCriteria.add(
                new StringBuilder()
                    .append("Criteria: id=")
                    .append(value.getId())
                    .append(" requirement=")
                    .append(TextUtils.quoteAndEscape(value.getMotivationValue().getShortDescr())));
          }
        }
        default -> {
          for (var value : categoryInputs.getInputValues()) {
            var ks = value.getKnowledgeAndSkillValue();
            var ksDescr =
                Stream.of(ks.getShortDescr(), ks.getName())
                    .filter(s -> !Strings.isNullOrEmpty(s))
                    .findFirst()
                    .orElse(null);
            if (ksDescr == null) {
              continue;
            }
            goal.add(
                new StringBuilder()
                    .append("Criteria: id=")
                    .append(value.getId())
                    .append(" requirement=")
                    .append(TextUtils.quoteAndEscape(ksDescr)));
          }
        }
      }
      goals.add(GOAL_CRITERIA_JOINER.join(goal));
    }

    return new InitialChatMessage(
        new ChatMessage(
            "system",
            "You are a senior student who wants to spend 60 hours to build a project. "
                + GOAL_JOINER.join(goals)
                + "."));
  }
}
