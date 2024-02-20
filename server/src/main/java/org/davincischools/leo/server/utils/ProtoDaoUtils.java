package org.davincischools.leo.server.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.davincischools.leo.database.utils.DaoUtils.createJoinTableRows;
import static org.davincischools.leo.database.utils.DaoUtils.getDaoClass;
import static org.davincischools.leo.database.utils.DaoUtils.ifInitialized;
import static org.davincischools.leo.database.utils.DaoUtils.isInitialized;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Streams;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkill.Type;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectInputFulfillment;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.ProjectPostRating;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.AssignmentKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.ClassXKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository.FullProjectPostComment;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.davincischools.leo.protos.pl_types.ClassX.Builder;
import org.davincischools.leo.protos.pl_types.ProjectDefinition;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory.Option;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory.ValueType;
import org.davincischools.leo.protos.pl_types.ProjectInputCategoryOrBuilder;
import org.davincischools.leo.protos.user_x_management.FullUserXDetails;
import org.davincischools.leo.protos.user_x_management.RegisterUserXRequest;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;

public class ProtoDaoUtils {

  private static final Logger logger = LogManager.getLogger();
  private static final AtomicInteger positionCounter = new AtomicInteger(0);

  @SuppressWarnings("unchecked")
  public static <T> T valueOrNull(MessageOrBuilder message, int fieldNumber) {
    FieldDescriptor descriptor = message.getDescriptorForType().findFieldByNumber(fieldNumber);
    if (message.hasField(descriptor)) {
      return (T) message.getField(descriptor);
    }
    return null;
  }

  public static <T> T valueOrNull(MessageOrBuilder message, int fieldNumber, boolean condition) {
    return condition ? valueOrNull(message, fieldNumber) : null;
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> listOrNull(MessageOrBuilder message, int fieldNumber) {
    FieldDescriptor descriptor = message.getDescriptorForType().findFieldByNumber(fieldNumber);
    if (descriptor.isRepeated()) {
      if (message.getRepeatedFieldCount(descriptor) > 0) {
        return (List<T>) message.getField(descriptor);
      }
    } else {
      if (message.hasField(descriptor)) {
        return List.of((T) message.getField(descriptor));
      }
    }
    return null;
  }

  public static <F extends Enum<?>, T extends Enum<?>> T enumNameOrNull(
      F enumValue, Class<T> enumClass) {
    return enumValue != null && enumValue.ordinal() != 0
        ? Arrays.stream(enumClass.getEnumConstants())
            .filter(e -> e.name().equals(enumValue.name()))
            .findFirst()
            .orElseThrow()
        : null;
  }

  private record ProtoDaoFields(
      Class<? extends MessageOrBuilder> messageOrBuilderClass,
      Class<?> daoClass,
      Set<Integer> ignoredFieldNumbers) {}

  private static final Map<
          ProtoDaoFields,
          Map<Integer, BiConsumer</* message= */ MessageOrBuilder, /* dao= */ Object>>>
      protoToDaoSetters = Collections.synchronizedMap(new HashMap<>());

  private static final Map<
          ProtoDaoFields,
          Map<Integer, BiConsumer</* dao= */ Object, /* message= */ Message.Builder>>>
      daoToProtoSetters = Collections.synchronizedMap(new HashMap<>());

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder>
      toProjectDefinitionProto(
          @Nullable org.davincischools.leo.database.daos.ProjectDefinition projectDefinition,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder> newBuilder) {
    return translateToProto(
        projectDefinition,
        newBuilder,
        builder -> {
          AtomicInteger index = new AtomicInteger(0);
          ifInitialized(
              projectDefinition.getProjectDefinitionCategories(),
              Comparator.comparing(ProjectDefinitionCategory::getPosition),
              projectDefinitionCategory -> {
                int actualIndex = index.getAndIncrement();
                if (actualIndex < builder.getInputsCount()) {
                  toProjectInputCategoryProto(
                      projectDefinitionCategory,
                      () -> builder.getInputsBuilder(actualIndex).getCategoryBuilder());
                } else {
                  toProjectInputCategoryProto(
                      projectDefinitionCategory,
                      () -> builder.addInputsBuilder().getCategoryBuilder());
                }
              });
        },
        org.davincischools.leo.protos.pl_types.ProjectDefinition.AI_PROMPT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.AI_RESPONSE_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUT_ID_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.SELECTED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.STATE_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.ASSIGNMENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.EXISTING_PROJECT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition
            .EXISTING_PROJECT_USE_TYPE_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder>
      toProjectDefinitionProto(
          org.davincischools.leo.database.daos.ProjectInput projectInput,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder> newBuilder) {
    return translateToProto(
        projectInput,
        newBuilder,
        builder -> {
          builder.setInputId(projectInput.getId());
          toProjectDefinitionProto(projectInput.getProjectDefinition(), () -> builder);
          toAssignmentProto(projectInput.getAssignment(), false, builder::getAssignmentBuilder);
          toProjectProto(
              projectInput.getExistingProject(), true, builder::getExistingProjectBuilder);

          Map<Integer, org.davincischools.leo.protos.pl_types.ProjectInputValue.Builder>
              categoryIdToProjectInputValue = new HashMap<>();
          builder
              .getInputsBuilderList()
              .forEach(
                  input -> categoryIdToProjectInputValue.put(input.getCategory().getId(), input));

          ifInitialized(
              projectInput.getProjectInputValues(),
              Comparator.comparing(ProjectInputValue::getPosition),
              projectInputValue -> {
                var projectInputValueProto =
                    Optional.ofNullable(
                            categoryIdToProjectInputValue.get(
                                projectInputValue.getProjectDefinitionCategory().getId()))
                        .orElseThrow(
                            () ->
                                new IllegalArgumentException(
                                    "No category found for project_input_value "
                                        + projectInputValue.getId()
                                        + "."));

                switch (projectInputValueProto.getCategory().getValueType()) {
                  case FREE_TEXT ->
                      projectInputValueProto.addFreeTexts(projectInputValue.getFreeTextValue());
                  case MOTIVATION ->
                      projectInputValueProto.addSelectedIds(
                          projectInputValue.getMotivationValue().getId());
                  default ->
                      projectInputValueProto.addSelectedIds(
                          projectInputValue.getKnowledgeAndSkillValue().getId());
                }
              });
        },
        org.davincischools.leo.protos.pl_types.ProjectDefinition.ID_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.NAME_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUT_ID_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.TEMPLATE_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.SELECTED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.ASSIGNMENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.EXISTING_PROJECT_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.database.daos.ProjectDefinition>
      toProjectDefinitionDao(
          org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder projectDefinition) {
    return translateToDao(
        projectDefinition,
        () ->
            new org.davincischools.leo.database.daos.ProjectDefinition()
                .setCreationTime(Instant.now()),
        dao -> {
          dao.setProjectDefinitionCategories(
              new LinkedHashSet<>(
                  projectDefinition.getInputsList().stream()
                      .map(org.davincischools.leo.protos.pl_types.ProjectInputValue::getCategory)
                      .map(ProtoDaoUtils::toProjectDefinitionCategoryDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList()));
          dao.getProjectDefinitionCategories()
              .forEach(category -> category.setProjectDefinition(dao));
        },
        org.davincischools.leo.protos.pl_types.ProjectDefinition.AI_PROMPT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.AI_RESPONSE_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUT_ID_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.SELECTED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.STATE_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.ASSIGNMENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.EXISTING_PROJECT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition
            .EXISTING_PROJECT_USE_TYPE_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.database.daos.ProjectInput> toProjectInputDao(
      org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder projectDefinition) {
    return translateToDao(
        projectDefinition,
        () ->
            new org.davincischools.leo.database.daos.ProjectInput().setCreationTime(Instant.now()),
        dao -> {
          dao.setId(valueOrNull(projectDefinition, ProjectDefinition.INPUT_ID_FIELD_NUMBER));
          toProjectDefinitionDao(projectDefinition).ifPresent(dao::setProjectDefinition);
          toAssignmentDao(projectDefinition.getAssignment()).ifPresent(dao::setAssignment);
          toProjectDao(projectDefinition.getExistingProject()).ifPresent(dao::setExistingProject);

          Map<Integer, ProjectDefinitionCategory> idToProjectDefinitionCategory =
              Maps.uniqueIndex(
                  dao.getProjectDefinition().getProjectDefinitionCategories(),
                  ProjectDefinitionCategory::getId);

          List<ProjectInputValue> projectInputValues = new ArrayList<>();
          projectDefinition
              .getInputsList()
              .forEach(
                  input -> {
                    Supplier<ProjectInputValue> newProjectInputValue =
                        () ->
                            new ProjectInputValue()
                                .setCreationTime(Instant.now())
                                .setProjectInput(dao)
                                .setPosition((float) positionCounter.incrementAndGet())
                                .setProjectDefinitionCategory(
                                    checkNotNull(
                                        idToProjectDefinitionCategory.get(
                                            input.getCategory().getId())));

                    switch (input.getCategory().getValueType()) {
                      case FREE_TEXT ->
                          projectInputValues.addAll(
                              input.getFreeTextsList().stream()
                                  .map(
                                      freeText ->
                                          newProjectInputValue.get().setFreeTextValue(freeText))
                                  .toList());
                      case MOTIVATION ->
                          projectInputValues.addAll(
                              input.getSelectedIdsList().stream()
                                  .map(
                                      motivationId ->
                                          newProjectInputValue
                                              .get()
                                              .setMotivationValue(
                                                  new Motivation().setId(motivationId)))
                                  .toList());
                      default ->
                          projectInputValues.addAll(
                              input.getSelectedIdsList().stream()
                                  .map(
                                      selectedId ->
                                          newProjectInputValue
                                              .get()
                                              .setKnowledgeAndSkillValue(
                                                  new KnowledgeAndSkill().setId(selectedId)))
                                  .toList());
                    }
                  });
          dao.setProjectInputValues(new LinkedHashSet<>(projectInputValues));
        },
        org.davincischools.leo.protos.pl_types.ProjectDefinition.ID_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.NAME_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUT_ID_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.TEMPLATE_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.SELECTED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.ASSIGNMENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.EXISTING_PROJECT_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectInputCategory.Builder>
      toProjectInputCategoryProto(
          ProjectDefinitionCategory projectDefinitionCategory,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectInputCategory.Builder>
              newBuilder) {
    return translateToProto(
        projectDefinitionCategory,
        newBuilder,
        builder ->
            ifInitialized(projectDefinitionCategory.getProjectDefinitionCategoryType())
                .ifPresent(
                    type ->
                        translateToProto(
                            type,
                            () -> builder,
                            builder2 -> {
                              Optional.ofNullable(type.getInputPlaceholder())
                                  .ifPresent(builder::setPlaceholder);
                              Optional.ofNullable(type.getId()).ifPresent(builder::setTypeId);
                            },
                            ProjectInputCategory.ID_FIELD_NUMBER,
                            ProjectInputCategory.TYPE_ID_FIELD_NUMBER,
                            ProjectInputCategory.PLACEHOLDER_FIELD_NUMBER,
                            ProjectInputCategory.MAX_NUM_VALUES_FIELD_NUMBER,
                            ProjectInputCategory.OPTIONS_FIELD_NUMBER)),
        ProjectInputCategory.TYPE_ID_FIELD_NUMBER,
        ProjectInputCategory.NAME_FIELD_NUMBER,
        ProjectInputCategory.SHORT_DESCR_FIELD_NUMBER,
        ProjectInputCategory.INPUT_DESCR_FIELD_NUMBER,
        ProjectInputCategory.HINT_FIELD_NUMBER,
        ProjectInputCategory.PLACEHOLDER_FIELD_NUMBER,
        ProjectInputCategory.VALUE_TYPE_FIELD_NUMBER,
        ProjectInputCategory.OPTIONS_FIELD_NUMBER);
  }

  public static Optional<Option.Builder> toOptionProto(
      KnowledgeAndSkill knowledgeAndSkill, Supplier<Option.Builder> newBuilder) {
    return translateToProto(
        knowledgeAndSkill,
        newBuilder,
        builder -> {
          toUserXProto(knowledgeAndSkill.getUserX(), builder::getUserXBuilder);
        },
        Option.USER_X_FIELD_NUMBER);
  }

  public static Optional<Option.Builder> toOptionProto(
      Motivation motivation, Supplier<Option.Builder> newBuilder) {
    return translateToProto(
        motivation,
        newBuilder,
        builder -> {},
        Option.USER_X_FIELD_NUMBER,
        Option.CATEGORY_FIELD_NUMBER);
  }

  public static void addProjectInputCategoryOptions(
      Database db, Iterable<ProjectInputCategory.Builder> projectInputCategories) {
    var options = new HashMap<ValueType, List<Option>>();
    for (var projectInputCategory : projectInputCategories) {
      switch (projectInputCategory.getValueType()) {
        case MOTIVATION ->
            projectInputCategory.addAllOptions(
                options.computeIfAbsent(
                    projectInputCategory.getValueType(),
                    valueType ->
                        db.getMotivationRepository().findAll().stream()
                            .filter(knowledgeAndSkill -> knowledgeAndSkill.getDeleted() == null)
                            .map(motivation -> toOptionProto(motivation, Option::newBuilder))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Option.Builder::build)
                            .toList()));
        case FREE_TEXT -> {}
        default ->
            projectInputCategory.addAllOptions(
                options.computeIfAbsent(
                    projectInputCategory.getValueType(),
                    valueType ->
                        db
                            .getKnowledgeAndSkillRepository()
                            .findAll(Type.valueOf(valueType.name()))
                            .stream()
                            .filter(knowledgeAndSkill -> knowledgeAndSkill.getDeleted() == null)
                            .map(
                                knowledgeAndSkill ->
                                    toOptionProto(knowledgeAndSkill, Option::newBuilder))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Option.Builder::build)
                            .toList()));
      }
    }
  }

  private static <T> void populateOptions(
      Iterable<T> values,
      Function<T, Option.Builder> toOption,
      ProjectInputCategory.Builder inputCategory) {
    Streams.stream(values)
        .map(toOption)
        .sorted(Comparator.comparing(Option.Builder::getName))
        .forEach(inputCategory::addOptions);
  }

  public static Optional<ProjectDefinitionCategory> toProjectDefinitionCategoryDao(
      ProjectInputCategoryOrBuilder projectInputCategory) {
    return translateToDao(
        projectInputCategory,
        () ->
            new ProjectDefinitionCategory()
                .setCreationTime(Instant.now())
                .setPosition((float) positionCounter.incrementAndGet())
                .setProjectDefinitionCategoryType(
                    new ProjectDefinitionCategoryType().setCreationTime(Instant.now())),
        dao ->
            translateToDao(
                projectInputCategory,
                dao::getProjectDefinitionCategoryType,
                typeDao -> {
                  typeDao.setId(
                      valueOrNull(projectInputCategory, ProjectInputCategory.TYPE_ID_FIELD_NUMBER));
                  typeDao.setInputPlaceholder(
                      valueOrNull(
                          projectInputCategory, ProjectInputCategory.PLACEHOLDER_FIELD_NUMBER));
                },
                ProjectInputCategory.ID_FIELD_NUMBER,
                ProjectInputCategory.TYPE_ID_FIELD_NUMBER,
                ProjectInputCategory.PLACEHOLDER_FIELD_NUMBER,
                ProjectInputCategory.MAX_NUM_VALUES_FIELD_NUMBER,
                ProjectInputCategory.OPTIONS_FIELD_NUMBER),
        ProjectInputCategory.TYPE_ID_FIELD_NUMBER,
        ProjectInputCategory.NAME_FIELD_NUMBER,
        ProjectInputCategory.SHORT_DESCR_FIELD_NUMBER,
        ProjectInputCategory.INPUT_DESCR_FIELD_NUMBER,
        ProjectInputCategory.HINT_FIELD_NUMBER,
        ProjectInputCategory.PLACEHOLDER_FIELD_NUMBER,
        ProjectInputCategory.VALUE_TYPE_FIELD_NUMBER,
        ProjectInputCategory.OPTIONS_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Milestone.Step.Builder>
      toMilestoneStepProto(
          ProjectMilestoneStep projectMilestoneStep,
          Supplier<org.davincischools.leo.protos.pl_types.Project.Milestone.Step.Builder>
              newBuilder) {
    return translateToProto(projectMilestoneStep, newBuilder, builder -> {});
  }

  public static Optional<ProjectMilestoneStep> toProjectMilestoneStepDao(
      org.davincischools.leo.protos.pl_types.Project.Milestone.StepOrBuilder step) {
    return translateToDao(
        step,
        () ->
            new ProjectMilestoneStep()
                .setCreationTime(Instant.now())
                .setPosition((float) positionCounter.incrementAndGet()),
        dao -> {});
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Milestone.Builder>
      toMilestoneProto(
          ProjectMilestone projectMilestone,
          Supplier<org.davincischools.leo.protos.pl_types.Project.Milestone.Builder> newBuilder) {
    return translateToProto(
        projectMilestone,
        newBuilder,
        builder ->
            ifInitialized(
                projectMilestone.getProjectMilestoneSteps(),
                Comparator.comparing(ProjectMilestoneStep::getPosition),
                step -> toMilestoneStepProto(step, builder::addStepsBuilder)),
        org.davincischools.leo.protos.pl_types.Project.Milestone.STEPS_FIELD_NUMBER);
  }

  public static Optional<ProjectMilestone> toProjectMilestoneDao(
      org.davincischools.leo.protos.pl_types.Project.MilestoneOrBuilder milestone) {
    return translateToDao(
        milestone,
        () ->
            new ProjectMilestone()
                .setCreationTime(Instant.now())
                .setPosition((float) positionCounter.incrementAndGet()),
        dao ->
            dao.setProjectMilestoneSteps(
                new LinkedHashSet<>(
                    milestone.getStepsList().stream()
                        .map(ProtoDaoUtils::toProjectMilestoneStepDao)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList())),
        org.davincischools.leo.protos.pl_types.Project.Milestone.STEPS_FIELD_NUMBER);
  }

  public static Optional<Project> toProjectDao(
      org.davincischools.leo.protos.pl_types.ProjectOrBuilder project) {
    return translateToDao(
        project,
        () -> new Project().setCreationTime(Instant.now()),
        dao -> {
          toAssignmentDao(project.getAssignment()).ifPresent(dao::setAssignment);
          dao.setProjectMilestones(
              new LinkedHashSet<>(
                  project.getMilestonesList().stream()
                      .map(ProtoDaoUtils::toProjectMilestoneDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList()));
          toProjectInputDao(project.getProjectDefinition()).ifPresent(dao::setProjectInput);
        },
        org.davincischools.leo.protos.pl_types.Project.ASSIGNMENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Project.MILESTONES_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Project.PROJECT_DEFINITION_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Builder> toProjectProto(
      Project project,
      boolean includeChildren,
      Supplier<org.davincischools.leo.protos.pl_types.Project.Builder> newBuilder) {
    return translateToProto(
        project,
        newBuilder,
        builder -> {
          if (includeChildren) {
            toAssignmentProto(project.getAssignment(), true, builder::getAssignmentBuilder);
          }
          ifInitialized(
              project.getProjectMilestones(),
              Comparator.comparing(ProjectMilestone::getPosition),
              milestone -> toMilestoneProto(milestone, builder::addMilestonesBuilder));
          ifInitialized(project.getProjectInput())
              .ifPresent(
                  projectInput ->
                      toProjectDefinitionProto(projectInput, builder::getProjectDefinitionBuilder));
        },
        org.davincischools.leo.protos.pl_types.Project.ASSIGNMENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Project.MILESTONES_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Project.PROJECT_DEFINITION_FIELD_NUMBER);
  }

  public static Optional<ProjectPost> toProjectPostDao(
      org.davincischools.leo.protos.pl_types.ProjectPostOrBuilder projectPost) {
    return translateToDao(
        projectPost,
        () -> new ProjectPost().setCreationTime(Instant.now()),
        dao -> {
          dao.setUserX(toUserXDao(projectPost.getUserX()).orElse(null));
          dao.setProject(toProjectDao(projectPost.getProject()).orElse(null));
          dao.setTags(
              new LinkedHashSet<>(
                  projectPost.getTagsList().stream()
                      .map(ProtoDaoUtils::toTagDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList()));
          dao.setProjectPostComments(
              new LinkedHashSet<>(
                  projectPost.getCommentsList().stream()
                      .map(ProtoDaoUtils::toProjectPostCommentDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList()));
          if (projectPost.getRatingCategoriesCount() > 0) {
            if (!isInitialized(dao.getProject().getProjectInputFulfillments())) {
              dao.getProject().setProjectInputFulfillments(new LinkedHashSet<>());
            }

            projectPost
                .getRatingCategoriesList()
                .forEach(
                    ratingCategory -> {
                      var inputFulfillmentId = ratingCategory.getProjectInputFulfillmentId();
                      var valueType = ratingCategory.getValueType();
                      var projectInputValue = new ProjectInputValue();
                      switch (valueType) {
                        case MOTIVATION ->
                            projectInputValue.setMotivationValue(
                                new Motivation().setName(ratingCategory.getValue()));
                        case FREE_TEXT ->
                            projectInputValue.setFreeTextValue(ratingCategory.getValue());
                        default ->
                            projectInputValue.setKnowledgeAndSkillValue(
                                new KnowledgeAndSkill().setName(ratingCategory.getValue()));
                      }
                      projectInputValue.setProjectDefinitionCategory(
                          new ProjectDefinitionCategory()
                              .setProjectDefinitionCategoryType(
                                  new ProjectDefinitionCategoryType()
                                      .setName(ratingCategory.getCategory())
                                      .setValueType(
                                          ProjectDefinitionCategoryType.ValueType.valueOf(
                                              valueType.name()))));
                      var projectInputFulfillment =
                          new ProjectInputFulfillment()
                              .setId(inputFulfillmentId)
                              .setProjectInputValue(projectInputValue);
                      dao.getProject().getProjectInputFulfillments().add(projectInputFulfillment);
                    });

            var ratings =
                new LinkedHashSet<>(
                    projectPost.getRatingsList().stream()
                        .map(ProtoDaoUtils::toProjectPostRatingDao)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList());
            var idToProjectInputFulfillments =
                new HashMap<>(
                    Maps.uniqueIndex(
                        dao.getProject().getProjectInputFulfillments(),
                        ProjectInputFulfillment::getId));
            ratings.forEach(
                rating ->
                    rating.setProjectInputFulfillment(
                        idToProjectInputFulfillments.get(
                            rating.getProjectInputFulfillment().getId())));
            dao.setProjectPostRatings(ratings);
          }
        },
        org.davincischools.leo.protos.pl_types.ProjectPost.USER_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.TAGS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.COMMENTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.PROJECT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.RATING_CATEGORIES_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.RATINGS_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectPost.Builder>
      toProjectPostProto(
          ProjectPost projectPost,
          boolean includeChildren,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectPost.Builder> newBuilder) {
    return translateToProto(
        projectPost,
        newBuilder,
        b -> {
          toUserXProto(projectPost.getUserX(), b::getUserXBuilder);
          toProjectProto(projectPost.getProject(), true, b::getProjectBuilder);
          ifInitialized(projectPost.getTags(), tag -> toTagProto(tag, b::addTagsBuilder));
          if (includeChildren) {
            ifInitialized(
                projectPost.getProjectPostComments(),
                comment -> toProjectPostCommentProto(comment, b::addCommentsBuilder));
            ifInitialized(
                projectPost.getProjectPostRatings(),
                rating -> toProjectPostRatingProto(rating, b::addRatingsBuilder));
            ifInitialized(projectPost.getProject())
                .ifPresent(
                    project -> {
                      ifInitialized(
                          project.getProjectInputFulfillments(),
                          inputFulfillment -> {
                            ifInitialized(inputFulfillment.getProjectInputValue())
                                .ifPresent(
                                    inputValue -> {
                                      var valueType =
                                          ValueType.valueOf(
                                              inputValue
                                                  .getProjectDefinitionCategory()
                                                  .getProjectDefinitionCategoryType()
                                                  .getValueType()
                                                  .name());
                                      var rating =
                                          b.addRatingCategoriesBuilder()
                                              .setProjectInputFulfillmentId(
                                                  inputFulfillment.getId())
                                              .setCategory(
                                                  inputValue
                                                      .getProjectDefinitionCategory()
                                                      .getProjectDefinitionCategoryType()
                                                      .getName())
                                              .setValueType(valueType);
                                      switch (valueType) {
                                        case MOTIVATION -> {
                                          ifInitialized(inputValue.getMotivationValue())
                                              .ifPresent(
                                                  motivationValue ->
                                                      rating.setValue(motivationValue.getName()));
                                        }
                                        case FREE_TEXT ->
                                            rating.setValue(inputValue.getFreeTextValue());
                                        default -> {
                                          ifInitialized(inputValue.getKnowledgeAndSkillValue())
                                              .ifPresent(
                                                  knowledgeAndSkillValue ->
                                                      rating.setValue(
                                                          knowledgeAndSkillValue.getName()));
                                        }
                                      }
                                    });
                          });
                    });
          }
        },
        org.davincischools.leo.protos.pl_types.ProjectPost.USER_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.PROJECT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.TAGS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.COMMENTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.RATING_CATEGORIES_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.RATINGS_FIELD_NUMBER);
  }

  public static Optional<ProjectPostComment> toProjectPostCommentDao(
      org.davincischools.leo.protos.pl_types.ProjectPostCommentOrBuilder projectPostComment) {
    return translateToDao(
        projectPostComment,
        () -> new ProjectPostComment().setCreationTime(Instant.now()),
        dao -> {
          toUserXDao(projectPostComment.getUserX()).ifPresent(dao::setUserX);
          toProjectPostDao(projectPostComment.getProjectPost()).ifPresent(dao::setProjectPost);
        },
        org.davincischools.leo.protos.pl_types.ProjectPostComment.USER_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostComment.PROJECT_POST_FIELD_NUMBER);
  }

  public static FullProjectPostComment toFullProjectPostComment(
      org.davincischools.leo.protos.pl_types.ProjectPostCommentOrBuilder projectPostComment) {
    return new FullProjectPostComment(toProjectPostCommentDao(projectPostComment).orElseThrow());
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectPostComment.Builder>
      toProjectPostCommentProto(
          ProjectPostComment projectPostComment,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectPostComment.Builder> newBuilder) {
    return translateToProto(
        projectPostComment,
        newBuilder,
        builder -> {
          toUserXProto(projectPostComment.getUserX(), builder::getUserXBuilder);
          toProjectPostProto(
              projectPostComment.getProjectPost(), false, builder::getProjectPostBuilder);
        },
        org.davincischools.leo.protos.pl_types.ProjectPostComment.USER_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostComment.PROJECT_POST_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectPostComment.Builder>
      toProjectPostCommentProto(
          FullProjectPostComment projectPostComment,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectPostComment.Builder> newBuilder) {
    return toProjectPostCommentProto(projectPostComment.getProjectPostComment(), newBuilder);
  }

  public static Optional<ProjectPostRating> toProjectPostRatingDao(
      org.davincischools.leo.protos.pl_types.ProjectPostRatingOrBuilder projectPostRating) {
    return translateToDao(
        projectPostRating,
        () -> new ProjectPostRating().setCreationTime(Instant.now()),
        dao -> {
          toUserXDao(projectPostRating.getUserX()).ifPresent(dao::setUserX);
          toKnowledgeAndSkillDao(projectPostRating.getKnowledgeAndSkill())
              .ifPresent(dao::setKnowledgeAndSkill);
          toProjectPostDao(projectPostRating.getProjectPost()).ifPresent(dao::setProjectPost);
          dao.setProjectInputFulfillment(
              new ProjectInputFulfillment()
                  .setId(projectPostRating.getProjectInputFulfillmentId()));
        },
        org.davincischools.leo.protos.pl_types.ProjectPostRating.USER_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostRating.KNOWLEDGE_AND_SKILL_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostRating.PROJECT_POST_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostRating
            .PROJECT_INPUT_FULFILLMENT_ID_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectPostRating.Builder>
      toProjectPostRatingProto(
          ProjectPostRating projectPostRating,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectPostRating.Builder> newBuilder) {
    return translateToProto(
        projectPostRating,
        newBuilder,
        builder -> {
          toUserXProto(projectPostRating.getUserX(), builder::getUserXBuilder);
          toKnowledgeAndSkillProto(
              projectPostRating.getKnowledgeAndSkill(), builder::getKnowledgeAndSkillBuilder);
          toProjectPostProto(
              projectPostRating.getProjectPost(), false, builder::getProjectPostBuilder);
          builder.setProjectInputFulfillmentId(
              projectPostRating.getProjectInputFulfillment().getId());
        },
        org.davincischools.leo.protos.pl_types.ProjectPostRating.USER_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostRating.KNOWLEDGE_AND_SKILL_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostRating.PROJECT_POST_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPostRating
            .PROJECT_INPUT_FULFILLMENT_ID_FIELD_NUMBER);
  }

  public static Optional<Interest> toInterestDao(RegisterUserXRequest register_userX_request) {
    return translateToDao(
        register_userX_request,
        () -> new Interest().setCreationTime(Instant.now()),
        dao -> {},
        RegisterUserXRequest.PASSWORD_FIELD_NUMBER,
        RegisterUserXRequest.VERIFY_PASSWORD_FIELD_NUMBER);
  }

  public static Optional<RegisterUserXRequest.Builder> toRegisterUserXRequestProto(
      Interest interest, Supplier<RegisterUserXRequest.Builder> newBuilder) {
    return translateToProto(
        interest,
        newBuilder,
        builder -> {},
        RegisterUserXRequest.PASSWORD_FIELD_NUMBER,
        RegisterUserXRequest.VERIFY_PASSWORD_FIELD_NUMBER);
  }

  public static Optional<UserX> toUserXDao(
      org.davincischools.leo.protos.pl_types.UserXOrBuilder userX) {
    return translateToDao(
        userX,
        () -> new UserX().setCreationTime(Instant.now()),
        dao -> {
          if (userX.getIsAdminX()) {
            if (!isInitialized(dao.getAdminX())) {
              dao.setAdminX(new AdminX());
            }
            dao.getAdminX().setIsCrossDistrictAdminX(userX.getIsCrossDistrictAdminX());
          }
          if (userX.getIsDemo()) {
            if (!isInitialized(dao.getDistrict())) {
              dao.setDistrict(new District());
            }
            dao.getDistrict().setIsDemo(true);
          }
        },
        org.davincischools.leo.protos.pl_types.UserX.IS_ADMIN_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_TEACHER_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_STUDENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_DEMO_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_AUTHENTICATED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_CROSS_DISTRICT_ADMIN_X_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.UserX.Builder> toUserXProto(
      UserX userX, Supplier<org.davincischools.leo.protos.pl_types.UserX.Builder> newBuilder) {
    return translateToProto(
        userX,
        newBuilder,
        builder -> {
          if (isInitialized(userX.getAdminX())) {
            builder.setIsCrossDistrictAdminX(
                Boolean.TRUE.equals(userX.getAdminX().getIsCrossDistrictAdminX()));
          }
          builder
              .setIsAdminX(UserXRepository.isAdminX(userX))
              .setIsTeacher(UserXRepository.isTeacher(userX))
              .setIsStudent(UserXRepository.isStudent(userX))
              .setIsDemo(UserXRepository.isDemo(userX))
              .setIsAuthenticated(UserXRepository.isAuthenticated(userX));
        },
        org.davincischools.leo.protos.pl_types.UserX.IS_ADMIN_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_TEACHER_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_STUDENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_DEMO_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_AUTHENTICATED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_CROSS_DISTRICT_ADMIN_X_FIELD_NUMBER);
  }

  public static Optional<FullUserXDetails.Builder> toFullUserXDetailsProto(
      UserX userX, Supplier<FullUserXDetails.Builder> newBuilder) {
    if (userX != null) {
      var builder = newBuilder.get();
      toUserXProto(userX, builder::getUserXBuilder);
      if (UserXRepository.isStudent(userX)) {
        if (userX.getStudent().getDistrictStudentId() != null) {
          builder.setDistrictStudentId(userX.getStudent().getDistrictStudentId());
        }
        if (userX.getStudent().getGrade() != null) {
          builder.setStudentGrade(userX.getStudent().getGrade());
        }
      }

      var schools = new HashMap<Integer, School>();
      var classXs = new HashMap<Integer, ClassX>();

      if (userX.getTeacher() != null) {
        ifInitialized(
            userX.getTeacher().getTeacherSchools(),
            teacherSchool ->
                schools.put(teacherSchool.getSchool().getId(), teacherSchool.getSchool()));
        ifInitialized(
            userX.getTeacher().getTeacherClassXES(),
            teacherClassX ->
                classXs.put(teacherClassX.getClassX().getId(), teacherClassX.getClassX()));
      }
      if (userX.getStudent() != null) {
        ifInitialized(
            userX.getStudent().getStudentSchools(),
            studentSchool ->
                schools.put(studentSchool.getSchool().getId(), studentSchool.getSchool()));
        ifInitialized(
            userX.getStudent().getStudentClassXES(),
            studentClassX ->
                classXs.put(studentClassX.getClassX().getId(), studentClassX.getClassX()));
      }

      schools.values().forEach(school -> toSchoolProto(school, builder::addSchoolsBuilder));
      classXs.values().forEach(classX -> toClassXProto(classX, false, builder::addClassXsBuilder));
      toDistrictProto(userX.getDistrict(), builder::getDistrictBuilder);

      return Optional.of(builder);
    }
    return Optional.empty();
  }

  public static Optional<Assignment> toAssignmentDao(
      org.davincischools.leo.protos.pl_types.AssignmentOrBuilder assignment) {
    return translateToDao(
        assignment,
        () -> new Assignment().setCreationTime(Instant.now()),
        dao -> {
          toClassXDao(assignment.getClassX()).ifPresent(dao::setClassX);
          dao.setAssignmentKnowledgeAndSkills(
              new LinkedHashSet<>(
                  assignment.getKnowledgeAndSkillsList().stream()
                      .map(ProtoDaoUtils::toKnowledgeAndSkillDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .map(ks -> AssignmentKnowledgeAndSkillRepository.create(dao, ks))
                      .toList()));
          dao.setAssignmentProjectDefinitions(
              new LinkedHashSet<>(
                  assignment.getProjectDefinitionsList().stream()
                      .map(
                          projectDefinition ->
                              new AssignmentProjectDefinition()
                                  .setAssignment(dao)
                                  .setSelected(
                                      projectDefinition.getSelected() ? Instant.now() : Instant.MIN)
                                  .setProjectDefinition(
                                      toProjectDefinitionDao(projectDefinition).orElse(null)))
                      .toList()));
        },
        org.davincischools.leo.protos.pl_types.Assignment.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Assignment.CLASS_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Assignment.PROJECT_DEFINITIONS_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Assignment.Builder>
      toAssignmentProto(
          Assignment assignment,
          boolean recursive,
          Supplier<org.davincischools.leo.protos.pl_types.Assignment.Builder> newBuilder) {
    return translateToProto(
        assignment,
        newBuilder,
        builder -> {
          toClassXProto(assignment.getClassX(), recursive, builder::getClassXBuilder);
          ifInitialized(
              assignment.getAssignmentKnowledgeAndSkills(),
              assignmentKnowledgeAndSkill ->
                  toKnowledgeAndSkillProto(
                      assignmentKnowledgeAndSkill.getKnowledgeAndSkill(),
                      builder::addKnowledgeAndSkillsBuilder));
          ifInitialized(
              assignment.getAssignmentProjectDefinitions(),
              Comparator.comparing(
                      (AssignmentProjectDefinition a) ->
                          Optional.ofNullable(a.getSelected()).orElse(Instant.MIN))
                  .reversed(),
              assignmentProjectDefinition ->
                  toProjectDefinitionProto(
                      assignmentProjectDefinition.getProjectDefinition(),
                      builder::addProjectDefinitionsBuilder));
          if (builder.getProjectDefinitionsCount() > 0) {
            builder.getProjectDefinitionsBuilder(0).setSelected(true);
          }
        },
        org.davincischools.leo.protos.pl_types.Assignment.CLASS_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Assignment.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Assignment.PROJECT_DEFINITIONS_FIELD_NUMBER);
  }

  public static Optional<ClassX> toClassXDao(
      org.davincischools.leo.protos.pl_types.ClassXOrBuilder classX) {
    return translateToDao(
        classX,
        () -> new ClassX().setCreationTime(Instant.now()),
        dao -> {
          toSchoolDao(classX.getSchool()).ifPresent(dao::setSchool);
          dao.setAssignments(
              new LinkedHashSet<>(
                  classX.getAssignmentsList().stream()
                      .map(ProtoDaoUtils::toAssignmentDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList()));
          dao.setClassXKnowledgeAndSkills(
              createJoinTableRows(
                  dao,
                  classX.getKnowledgeAndSkillsList().stream()
                      .map(ProtoDaoUtils::toKnowledgeAndSkillDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .collect(toSet()),
                  ClassXKnowledgeAndSkillRepository::create));
        },
        org.davincischools.leo.protos.pl_types.ClassX.SCHOOL_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ClassX.ASSIGNMENTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ClassX.Builder> toClassXProto(
      ClassX classX, boolean recursive, Supplier<Builder> newBuilder) {
    return translateToProto(
        classX,
        newBuilder,
        builder -> {
          toSchoolProto(classX.getSchool(), builder::getSchoolBuilder);
          ifInitialized(
              classX.getClassXKnowledgeAndSkills(),
              classXKnowledgeAndSkill ->
                  toKnowledgeAndSkillProto(
                      classXKnowledgeAndSkill.getKnowledgeAndSkill(),
                      builder::addKnowledgeAndSkillsBuilder));
          if (recursive) {
            ifInitialized(
                classX.getAssignments(),
                assignment -> toAssignmentProto(assignment, false, builder::addAssignmentsBuilder));
          }
        },
        org.davincischools.leo.protos.pl_types.ClassX.SCHOOL_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ClassX.ASSIGNMENTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER);
  }

  public static Optional<District> toDistrictDao(
      org.davincischools.leo.protos.pl_types.DistrictOrBuilder district) {
    return translateToDao(district, () -> new District().setCreationTime(Instant.now()), dao -> {});
  }

  public static Optional<org.davincischools.leo.protos.pl_types.District.Builder> toDistrictProto(
      District district,
      Supplier<org.davincischools.leo.protos.pl_types.District.Builder> newBuilder) {
    return translateToProto(district, newBuilder, builder -> {});
  }

  public static Optional<KnowledgeAndSkill> toKnowledgeAndSkillDao(
      org.davincischools.leo.protos.pl_types.KnowledgeAndSkillOrBuilder knowledgeAndSkill) {
    return translateToDao(
        knowledgeAndSkill,
        () -> new KnowledgeAndSkill().setCreationTime(Instant.now()),
        dao -> toUserXDao(knowledgeAndSkill.getUserX()).ifPresent(dao::setUserX),
        org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.USER_X_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder>
      toKnowledgeAndSkillProto(
          KnowledgeAndSkill knowledgeAndSkill,
          Supplier<org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder> newBuilder) {
    return translateToProto(
        knowledgeAndSkill,
        newBuilder,
        builder -> toUserXProto(knowledgeAndSkill.getUserX(), builder::getUserXBuilder),
        org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.USER_X_FIELD_NUMBER);
  }

  public static Optional<School> toSchoolDao(
      org.davincischools.leo.protos.pl_types.SchoolOrBuilder school) {
    return translateToDao(
        school,
        () -> new School().setCreationTime(Instant.now()),
        dao -> toDistrictDao(school.getDistrict()).ifPresent(dao::setDistrict),
        org.davincischools.leo.protos.pl_types.School.DISTRICT_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.School.Builder> toSchoolProto(
      School school, Supplier<org.davincischools.leo.protos.pl_types.School.Builder> newBuilder) {
    return translateToProto(
        school,
        newBuilder,
        builder -> toDistrictProto(school.getDistrict(), builder::getDistrictBuilder),
        org.davincischools.leo.protos.pl_types.School.DISTRICT_FIELD_NUMBER);
  }

  public static Optional<Tag> toTagDao(org.davincischools.leo.protos.pl_types.TagOrBuilder tag) {
    return translateToDao(tag, () -> new Tag().setCreationTime(Instant.now()), dao -> {});
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Tag.Builder> toTagProto(
      Tag tag, Supplier<org.davincischools.leo.protos.pl_types.Tag.Builder> newBuilder) {
    return translateToProto(tag, newBuilder, builder -> {});
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <M extends MessageOrBuilder, D> Optional<D> translateToDao(
      @Nullable M fromMessage,
      Supplier<D> newDao,
      Consumer<D> customTranslations,
      int... ignoreFieldNumbers) {
    checkNotNull(newDao);
    checkNotNull(customTranslations);

    if (fromMessage == null || fromMessage.equals(fromMessage.getDefaultInstanceForType())) {
      return Optional.empty();
    }

    D translatedDao = checkNotNull(newDao.get());
    Class<? extends MessageOrBuilder> protoClass = fromMessage.getClass();
    Descriptor protoDescriptor = fromMessage.getDescriptorForType();
    Class<?> daoClass = getDaoClass(translatedDao);

    Set<Integer> ignoredFieldNumbers = Arrays.stream(ignoreFieldNumbers).boxed().collect(toSet());

    Map<Integer, BiConsumer</* message= */ MessageOrBuilder, /* dao= */ Object>> daoSetters =
        protoToDaoSetters.computeIfAbsent(
            new ProtoDaoFields(protoClass, daoClass, ignoredFieldNumbers),
            (protoDao) -> {
              Map<Integer, BiConsumer</* message= */ MessageOrBuilder, /* dao= */ Object>> setters =
                  new HashMap<>();

              // Get the list of all uniquely named methods.
              ImmutableMap<String, Method> setMethods =
                  ImmutableMap.copyOf(
                      Maps.filterValues(
                          Maps.transformValues(
                              Maps.filterValues(
                                  Multimaps.index(
                                          Arrays.asList(daoClass.getMethods()), Method::getName)
                                      .asMap(),
                                  methods -> methods.size() == 1),
                              Iterables::getOnlyElement),
                          method ->
                              method.getName().startsWith("set")
                                  && method.getParameterTypes().length == 1));

              // Build the translators for each proto field.
              for (FieldDescriptor field : protoDescriptor.getFields()) {
                try {
                  // Skip field if it's ignored.
                  if (ignoredFieldNumbers.contains(field.getNumber())) {
                    continue;
                  }
                  if (field.isRepeated()) {
                    throw new IOException(
                        "A repeated field cannot be translated to a dao field: "
                            + field.getFullName());
                  }

                  Optional<Method> setMethod =
                      Optional.ofNullable(setMethods.get(toDaoSetMethod(field)));

                  // Check for special mappings.
                  if (setMethod.isEmpty()) {
                    // Process id fields specially.
                    if (field.getName().endsWith("_id")
                        && field.getType() == FieldDescriptor.Type.INT32) {

                      // Make sure the dao has a setter for a foreign object.
                      Optional<Method> setDaoMethod =
                          Optional.ofNullable(setMethods.get(toDaoSetDaoMethod(field)));
                      if (setDaoMethod.isEmpty()) {
                        throw new IOException("Unmapped id field: " + field.getFullName());
                      }

                      // Get supposed dao type.
                      Class<?> innerDaoType = setDaoMethod.get().getParameterTypes()[0];
                      Constructor<?> constructor = null;
                      for (Constructor<?> candidate : innerDaoType.getConstructors()) {
                        if (candidate.getParameterCount() == 0) {
                          constructor = candidate;
                          break;
                        }
                      }
                      if (constructor == null) {
                        throw new IOException("Cannot create inner dao: " + field.getFullName());
                      }
                      Constructor<?> finalConstructor = constructor;

                      // Add translator to set the inner dao object.
                      setters.put(
                          field.getNumber(),
                          (message, dao) -> {
                            try {
                              if (message.hasField(field)) {
                                Object innerDao = finalConstructor.newInstance();
                                getDaoClass(innerDao)
                                    .getMethod("setId", Integer.class)
                                    .invoke(innerDao, message.getField(field));
                                setDaoMethod.get().invoke(dao, innerDao);
                              }
                            } catch (Exception e) {
                              throw new RuntimeException(
                                  "Error processing proto field as id to inner dao value: "
                                      + field.getFullName(),
                                  e);
                            }
                          });
                      continue;
                    }

                    // Process time fields specially.
                    if (field.getName().endsWith("_time_ms")
                        && field.getType() == FieldDescriptor.Type.INT64) {

                      // Make sure the dao has a setter for time.
                      Optional<Method> setTimeMethod =
                          Optional.ofNullable(setMethods.get(toDaoSetTimeMethod(field)));
                      if (setTimeMethod.isEmpty()) {
                        throw new IOException("Unmapped time field: " + field.getFullName());
                      }

                      setters.put(
                          field.getNumber(),
                          (message, dao) -> {
                            try {
                              if (message.hasField(field)) {
                                setTimeMethod
                                    .get()
                                    .invoke(
                                        dao, Instant.ofEpochMilli((Long) message.getField(field)));
                              }
                            } catch (Exception e) {
                              throw new RuntimeException(
                                  "Error processing proto field as time value: "
                                      + field.getFullName(),
                                  e);
                            }
                          });

                      continue;
                    }

                    logger
                        .atWarn()
                        .log("No mapping from {} to {}.", field.getFullName(), daoClass.getName());
                  }

                  // Check for an enum field, it needs to be translated to a string.
                  if (field.getType() == FieldDescriptor.Type.ENUM) {
                    setters.put(
                        field.getNumber(),
                        (message, dao) -> {
                          try {
                            if (message.hasField(field)) {
                              var protoEnumValue = ((EnumValueDescriptor) message.getField(field));
                              if (protoEnumValue.getNumber() != 0) {
                                var parameterType = setMethod.get().getParameterTypes()[0];
                                if (Enum.class.isAssignableFrom(parameterType)) {
                                  setMethod
                                      .get()
                                      .invoke(
                                          dao,
                                          Enum.valueOf(
                                              (Class<Enum>) parameterType,
                                              protoEnumValue.getName()));
                                } else {
                                  setMethod.get().invoke(dao, protoEnumValue.getName());
                                }
                              } else {
                                setMethod.get().invoke(dao, (Object[]) null);
                              }
                            }
                          } catch (Exception e) {
                            throw new RuntimeException(
                                "Error processing proto field as enum to string dao value: "
                                    + field.getFullName(),
                                e);
                          }
                        });
                    continue;
                  }

                  // Assume that it's the same type.
                  setters.put(
                      field.getNumber(),
                      (message, dao) -> {
                        try {
                          if (message.hasField(field)) {
                            setMethod.get().invoke(dao, message.getField(field));
                          }
                        } catch (Exception e) {
                          throw new RuntimeException(
                              "Error processing proto field as direct dao value: "
                                  + field.getFullName(),
                              e);
                        }
                      });
                } catch (Exception e) {
                  throw new RuntimeException(
                      "Error processing proto field: " + field.getFullName(), e);
                }
              }

              return setters;
            });

    // Translate each proto field.
    for (FieldDescriptor field : fromMessage.getDescriptorForType().getFields()) {
      if (ignoredFieldNumbers.contains(field.getNumber())) {
        continue;
      }
      if (!daoSetters.containsKey(field.getNumber())) {
        throw new RuntimeException("Proto field is not accounted for: " + field.getFullName());
      }
      if (fromMessage.hasField(field)) {
        daoSetters.get(field.getNumber()).accept(fromMessage, translatedDao);
      }
    }

    customTranslations.accept(translatedDao);

    return Optional.of(translatedDao);
  }

  public static <M extends Message.Builder, D> Optional<M> translateToProto(
      D fromDao,
      Supplier<M> toMessageSupplier,
      Consumer<M> customTranslations,
      int... ignoreFieldNumbers) {
    checkNotNull(toMessageSupplier);

    if (fromDao == null) {
      return Optional.empty();
    }

    final Class<?> daoClass = getDaoClass(fromDao);

    if (!Hibernate.isInitialized(fromDao)) {
      // Still, the ID is there.
      try {
        Object id = daoClass.getMethod("getId").invoke(fromDao);
        if (id instanceof Integer && (Integer) id > 0) {
          M toMessage = toMessageSupplier.get();
          Descriptor protoDescriptor = toMessage.getDescriptorForType();
          toMessage.setField(protoDescriptor.findFieldByName("id"), id);
          return Optional.of(toMessage);
        }
        return Optional.empty();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    M toMessage = toMessageSupplier.get();
    Class<? extends Message.Builder> protoClass = toMessage.getClass();
    Descriptor protoDescriptor = toMessage.getDescriptorForType();

    Set<Integer> ignoredFieldNumbers = Arrays.stream(ignoreFieldNumbers).boxed().collect(toSet());

    Map<Integer, BiConsumer</* dao= */ Object, /* message= */ Message.Builder>> protoSetters =
        daoToProtoSetters.computeIfAbsent(
            new ProtoDaoFields(protoClass, daoClass, ignoredFieldNumbers),
            (protoDao) -> {
              Map<Integer, BiConsumer</* dao= */ Object, /* message= */ Message.Builder>> setters =
                  new HashMap<>();

              // Get the list of all uniquely named methods.
              ImmutableMap<String, Method> getMethods =
                  ImmutableMap.copyOf(
                      Maps.filterValues(
                          Maps.transformValues(
                              Maps.filterValues(
                                  Multimaps.index(
                                          Arrays.asList(daoClass.getMethods()), Method::getName)
                                      .asMap(),
                                  methods -> methods.size() == 1),
                              Iterables::getOnlyElement),
                          method ->
                              method.getName().startsWith("get")
                                  && method.getParameterTypes().length == 0));

              // Build the translators for each proto field.
              for (FieldDescriptor field : protoDescriptor.getFields()) {
                try {
                  // Skip field if it's ignored.
                  if (ignoredFieldNumbers.contains(field.getNumber())) {
                    continue;
                  }
                  if (field.isRepeated()) {
                    throw new IOException(
                        "A dao field cannot be translated to a repeated proto field: "
                            + field.getFullName());
                  }

                  Optional<Method> getMethod =
                      Optional.ofNullable(getMethods.get(toDaoGetMethod(field)));

                  // Check for special mappings.
                  if (getMethod.isEmpty()) {
                    // Process id fields specially.
                    if (field.getName().endsWith("_id")
                        && field.getType() == FieldDescriptor.Type.INT32) {

                      // Make sure the dao has a getter for a foreign object.
                      Optional<Method> getDaoMethod =
                          Optional.ofNullable(getMethods.get(toDaoGetDaoMethod(field)));
                      if (getDaoMethod.isEmpty()) {
                        throw new IOException("Unmapped id field: " + field.getFullName());
                      }

                      // Get supposed dao type.
                      Class<?> daoType = getDaoMethod.get().getReturnType();
                      Method getDaoId = daoType.getMethod("getId");
                      if (getDaoId.getReturnType() != Integer.class) {
                        throw new IOException("Id field is not an integer: " + field.getFullName());
                      }

                      // Add translator to set the inner dao object.
                      setters.put(
                          field.getNumber(),
                          (dao, message) -> {
                            try {
                              Object innerDao = getDaoMethod.get().invoke(dao);
                              if (innerDao != null) {
                                Integer innerDaoId = (Integer) getDaoId.invoke(innerDao);
                                if (innerDaoId != null) {
                                  message.setField(field, innerDaoId);
                                }
                              }
                            } catch (Exception e) {
                              throw new RuntimeException(
                                  "Error processing proto field as id value from inner dao: "
                                      + field.getFullName(),
                                  e);
                            }
                          });
                      continue;
                    }

                    // Process time fields specially.
                    if (field.getName().endsWith("_time_ms")
                        && field.getType() == FieldDescriptor.Type.INT64) {

                      // Make sure the dao has a setter for time.
                      Optional<Method> getTimeMethod =
                          Optional.ofNullable(getMethods.get(toDaoGetTimeMethod(field)));
                      if (getTimeMethod.isEmpty()) {
                        throw new IOException("Unmapped time field: " + field.getFullName());
                      }

                      setters.put(
                          field.getNumber(),
                          (dao, message) -> {
                            try {
                              Object value = getTimeMethod.get().invoke(dao);
                              if (value != null) {
                                message.setField(field, ((Instant) value).toEpochMilli());
                              }
                            } catch (Exception e) {
                              throw new RuntimeException(
                                  "Error processing proto field as time value: "
                                      + field.getFullName(),
                                  e);
                            }
                          });

                      continue;
                    }

                    throw new IOException("Cannot map the following field: " + field.getFullName());
                  }

                  // Check for an enum field, it needs to be translated to a proto enum.
                  if (field.getType() == FieldDescriptor.Type.ENUM) {
                    setters.put(
                        field.getNumber(),
                        (dao, message) -> {
                          try {
                            var returnType = getMethod.get().getReturnType();
                            if (Enum.class.isAssignableFrom(returnType)) {
                              var daoValue = (Enum<?>) getMethod.get().invoke(dao);
                              if (daoValue != null) {
                                var enumValue =
                                    field.getEnumType().findValueByName(daoValue.name());
                                if (enumValue == null) {
                                  throw new RuntimeException(
                                      "Enum value does not exist: " + daoValue.name());
                                }
                                message.setField(field, enumValue);
                              } else {
                                message.clearField(field);
                              }
                            } else {
                              String daoValue = (String) getMethod.get().invoke(dao);
                              if (daoValue != null) {
                                var enumValue = field.getEnumType().findValueByName(daoValue);
                                if (enumValue == null) {
                                  throw new RuntimeException(
                                      "Enum value does not exist: " + daoValue);
                                }
                                message.setField(field, enumValue);
                              } else {
                                message.clearField(field);
                              }
                            }
                          } catch (Exception e) {
                            throw new RuntimeException(
                                "Error processing proto field as enum value from dao: "
                                    + field.getFullName(),
                                e);
                          }
                        });
                    continue;
                  }

                  // Assume that it's the same type.
                  setters.put(
                      field.getNumber(),
                      (dao, message) -> {
                        try {
                          Object value = getMethod.get().invoke(dao);
                          if (value != null) {
                            message.setField(field, value);
                          }
                        } catch (Exception e) {
                          throw new RuntimeException(
                              "Error processing proto field as direct value from dao: "
                                  + field.getFullName(),
                              e);
                        }
                      });
                } catch (Exception e) {
                  throw new RuntimeException(
                      "Error processing proto field: " + field.getFullName(), e);
                }
              }

              return setters;
            });

    // Translate each proto field.
    for (FieldDescriptor field : toMessage.getDescriptorForType().getFields()) {
      if (ignoredFieldNumbers.contains(field.getNumber())) {
        continue;
      }
      if (!protoSetters.containsKey(field.getNumber())) {
        throw new RuntimeException("Proto field is not accounted for: " + field.getFullName());
      }
      try {
        protoSetters.get(field.getNumber()).accept(fromDao, toMessage);
      } catch (/* InvocationTargetException */ Exception e) {
        if (e.getCause() != null
            && e.getCause().getCause() instanceof LazyInitializationException) {
          // Do nothing. Hibernate.isPropertyInitialized() will return false for the id field of
          // an unitialized proxy, even though we can still retrieve it. So, we catch the exception
          // for the other fields instead of doing a check ahead of time.
        } else {
          throw e;
        }
      }
    }

    customTranslations.accept(toMessage);

    return Optional.of(toMessage);
  }

  private static String toDaoSetMethod(FieldDescriptor field) {
    checkNotNull(field);

    return "set" + capitalizeFirst(field.getJsonName());
  }

  private static String toDaoSetDaoMethod(FieldDescriptor field) {
    checkNotNull(field);
    checkArgument(field.getName().endsWith("_id"));

    return "set" + capitalizeFirst(field.getJsonName().replaceFirst("Id$", ""));
  }

  private static String toDaoSetTimeMethod(FieldDescriptor field) {
    checkNotNull(field);
    checkArgument(field.getName().endsWith("_time_ms"));

    return "set" + capitalizeFirst(field.getJsonName().replaceFirst("Ms$", ""));
  }

  private static String toDaoGetMethod(FieldDescriptor field) {
    checkNotNull(field);

    return "get" + capitalizeFirst(field.getJsonName());
  }

  private static String toDaoGetDaoMethod(FieldDescriptor field) {
    checkNotNull(field);
    checkArgument(field.getName().endsWith("_id"));

    return "get" + capitalizeFirst(field.getJsonName().replaceFirst("Id$", ""));
  }

  private static String toDaoGetTimeMethod(FieldDescriptor field) {
    checkNotNull(field);
    checkArgument(field.getName().endsWith("_time_ms"));

    return "get" + capitalizeFirst(field.getJsonName().replaceFirst("Ms$", ""));
  }

  private static String capitalizeFirst(String value) {
    checkNotNull(value);

    if (value.isEmpty()) {
      return value;
    }

    return value.substring(0, 1).toUpperCase(Locale.US) + value.substring(1);
  }
}
