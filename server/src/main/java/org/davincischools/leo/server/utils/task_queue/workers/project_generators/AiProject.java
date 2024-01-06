package org.davincischools.leo.server.utils.task_queue.workers.project_generators;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;
import static org.davincischools.leo.database.utils.DaoUtils.sortByPosition;
import static org.davincischools.leo.database.utils.DaoUtils.streamIfInitialized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.google.common.collect.Maps;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInputFulfillment;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V1ProjectGenerator.InitialChatMessage;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V3ProjectGenerator;
import org.jetbrains.annotations.NotNull;

public class AiProject {

  private static final Logger logger = LogManager.getLogger();

  public static class AiProjects {
    @JsonPropertyDescription("The resulting list of projects.")
    @JsonProperty(required = true)
    public List<AiProject> projects;
  }

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

  public static AiProject projectToAiProject(Project project) {
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
              criteriaFulfillment.criteriaNumber = i.getProjectInputValue().getId();
              criteriaFulfillment.howProjectFulfills = i.getHowProjectFulfills();
              criteriaFulfillment.fulfillmentPercentage = i.getFulfillmentPercentage();
              criteriaFulfillment.assessmentApproach = i.getVisibleIndicator();
              aiProject.criteriaFulfillment.add(criteriaFulfillment);
            });

    return aiProject;
  }

  @NotNull
  public static Project aiProjectToProject(
      ProjectGeneratorInput generatorInput,
      InitialChatMessage initialChatMessage,
      AiProject aiProject) {
    AtomicInteger position = new AtomicInteger(0);
    var project =
        new Project()
            .setGenerator(OpenAi3V3ProjectGenerator.class.getName())
            .setCreationTime(Instant.now())
            .setProjectInput(generatorInput.getProjectInput())
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

    var inputValues =
        generatorInput.getSortedProjectInputs().stream()
            .flatMap(p -> p.getInputValues().stream())
            .toList();
    inputValues.forEach(
        inputValue -> inputValue.setProjectInputFulfillments(new LinkedHashSet<>()));
    var inputValuesById = Maps.uniqueIndex(inputValues, ProjectInputValue::getId);
    aiProject.criteriaFulfillment.forEach(
        criteria -> {
          var inputValue = inputValuesById.get(criteria.criteriaNumber);
          if (inputValue == null) {
            logger
                .atError()
                .log(
                    "Unable to find input value with id {} for project input {}.",
                    criteria.criteriaNumber,
                    generatorInput.getProjectInput().getId());
            return;
          }

          var projectInputFulfillment =
              new ProjectInputFulfillment()
                  .setCreationTime(Instant.now())
                  .setProject(project)
                  .setProjectInputValue(inputValue)
                  .setHowProjectFulfills(criteria.howProjectFulfills)
                  .setFulfillmentPercentage(criteria.fulfillmentPercentage)
                  .setVisibleIndicator(criteria.assessmentApproach);

          project.getProjectInputFulfillments().add(projectInputFulfillment);

          inputValue.getProjectInputFulfillments().add(projectInputFulfillment);
        });
    return project;
  }
}
