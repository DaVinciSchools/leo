package org.davincischools.leo.server.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.repos.ClassXRepository.FullClassX;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository.FullProjectDefinition;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.FullProjectInput;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository.FullProjectPostComment;
import org.davincischools.leo.database.utils.repos.ProjectPostRepository.FullProjectPost;
import org.davincischools.leo.database.utils.repos.ProjectRepository.MilestoneWithSteps;
import org.davincischools.leo.database.utils.repos.ProjectRepository.ProjectWithMilestones;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.davincischools.leo.protos.pl_types.ProjectDefinition.State;
import org.davincischools.leo.protos.pl_types.ProjectInputValue;
import org.davincischools.leo.protos.user_x_management.FullUserXDetails;
import org.davincischools.leo.protos.user_x_management.RegisterUserXRequest;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

public class ProtoDaoUtils {

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
      toProjectDefinition(
          FullProjectDefinition fullProjectDefinition,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder> newBuilder) {
    return translateToProto(
        fullProjectDefinition.definition(),
        newBuilder,
        builder -> {
          fullProjectDefinition
              .categories()
              .forEach(
                  categoryDao -> {
                    var type = categoryDao.getProjectDefinitionCategoryType();
                    createProjectInputValueProto(
                        categoryDao.getId(), type, builder.addInputsBuilder());
                  });
        },
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUT_ID_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.INPUTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.SELECTED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectDefinition.STATE_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder>
      toProjectDefinition(
          FullProjectInput fullProjectInput,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder> newBuilder) {
    var projectDefinitionProto =
        newBuilder
            .get()
            .setId(fullProjectInput.definition().getId())
            .setName(fullProjectInput.definition().getName())
            .setInputId(fullProjectInput.input().getId())
            .setState(State.valueOf(fullProjectInput.input().getState()))
            .setTemplate(Boolean.TRUE.equals(fullProjectInput.definition().getTemplate()));
    for (var categoryValues : fullProjectInput.values()) {
      var type = categoryValues.type();
      var inputValueProto =
          createProjectInputValueProto(
              categoryValues.category().getId(), type, projectDefinitionProto.addInputsBuilder());
      switch (inputValueProto.getCategory().getValueType()) {
        case FREE_TEXT -> inputValueProto.addAllFreeTexts(
            categoryValues.values().stream()
                .map(org.davincischools.leo.database.daos.ProjectInputValue::getFreeTextValue)
                .toList());
        case EKS, XQ_COMPETENCY -> inputValueProto.addAllSelectedIds(
            categoryValues.values().stream()
                .map(
                    org.davincischools.leo.database.daos.ProjectInputValue
                        ::getKnowledgeAndSkillValue)
                .map(KnowledgeAndSkill::getId)
                .toList());
        case MOTIVATION -> inputValueProto.addAllSelectedIds(
            categoryValues.values().stream()
                .map(org.davincischools.leo.database.daos.ProjectInputValue::getMotivationValue)
                .map(Motivation::getId)
                .toList());
        case UNSET -> throw new IllegalStateException("Unset value type");
      }
    }
    return Optional.of(projectDefinitionProto);
  }

  private static ProjectInputValue.Builder createProjectInputValueProto(
      int categoryId,
      ProjectDefinitionCategoryType type,
      ProjectInputValue.Builder projectInputValueProto) {
    projectInputValueProto
        .getCategoryBuilder()
        .setId(categoryId)
        .setShortDescr(type.getShortDescr())
        .setInputDescr(type.getInputDescr())
        .setName(type.getName())
        .setHint(type.getHint())
        .setPlaceholder(type.getInputPlaceholder())
        .setValueType(
            org.davincischools.leo.protos.pl_types.ProjectInputCategory.ValueType.valueOf(
                type.getValueType()));
    return projectInputValueProto;
  }

  //
  // ---- Automated and tested converters. ----
  //

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Milestone.Step.Builder>
      toMilestoneStepProto(
          ProjectMilestoneStep projectMilestoneStep,
          Supplier<org.davincischools.leo.protos.pl_types.Project.Milestone.Step.Builder>
              newBuilder) {
    return translateToProto(projectMilestoneStep, newBuilder);
  }

  public static ProjectMilestoneStep toProjectMilestoneStepDao(
      org.davincischools.leo.protos.pl_types.Project.Milestone.StepOrBuilder step) {
    ProjectMilestoneStep dao =
        translateToDao(step, new ProjectMilestoneStep().setCreationTime(Instant.now()));
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Milestone.Builder>
      toMilestoneProto(
          ProjectMilestone projectMilestone,
          Supplier<org.davincischools.leo.protos.pl_types.Project.Milestone.Builder> newBuilder) {
    return translateToProto(
        projectMilestone,
        newBuilder,
        org.davincischools.leo.protos.pl_types.Project.Milestone.STEPS_FIELD_NUMBER);
  }

  public static ProjectMilestone toProjectMilestoneDao(
      org.davincischools.leo.protos.pl_types.Project.MilestoneOrBuilder step) {
    ProjectMilestone dao =
        translateToDao(
            step,
            new ProjectMilestone().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.Project.Milestone.STEPS_FIELD_NUMBER);
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Milestone.Builder>
      toMilestoneProto(
          MilestoneWithSteps milestone,
          Supplier<org.davincischools.leo.protos.pl_types.Project.Milestone.Builder> newBuilder) {
    var builder = toMilestoneProto(milestone.milestone(), newBuilder);
    builder.ifPresent(
        b -> milestone.steps().forEach(s -> toMilestoneStepProto(s, b::addStepsBuilder)));
    return builder;
  }

  public static MilestoneWithSteps toMilestoneWithStepsRecord(
      org.davincischools.leo.protos.pl_types.Project.MilestoneOrBuilder milestone) {
    return new MilestoneWithSteps(
        toProjectMilestoneDao(milestone),
        milestone.getStepsList().stream().map(ProtoDaoUtils::toProjectMilestoneStepDao).toList());
  }

  public static Project toProjectDao(
      org.davincischools.leo.protos.pl_types.ProjectOrBuilder project) {
    Project dao =
        translateToDao(
            project,
            new Project().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.Project.ASSIGNMENT_FIELD_NUMBER,
            org.davincischools.leo.protos.pl_types.Project.MILESTONES_FIELD_NUMBER);
    if (project.hasAssignment()) {
      dao.setAssignment(toAssignmentDao(project.getAssignment()));
    }
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Builder> toProjectProto(
      Project project,
      Supplier<org.davincischools.leo.protos.pl_types.Project.Builder> newBuilder) {
    return translateToProto(
        project,
        newBuilder,
        builder -> toAssignmentProto(project.getAssignment(), builder::getAssignmentBuilder),
        org.davincischools.leo.protos.pl_types.Project.ASSIGNMENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.Project.MILESTONES_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Project.Builder> toProjectProto(
      ProjectWithMilestones projectWithMilestones,
      Supplier<org.davincischools.leo.protos.pl_types.Project.Builder> newBuilder) {
    var builder = toProjectProto(projectWithMilestones.project(), newBuilder);
    builder.ifPresent(
        b ->
            projectWithMilestones
                .milestones()
                .forEach(m -> toMilestoneProto(m, b::addMilestonesBuilder)));
    return builder;
  }

  public static ProjectWithMilestones toProjectWithMilestonesRecord(
      org.davincischools.leo.protos.pl_types.ProjectOrBuilder project) {
    return new ProjectWithMilestones(
        toProjectDao(project),
        project.getMilestonesList().stream()
            .map(ProtoDaoUtils::toMilestoneWithStepsRecord)
            .toList());
  }

  public static ProjectPost toProjectPostDao(
      org.davincischools.leo.protos.pl_types.ProjectPostOrBuilder projectPost) {
    ProjectPost dao =
        translateToDao(
            projectPost,
            new ProjectPost().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.ProjectPost.USER_X_FIELD_NUMBER,
            org.davincischools.leo.protos.pl_types.ProjectPost.TAGS_FIELD_NUMBER,
            org.davincischools.leo.protos.pl_types.ProjectPost.COMMENTS_FIELD_NUMBER,
            org.davincischools.leo.protos.pl_types.ProjectPost.PROJECT_FIELD_NUMBER);
    if (projectPost.hasUserX()) {
      dao.setUserX(toUserXDao(projectPost.getUserX()));
    }
    if (projectPost.hasProject()) {
      dao.setProject(toProjectDao(projectPost.getProject()));
    }
    return dao;
  }

  public static FullProjectPost toFullProjectPostRecord(
      org.davincischools.leo.protos.pl_types.ProjectPostOrBuilder projectPost) {
    FullProjectPost fullProjectPost =
        new FullProjectPost().setProjectPost(toProjectPostDao(projectPost));
    projectPost
        .getTagsList()
        .forEach(tag -> fullProjectPost.getTags().put(tag.getUserXId(), tag.getText()));
    fullProjectPost
        .getProjectPostComments()
        .putAll(
            projectPost.getCommentsList().stream()
                .map(ProtoDaoUtils::toFullProjectPostComment)
                .collect(Collectors.toMap(e -> e.getProjectPostComment().getPostTime(), e -> e)));
    return fullProjectPost;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectPost.Builder>
      toProjectPostProto(
          ProjectPost projectPost,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectPost.Builder> newBuilder) {
    return translateToProto(
        projectPost,
        newBuilder,
        b -> {
          toUserXProto(projectPost.getUserX(), b::getUserXBuilder);
          toProjectProto(projectPost.getProject(), b::getProjectBuilder);
        },
        org.davincischools.leo.protos.pl_types.ProjectPost.USER_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.TAGS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.COMMENTS_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ProjectPost.PROJECT_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ProjectPost.Builder>
      toProjectPostProto(
          FullProjectPost projectPost,
          Supplier<org.davincischools.leo.protos.pl_types.ProjectPost.Builder> newBuilder) {
    var builder = toProjectPostProto(projectPost.getProjectPost(), newBuilder);
    builder.ifPresent(
        b -> {
          projectPost
              .getProjectPostComments()
              .values()
              .forEach(comment -> toProjectPostCommentProto(comment, b::addCommentsBuilder));
          projectPost
              .getTags()
              .entries()
              .forEach(
                  tag ->
                      toTagProto(
                          new Tag()
                              .setUserX(new UserX().setId(tag.getKey()))
                              .setText(tag.getValue()),
                          b::addTagsBuilder));
        });
    return builder;
  }

  public static ProjectPostComment toProjectPostCommentDao(
      org.davincischools.leo.protos.pl_types.ProjectPostCommentOrBuilder projectPostComment) {
    ProjectPostComment dao =
        translateToDao(
            projectPostComment,
            new ProjectPostComment().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.ProjectPostComment.USER_X_FIELD_NUMBER,
            org.davincischools.leo.protos.pl_types.ProjectPostComment.PROJECT_POST_FIELD_NUMBER);
    if (projectPostComment.hasUserX()) {
      dao.setUserX(toUserXDao(projectPostComment.getUserX()));
    }
    if (projectPostComment.hasProjectPost()) {
      dao.setProjectPost(toProjectPostDao(projectPostComment.getProjectPost()));
    }
    return dao;
  }

  public static FullProjectPostComment toFullProjectPostComment(
      org.davincischools.leo.protos.pl_types.ProjectPostCommentOrBuilder projectPostComment) {
    return new FullProjectPostComment(toProjectPostCommentDao(projectPostComment));
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
          toProjectPostProto(projectPostComment.getProjectPost(), builder::getProjectPostBuilder);
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

  public static Interest toInterestDao(RegisterUserXRequest register_userX_request) {
    return translateToDao(
        register_userX_request,
        new Interest().setCreationTime(Instant.now()),
        RegisterUserXRequest.PASSWORD_FIELD_NUMBER,
        RegisterUserXRequest.VERIFY_PASSWORD_FIELD_NUMBER);
  }

  public static Optional<RegisterUserXRequest.Builder> toRegisterUserXRequestProto(
      Interest interest, Supplier<RegisterUserXRequest.Builder> newBuilder) {
    return translateToProto(
        interest,
        newBuilder,
        RegisterUserXRequest.PASSWORD_FIELD_NUMBER,
        RegisterUserXRequest.VERIFY_PASSWORD_FIELD_NUMBER);
  }

  public static UserX toUserXDao(org.davincischools.leo.protos.pl_types.UserXOrBuilder userX) {
    return translateToDao(
        userX,
        new UserX().setCreationTime(Instant.now()),
        org.davincischools.leo.protos.pl_types.UserX.IS_ADMIN_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_TEACHER_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_STUDENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_DEMO_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_AUTHENTICATED_FIELD_NUMBER);
  }

  public static Optional<org.davincischools.leo.protos.pl_types.UserX.Builder> toUserXProto(
      UserX userX, Supplier<org.davincischools.leo.protos.pl_types.UserX.Builder> newBuilder) {
    return translateToProto(
        userX,
        newBuilder,
        builder -> {
          if (UserXRepository.isAdminX(userX)) {
            builder.setIsAdminX(true);
          }
          if (UserXRepository.isTeacher(userX)) {
            builder.setIsTeacher(true);
          }
          if (UserXRepository.isStudent(userX)) {
            builder.setIsStudent(true);
          }
          builder
              .setIsDemo(UserXRepository.isDemo(userX))
              .setIsAuthenticated(UserXRepository.isAuthenticated(userX));
        },
        org.davincischools.leo.protos.pl_types.UserX.IS_ADMIN_X_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_TEACHER_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_STUDENT_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_DEMO_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.UserX.IS_AUTHENTICATED_FIELD_NUMBER);
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
      return Optional.of(builder);
    }
    return Optional.empty();
  }

  public static Assignment toAssignmentDao(
      org.davincischools.leo.protos.pl_types.AssignmentOrBuilder assignment) {
    Assignment dao =
        translateToDao(
            assignment,
            new Assignment().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.Assignment.CLASS_X_FIELD_NUMBER);
    if (assignment.hasClassX()) {
      dao.setClassX(toClassXDao(assignment.getClassX()));
    }
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Assignment.Builder>
      toAssignmentProto(
          Assignment assignment,
          Supplier<org.davincischools.leo.protos.pl_types.Assignment.Builder> newBuilder) {
    return translateToProto(
        assignment,
        newBuilder,
        builder -> {
          toClassXProto(assignment.getClassX(), builder::getClassXBuilder);
        },
        org.davincischools.leo.protos.pl_types.Assignment.CLASS_X_FIELD_NUMBER);
  }

  public static ClassX toClassXDao(org.davincischools.leo.protos.pl_types.ClassXOrBuilder classX) {
    ClassX dao =
        translateToDao(
            classX,
            new ClassX().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.ClassX.SCHOOL_FIELD_NUMBER,
            org.davincischools.leo.protos.pl_types.ClassX.ENROLLED_FIELD_NUMBER,
            org.davincischools.leo.protos.pl_types.ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER);
    if (classX.hasSchool()) {
      dao.setSchool(toSchoolDao(classX.getSchool()));
    }
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ClassX.Builder> toClassXProto(
      ClassX classX, Supplier<org.davincischools.leo.protos.pl_types.ClassX.Builder> newBuilder) {
    return translateToProto(
        classX,
        newBuilder,
        builder -> {
          toSchoolProto(classX.getSchool(), builder::getSchoolBuilder);
        },
        org.davincischools.leo.protos.pl_types.ClassX.SCHOOL_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ClassX.ENROLLED_FIELD_NUMBER,
        org.davincischools.leo.protos.pl_types.ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER);
  }

  public static FullClassX toFullClassXRecord(
      org.davincischools.leo.protos.pl_types.ClassXOrBuilder classX) {
    return new FullClassX(
        toClassXDao(classX),
        classX.getEnrolled(),
        classX.getKnowledgeAndSkillsOrBuilderList().stream()
            .map(ProtoDaoUtils::toKnowledgeAndSkillDao)
            .toList());
  }

  public static Optional<org.davincischools.leo.protos.pl_types.ClassX.Builder> toFullClassXProto(
      FullClassX fullClassX,
      Supplier<org.davincischools.leo.protos.pl_types.ClassX.Builder> newBuilder) {
    var builder = toClassXProto(fullClassX.classX(), newBuilder);
    builder.ifPresent(
        b -> {
          b.setEnrolled(fullClassX.enrolled());
          fullClassX
              .knowledgeAndSkills()
              .forEach(k -> toKnowledgeAndSkillProto(k, b::addKnowledgeAndSkillsBuilder));
        });
    return builder;
  }

  public static District toDistrictDao(
      org.davincischools.leo.protos.pl_types.DistrictOrBuilder district) {
    return translateToDao(district, new District().setCreationTime(Instant.now()));
  }

  public static Optional<org.davincischools.leo.protos.pl_types.District.Builder> toDistrictProto(
      District district,
      Supplier<org.davincischools.leo.protos.pl_types.District.Builder> newBuilder) {
    return translateToProto(district, newBuilder);
  }

  public static KnowledgeAndSkill toKnowledgeAndSkillDao(
      org.davincischools.leo.protos.pl_types.KnowledgeAndSkillOrBuilder knowledgeAndSkill) {
    KnowledgeAndSkill dao =
        translateToDao(
            knowledgeAndSkill,
            new KnowledgeAndSkill().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.USER_X_FIELD_NUMBER);
    if (knowledgeAndSkill.hasUserX()) {
      dao.setUserX(toUserXDao(knowledgeAndSkill.getUserX()));
    }
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder>
      toKnowledgeAndSkillProto(
          KnowledgeAndSkill knowledgeAndSkill,
          Supplier<org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder> newBuilder) {
    return translateToProto(
        knowledgeAndSkill,
        newBuilder,
        builder -> {
          toUserXProto(knowledgeAndSkill.getUserX(), builder::getUserXBuilder);
        },
        org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.USER_X_FIELD_NUMBER);
  }

  public static School toSchoolDao(org.davincischools.leo.protos.pl_types.SchoolOrBuilder school) {
    School dao =
        translateToDao(
            school,
            new School().setCreationTime(Instant.now()),
            org.davincischools.leo.protos.pl_types.School.DISTRICT_FIELD_NUMBER);
    if (school.hasDistrict()) {
      dao.setDistrict(toDistrictDao(school.getDistrict()));
    }
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.School.Builder> toSchoolProto(
      School school, Supplier<org.davincischools.leo.protos.pl_types.School.Builder> newBuilder) {
    return translateToProto(
        school,
        newBuilder,
        builder -> {
          toDistrictProto(school.getDistrict(), builder::getDistrictBuilder);
        },
        org.davincischools.leo.protos.pl_types.School.DISTRICT_FIELD_NUMBER);
  }

  public static Tag toTagDao(org.davincischools.leo.protos.pl_types.TagOrBuilder tag) {
    Tag dao = translateToDao(tag, new Tag().setCreationTime(Instant.now()));
    return dao;
  }

  public static Optional<org.davincischools.leo.protos.pl_types.Tag.Builder> toTagProto(
      Tag tag, Supplier<org.davincischools.leo.protos.pl_types.Tag.Builder> newBuilder) {
    return translateToProto(tag, newBuilder);
  }

  private static <M extends MessageOrBuilder, D> D translateToDao(
      M fromMessage, D toDao, int... ignoreFieldNumbers) {
    checkNotNull(fromMessage);
    checkNotNull(toDao);

    Class<? extends MessageOrBuilder> protoClass = fromMessage.getClass();
    Descriptor protoDescriptor = fromMessage.getDescriptorForType();
    Class<?> daoClass = Hibernate.getClass(toDao);

    Set<Integer> ignoredFieldNumbers =
        Arrays.stream(ignoreFieldNumbers).boxed().collect(Collectors.toSet());

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
                                Hibernate.getClass(innerDao)
                                    .getMethod("setId", Integer.class)
                                    .invoke(innerDao, (Integer) message.getField(field));
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

                    throw new IOException("Cannot map the following field: " + field.getFullName());
                  }

                  // Check for an enum field, it needs to be translated to a string.
                  if (field.getType() == FieldDescriptor.Type.ENUM) {
                    setters.put(
                        field.getNumber(),
                        (message, dao) -> {
                          try {
                            if (message.hasField(field)) {
                              var enumValue = ((EnumValueDescriptor) message.getField(field));
                              setMethod
                                  .get()
                                  .invoke(
                                      dao, enumValue.getNumber() != 0 ? enumValue.getName() : null);
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
        daoSetters.get(field.getNumber()).accept(fromMessage, toDao);
      }
    }

    return toDao;
  }

  private static <M extends Message.Builder, D> Optional<M> translateToProto(
      D fromDao, Supplier<M> toMessageSupplier, int... ignoreFieldNumbers) {
    return translateToProto(fromDao, toMessageSupplier, (builder) -> {}, ignoreFieldNumbers);
  }

  private static <M extends Message.Builder, D> Optional<M> translateToProto(
      D fromDao,
      Supplier<M> toMessageSupplier,
      Consumer<M> customTranslations,
      int... ignoreFieldNumbers) {
    checkNotNull(toMessageSupplier);

    if (fromDao == null) {
      return Optional.empty();
    }

    final Class<?> daoClass;
    LazyInitializer lazyInitializer = HibernateProxy.extractLazyInitializer(fromDao);
    if (lazyInitializer != null) {
      daoClass = lazyInitializer.getPersistentClass();
    } else {
      daoClass = fromDao.getClass();
    }

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

    Set<Integer> ignoredFieldNumbers =
        Arrays.stream(ignoreFieldNumbers).boxed().collect(Collectors.toSet());

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
                        throw new IOException(
                            "Id field is not an intereger: " + field.getFullName());
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
                            String daoValue = (String) getMethod.get().invoke(dao);
                            if (daoValue != null) {
                              var enumValue = field.getEnumType().findValueByName(daoValue);
                              if (enumValue == null) {
                                throw new RuntimeException(
                                    "Enum value does not exist: " + daoValue);
                              }
                              message.setField(field, enumValue);
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

  @SuppressWarnings("unchecked")
  private static <T> T valueOrNull(MessageOrBuilder message, int fieldNumber) {
    checkNotNull(message);

    FieldDescriptor descriptor = message.getDescriptorForType().findFieldByNumber(fieldNumber);
    if (message.hasField(descriptor)) {
      return (T) message.getField(descriptor);
    }
    return null;
  }
}
