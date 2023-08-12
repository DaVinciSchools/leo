package org.davincischools.leo.server.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ClassXRepository.FullClassX;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository.FullProjectDefinition;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.FullProjectInput;
import org.davincischools.leo.database.utils.repos.ProjectRepository.MilestoneWithSteps;
import org.davincischools.leo.database.utils.repos.ProjectRepository.ProjectWithMilestones;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Type;
import org.davincischools.leo.protos.pl_types.Project.ThumbsState;
import org.davincischools.leo.protos.pl_types.ProjectDefinition.State;
import org.davincischools.leo.protos.pl_types.ProjectInputValue;
import org.davincischools.leo.protos.user_management.FullUserDetails;

public class DataAccess {

  @SafeVarargs
  public static <T> T coalesce(Callable<T>... values) throws NullPointerException {
    return Stream.of(values)
        .map(
            value -> {
              try {
                return value.call();
              } catch (Exception e) {
                return null;
              }
            })
        .filter(Objects::nonNull)
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  public static org.davincischools.leo.protos.pl_types.User convertFullUserXToProto(UserX user) {
    var userProto = org.davincischools.leo.protos.pl_types.User.newBuilder();
    if (user.getId() != null) {
      userProto
          .setUserXId(user.getId())
          .setFirstName(user.getFirstName())
          .setLastName(user.getLastName())
          .setEmailAddress(user.getEmailAddress());
    }
    if (UserXRepository.isAdmin(user)) {
      userProto.setIsAdmin(true).setAdminId(user.getAdminX().getId());
    }
    if (UserXRepository.isTeacher(user)) {
      userProto.setIsTeacher(true).setTeacherId(user.getTeacher().getId());
    }
    if (UserXRepository.isStudent(user)) {
      userProto.setIsStudent(true).setStudentId(user.getStudent().getId());
    }
    if (UserXRepository.isDemo(user)) {
      userProto.setIsDemo(true);
    }
    if (UserXRepository.isAuthenticated(user)) {
      userProto.setIsAuthenticated(true);
    }
    if (user.getDistrict() != null && user.getDistrict().getId() != null) {
      userProto.setDistrictId(user.getDistrict().getId());
    }
    return userProto.build();
  }

  public static FullUserDetails convertFullUserXToDetailsProto(UserX userX) {
    var proto = FullUserDetails.newBuilder().setUser(convertFullUserXToProto(userX));
    if (UserXRepository.isStudent(userX)) {
      if (userX.getStudent().getDistrictStudentId() != null) {
        proto.setDistrictStudentId(userX.getStudent().getDistrictStudentId());
      }
      if (userX.getStudent().getGrade() != null) {
        proto.setStudentGrade(userX.getStudent().getGrade());
      }
    }
    return proto.build();
  }

  public static List<org.davincischools.leo.protos.pl_types.User> getProtoFullUserXsByDistrictId(
      Database db, int districtId) {
    return StreamSupport.stream(
            db.getUserXRepository().findAllByDistrictId(districtId).spliterator(), false)
        .map(DataAccess::convertFullUserXToProto)
        .collect(Collectors.toList());
  }

  public static org.davincischools.leo.protos.pl_types.School convertSchoolToProto(School school) {
    return org.davincischools.leo.protos.pl_types.School.newBuilder()
        .setId(coalesce(school::getId, () -> -1))
        .setDistrictId(coalesce(school.getDistrict()::getId, () -> -1))
        .setName(school.getName())
        .setAddress(Strings.nullToEmpty(school.getAddress()))
        .build();
  }

  public static List<org.davincischools.leo.protos.pl_types.School> getProtoSchoolsByDistrictId(
      Database db, int districtId) {
    return StreamSupport.stream(
            db.getSchoolRepository().findAllByDistrictId(districtId).spliterator(), false)
        .map(DataAccess::convertSchoolToProto)
        .collect(Collectors.toList());
  }

  public static org.davincischools.leo.protos.pl_types.ClassX.Builder toFullClassXProto(
      FullClassX fullClassX) {
    var classX = fullClassX.classX();
    return org.davincischools.leo.protos.pl_types.ClassX.newBuilder()
        .setId(classX.getId())
        .setName(classX.getName())
        .setNumber(classX.getNumber())
        .setGrade(coalesce(classX::getGrade, () -> ""))
        .setPeriod(coalesce(classX::getPeriod, () -> ""))
        .setShortDescr(coalesce(classX::getShortDescr, () -> ""))
        .setLongDescrHtml(coalesce(classX::getLongDescrHtml, () -> ""))
        .addAllKnowledgeAndSkills(
            fullClassX.knowledgeAndSkills().stream()
                .map(DataAccess::toKnowledgeAndSkillProto)
                .map(org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder::build)
                .toList());
  }

  public static org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder
      toKnowledgeAndSkillProto(KnowledgeAndSkill knowledgeAndSkill) {
    return org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.newBuilder()
        .setId(knowledgeAndSkill.getId())
        .setType(Type.valueOf(knowledgeAndSkill.getType()))
        .setName(knowledgeAndSkill.getName())
        .setCategory(coalesce(knowledgeAndSkill::getCategory, () -> ""))
        .setShortDescr(coalesce(knowledgeAndSkill::getShortDescr, () -> ""))
        .setLongDescrHtml(coalesce(knowledgeAndSkill::getLongDescrHtml, () -> ""))
        .setGlobal(Boolean.TRUE.equals(knowledgeAndSkill.getGlobal()))
        .setUserXId(knowledgeAndSkill.getUserX().getId());
  }

  public static org.davincischools.leo.protos.pl_types.Assignment convertAssignmentToProto(
      ClassX classX, Assignment assignment) {
    return org.davincischools.leo.protos.pl_types.Assignment.newBuilder()
        .setId(coalesce(assignment::getId, () -> -1))
        .setName(assignment.getName())
        .setShortDescr(coalesce(assignment::getShortDescr, () -> ""))
        .setLongDescrHtml(coalesce(assignment::getLongDescrHtml, () -> ""))
        .setClassX(DataAccess.toFullClassXProto(new FullClassX(classX, new ArrayList<>())))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder
      convertFullProjectDefinition(FullProjectDefinition fullProjectDefinition) {
    var projectDefinitionProto =
        org.davincischools.leo.protos.pl_types.ProjectDefinition.newBuilder()
            .setId(fullProjectDefinition.definition().getId())
            .setName(fullProjectDefinition.definition().getName())
            .setTemplate(Boolean.TRUE.equals(fullProjectDefinition.definition().getTemplate()));
    for (var categoryDao : fullProjectDefinition.categories()) {
      var type = categoryDao.getProjectDefinitionCategoryType();
      createProjectInputValueProto(
          categoryDao.getId(), type, projectDefinitionProto.addInputsBuilder());
    }
    return projectDefinitionProto;
  }

  public static org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder
      convertFullProjectInput(FullProjectInput fullProjectInput) {
    var projectDefinitionProto =
        org.davincischools.leo.protos.pl_types.ProjectDefinition.newBuilder()
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
    return projectDefinitionProto;
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

  public static org.davincischools.leo.protos.pl_types.Project convertProjectToProto(
      Project project) {
    return org.davincischools.leo.protos.pl_types.Project.newBuilder()
        .setId(coalesce(project::getId, () -> -1))
        .setName(project.getName())
        .setShortDescr(coalesce(project::getShortDescr, () -> ""))
        .setLongDescrHtml(coalesce(project::getLongDescrHtml, () -> ""))
        .setFavorite(Boolean.TRUE.equals(project.getFavorite()))
        .setThumbsState(
            ThumbsState.valueOf(coalesce(project::getThumbsState, ThumbsState.UNSET::name)))
        .setThumbsStateReason(coalesce(project::getThumbsStateReason, () -> ""))
        .setArchived(Boolean.TRUE.equals(project.getArchived()))
        .setActive(Boolean.TRUE.equals(project.getActive()))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project.Milestone
      convertProjectMilestoneToProto(ProjectMilestone projectMilestone) {
    return org.davincischools.leo.protos.pl_types.Project.Milestone.newBuilder()
        .setId(projectMilestone.getId())
        .setName(projectMilestone.getName())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project.Milestone.Step
      convertProjectMilestoneStepToProto(ProjectMilestoneStep projectMilestoneStep) {
    return org.davincischools.leo.protos.pl_types.Project.Milestone.Step.newBuilder()
        .setId(projectMilestoneStep.getId())
        .setName(projectMilestoneStep.getName())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project.Milestone
      convertMilestoneWithStepsToProto(MilestoneWithSteps milestone) {
    return convertProjectMilestoneToProto(milestone.milestone()).toBuilder()
        .addAllSteps(
            milestone.steps().stream().map(DataAccess::convertProjectMilestoneStepToProto).toList())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project convertProjectWithMilestonesToProto(
      ProjectWithMilestones projectWithMilestones) {
    return convertProjectToProto(projectWithMilestones.project()).toBuilder()
        .addAllMilestones(
            projectWithMilestones.milestones().stream()
                .map(DataAccess::convertMilestoneWithStepsToProto)
                .toList())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.ProjectPost convertProjectPostToProto(
      ProjectPost projectPost) {
    return org.davincischools.leo.protos.pl_types.ProjectPost.newBuilder()
        .setUser(convertFullUserXToProto(projectPost.getUserX()))
        .setName(projectPost.getName())
        .setMessageHtml(projectPost.getMessageHtml())
        .setPostEpochSec((int) projectPost.getCreationTime().getEpochSecond())
        .build();
  }

  public static KnowledgeAndSkill toDao(
      org.davincischools.leo.protos.pl_types.KnowledgeAndSkill p) {
    return translateToDao(p, new KnowledgeAndSkill().setCreationTime(Instant.now()));
  }

  private record ProtoDaoFields(
      Class<? extends Message> protoClass, Class<?> daoClass, Set<Integer> ignoredFieldNumbers) {}

  private static final Map<
          ProtoDaoFields, Map<Integer, BiConsumer</* message= */ Message, /* dao= */ Object>>>
      protoToDaoSetters = Collections.synchronizedMap(new HashMap<>());

  // TODO: This needs to be code generated at compile time with compile time errors. It's
  // dangerous that fields could be overlooked.
  private static <M extends Message, D> D translateToDao(
      M fromMessage, D toDao, int... ignoreFieldNumbers) {
    checkNotNull(fromMessage);
    checkNotNull(toDao);

    Class<? extends Message> protoClass = fromMessage.getClass();
    Descriptor protoDescriptor = fromMessage.getDescriptorForType();
    Class<?> daoClass = toDao.getClass();

    Set<Integer> ignoredFieldNumbers =
        Arrays.stream(ignoreFieldNumbers).boxed().collect(Collectors.toSet());

    Map<Integer, BiConsumer</* message= */ Message, /* dao= */ Object>> daoSetters =
        protoToDaoSetters.computeIfAbsent(
            new ProtoDaoFields(protoClass, daoClass, ignoredFieldNumbers),
            (protoDao) -> {
              Map<Integer, BiConsumer</* message= */ Message, /* dao= */ Object>> setters =
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
                  Optional<Method> setMethod =
                      Optional.ofNullable(setMethods.get(toDaoSetMethod(field)));

                  // Check for a proto ID field that maps to a dao foreign object field.
                  if (setMethod.isEmpty()) {
                    // Make sure it's an ID field.
                    if (!field.getName().endsWith("_id")
                        || field.getType() != FieldDescriptor.Type.INT32) {
                      continue;
                    }

                    // Make sure the dao has a setter for a foreign object.
                    Optional<Method> setDaoMethod =
                        Optional.ofNullable(setMethods.get(toDaoSetDaoMethod(field)));
                    if (setDaoMethod.isEmpty()) {
                      continue;
                    }

                    // Get supposed dao type.
                    Class<?> innerDaoType = setDaoMethod.get().getParameterTypes()[0];
                    Constructor<?>[] constructors = innerDaoType.getConstructors();
                    if (constructors.length != 1) {
                      continue;
                    }
                    if (constructors[0].getParameterTypes().length != 0) {
                      continue;
                    }

                    // Add translator to set the inner dao object.
                    setters.put(
                        field.getNumber(),
                        (message, dao) -> {
                          try {
                            Object innerDao = constructors[0].newInstance();
                            innerDao
                                .getClass()
                                .getMethod("setId", Integer.class)
                                .invoke(innerDao, (Integer) message.getField(field));
                            setDaoMethod.get().invoke(dao, innerDao);
                          } catch (Exception e) {
                            throw new RuntimeException(
                                "Error processing proto field as id to inner dao value: "
                                    + field.getFullName(),
                                e);
                          }
                        });
                    continue;
                  }

                  // Check for an enum field, it needs to be translated to a string.
                  if (field.getType() == FieldDescriptor.Type.ENUM) {
                    setters.put(
                        field.getNumber(),
                        (message, dao) -> {
                          try {
                            setMethod
                                .get()
                                .invoke(
                                    dao, ((EnumValueDescriptor) message.getField(field)).getName());
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
                          setMethod.get().invoke(dao, message.getField(field));
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

  private static String toDaoSetMethod(FieldDescriptor field) {
    checkNotNull(field);

    return "set" + capitalizeFirst(field.getJsonName());
  }

  private static String toDaoSetDaoMethod(FieldDescriptor field) {
    checkNotNull(field);
    checkArgument(field.getName().endsWith("_id"));

    return "set" + capitalizeFirst(field.getJsonName().replaceFirst("Id$", ""));
  }

  private static String capitalizeFirst(String value) {
    checkNotNull(value);

    if (value.isEmpty()) {
      return value;
    }

    return value.substring(0, 1).toUpperCase(Locale.US) + value.substring(1);
  }

  @SuppressWarnings("unchecked")
  private static <T> T valueOrNull(Message message, int fieldNumber) {
    checkNotNull(message);

    FieldDescriptor descriptor = message.getDescriptorForType().findFieldByNumber(fieldNumber);
    if (message.hasField(descriptor)) {
      return (T) message.getField(descriptor);
    }
    return null;
  }
}
