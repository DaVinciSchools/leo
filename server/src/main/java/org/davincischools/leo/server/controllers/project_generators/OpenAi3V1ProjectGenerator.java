package org.davincischools.leo.server.controllers.project_generators;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;
import static org.davincischools.leo.database.utils.DaoUtils.sortByPosition;
import static org.davincischools.leo.database.utils.DaoUtils.streamIfInitialized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.theokanning.openai.completion.chat.ChatMessage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectInputFulfillment;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
import org.davincischools.leo.server.controllers.ProjectManagementService;
import org.davincischools.leo.server.controllers.ProjectManagementService.GenerateProjectsState;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator.AiProject.Milestone;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class OpenAi3V1ProjectGenerator {

  public static class AiProject {

    public static class Milestone {
      @JsonPropertyDescription(
          "A short declarative command statement that summarizes the milestone. This is the"
              + " milestone name and should not include any prefix such as 'Milestone 1'.")
      @JsonProperty(required = true)
      public String name;

      @JsonPropertyDescription(
          "Small steps, expressed as short declarative command statements, which are necessary to"
              + " complete the milestone. This is the step name and should not include any prefix"
              + " such as 'Step 1'.")
      @JsonProperty(required = true)
      public List<String> steps;
    }

    public static class CriteriaFulfillment {
      @JsonPropertyDescription("The criteria number from the description that this applies to.")
      @JsonProperty(required = true)
      public int criteriaNumber;

      @JsonPropertyDescription(
          "After completing the project, what specifically will the student have done to fulfill"
              + " this criteria.")
      @JsonProperty(required = true)
      public String howProjectFulfills;

      @JsonPropertyDescription(
          "From 0 to 100, how well does this project fulfill this criteria. If the project does"
              + " not fulfill this criteria return a 0.")
      @JsonProperty(required = true)
      public int fulfillmentPercentage;

      @JsonPropertyDescription(
          "What could a teacher be looking for while the project is being worked on in order to"
              + " assess whether the student is progress towards mastering that criteria.")
      @JsonProperty(required = true)
      public String assessmentApproach;
    }

    @JsonPropertyDescription("A short declarative command statement that summarizes the project.")
    @JsonProperty(required = true)
    public String name;

    @JsonPropertyDescription("A single sentence describing the project.")
    @JsonProperty(required = true)
    public String shortDescr;

    @JsonPropertyDescription(
        "Multiple paragraphs with a very detailed explanation of the entire project formatted as"
            + " HTML. This should include a description of the project, how it fulfills the"
            + " criteria, and what a student is expected to learn after completing this project.")
    @JsonProperty(required = true)
    public String longDescrHtml;

    @JsonPropertyDescription("Milestones to complete the project.")
    @JsonProperty(required = true)
    public List<Milestone> milestones;

    @JsonPropertyDescription(
        "How well project fulfills each criteria in the prompt. Each criteria in the prompt must"
            + " be included in this list, even if the project does not address it.")
    @JsonProperty(required = true)
    public List<CriteriaFulfillment> criteriaFulfillment;
  }

  public static class AiProjects {
    @JsonPropertyDescription("The resulting list of projects.")
    @JsonProperty(required = true)
    List<AiProject> projects;
  }

  // This uses the project input value id as the criteria number if a mapping is not provided.
  public static AiProject copyToAiProject(
      Project project, Optional<Map<Integer, Integer>> criteriaNumberToInputValueId) {
    var inputValueIdToCriteriaNumber =
        HashBiMap.create(criteriaNumberToInputValueId.orElse(new HashMap<>())).inverse();

    var aiProject = new AiProject();
    aiProject.name = project.getName();
    aiProject.shortDescr = project.getShortDescr();
    aiProject.longDescrHtml = project.getLongDescrHtml();

    aiProject.milestones = new ArrayList<>();
    sortByPosition(listIfInitialized(project.getProjectMilestones()))
        .forEach(
            m -> {
              var aiMilestone = new Milestone();
              aiMilestone.name = m.getName();
              aiMilestone.steps =
                  sortByPosition(listIfInitialized(m.getProjectMilestoneSteps())).stream()
                      .map(ProjectMilestoneStep::getName)
                      .toList();
              aiProject.milestones.add(aiMilestone);
            });

    aiProject.criteriaFulfillment = new ArrayList<>();
    streamIfInitialized(project.getProjectInputFulfillments())
        .forEach(
            i -> {
              var criteriaFulfillment = new AiProject.CriteriaFulfillment();
              criteriaFulfillment.criteriaNumber =
                  inputValueIdToCriteriaNumber.getOrDefault(
                      i.getProjectInputValue().getId(), i.getProjectInputValue().getId());
              criteriaFulfillment.howProjectFulfills = i.getHowProjectFulfills();
              criteriaFulfillment.fulfillmentPercentage = i.getFulfillmentPercentage();
              criteriaFulfillment.assessmentApproach = i.getVisibleIndicator();
              aiProject.criteriaFulfillment.add(criteriaFulfillment);
            });

    return aiProject;
  }

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");
  private static final Joiner SENTENCE_JOINER = Joiner.on(". ");

  public static record GetInitialChatMessageResponse(
      ChatMessage chatMessage, Map<Integer, Integer> criteriaToInputValue) {}

  public static GetInitialChatMessageResponse getInitialState(GenerateProjectsState state) {
    AtomicInteger previousCriteriaNumber = new AtomicInteger(0);
    Map<Integer, Integer> criteriaToInputValue = state.criteriaToInputValue();

    List<String> requirements = new ArrayList<>();
    int i = -1;
    for (var category :
        DaoUtils.sortByPosition(state.definition().getProjectDefinitionCategories())) {
      ++i;
      ProjectDefinitionCategoryType type = category.getProjectDefinitionCategoryType();
      ImmutableList<ProjectInputValue> values = state.values().get(i);

      StringBuilder completeRequirements = new StringBuilder();
      completeRequirements.append(type.getQueryPrefix()).append(' ');
      switch (ValueType.valueOf(type.getValueType())) {
        case FREE_TEXT -> {
          List<StringBuilder> allCriteria = new ArrayList<>();
          for (var value : values) {
            int criteriaNumber = previousCriteriaNumber.incrementAndGet();
            criteriaToInputValue.put(criteriaNumber, value.getId());
            allCriteria.add(
                new StringBuilder()
                    .append("Criteria ")
                    .append(criteriaNumber)
                    .append(") ")
                    .append(ProjectManagementService.quoteAndEscape(value.getFreeTextValue())));
          }
          completeRequirements.append(COMMA_AND_JOINER.join(allCriteria));
        }
        case MOTIVATION -> {
          List<StringBuilder> allCriteria = new ArrayList<>();
          for (var value : values) {
            int criteriaNumber = previousCriteriaNumber.incrementAndGet();
            criteriaToInputValue.put(criteriaNumber, value.getId());
            allCriteria.add(
                new StringBuilder()
                    .append("Criteria ")
                    .append(criteriaNumber)
                    .append(") ")
                    .append(
                        ProjectManagementService.quoteAndEscape(
                            value.getMotivationValue().getShortDescr())));
          }
          completeRequirements.append(COMMA_AND_JOINER.join(allCriteria));
        }
        default -> {
          List<StringBuilder> allCriteria = new ArrayList<>();
          for (var value : values) {
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
                    .append(ProjectManagementService.quoteAndEscape(ksDescr)));
          }
          completeRequirements.append(COMMA_AND_JOINER.join(allCriteria));
        }
      }
      requirements.add(completeRequirements.toString());
    }

    return new GetInitialChatMessageResponse(
        new ChatMessage(
            "system",
            "You are a senior student who wants to spend 60 hours to build a project. "
                + SENTENCE_JOINER.join(requirements)
                + "."),
        criteriaToInputValue);
  }

  @NotNull
  static List<@NotNull Project> saveAndGetProjects(
      Database db, GenerateProjectsState state, List<AiProject> aiProjects) {
    var projects =
        db.getProjectRepository()
            .saveAll(aiProjects.stream().map(p -> convertToProject(p, state)).toList());
    var milestones =
        db.getProjectMilestoneRepository()
            .saveAll(
                projects.stream()
                    .flatMap(p -> p.getProjectMilestones().stream())
                    .collect(toImmutableSet()));
    db.getProjectMilestoneStepRepository()
        .saveAll(
            milestones.stream()
                .flatMap(m -> m.getProjectMilestoneSteps().stream())
                .collect(toImmutableSet()));
    db.getProjectInputFulfillmentRepository()
        .saveAll(
            projects.stream()
                .map(Project::getProjectInputFulfillments)
                .flatMap(Set::stream)
                .toList());

    return projects;
  }

  @NotNull
  public static Project convertToProject(AiProject aiProject, GenerateProjectsState state) {
    AtomicInteger position = new AtomicInteger(0);
    var project =
        new Project()
            .setGenerator(OpenAi3V3ProjectGenerator.class.getName())
            .setCreationTime(Instant.now())
            .setProjectInput(state.input())
            .setName(aiProject.name)
            .setShortDescr(aiProject.shortDescr)
            .setLongDescrHtml(aiProject.longDescrHtml);
    project.setProjectMilestones(
        aiProject.milestones.stream()
            .map(
                m -> {
                  var milestone =
                      new ProjectMilestone()
                          .setCreationTime(Instant.now())
                          .setPosition((float) position.incrementAndGet())
                          .setName(m.name)
                          .setProject(project);
                  milestone.setProjectMilestoneSteps(
                      m.steps.stream()
                          .map(
                              s ->
                                  new ProjectMilestoneStep()
                                      .setCreationTime(Instant.now())
                                      .setPosition((float) position.incrementAndGet())
                                      .setName(s)
                                      .setProjectMilestone(milestone))
                          .collect(toImmutableSet()));
                  return milestone;
                })
            .collect(toImmutableSet()));
    var inputValuesById =
        Maps.uniqueIndex(
            state.values().stream().flatMap(List::stream).toList(), ProjectInputValue::getId);
    aiProject.criteriaFulfillment.forEach(
        c -> {
          var inputValueId = state.criteriaToInputValue().get(c.criteriaNumber);
          var inputValue = inputValuesById.get(inputValueId);
          if (inputValue == null) {
            return;
          }
          if (!DaoUtils.isInitialized(inputValue.getProjectInputFulfillments())) {
            inputValue.setProjectInputFulfillments(new LinkedHashSet<>());
          }
          inputValue
              .getProjectInputFulfillments()
              .add(
                  new ProjectInputFulfillment()
                      .setCreationTime(Instant.now())
                      .setProject(project)
                      .setProjectInputValue(inputValue)
                      .setHowProjectFulfills(c.howProjectFulfills)
                      .setFulfillmentPercentage(c.fulfillmentPercentage)
                      .setVisibleIndicator(c.assessmentApproach));
        });
    project.setProjectInputFulfillments(
        new LinkedHashSet<>(
            project.getProjectInput().getProjectInputValues().stream()
                .flatMap(v -> streamIfInitialized(v.getProjectInputFulfillments()))
                .collect(toImmutableSet())));
    return project;
  }
}
