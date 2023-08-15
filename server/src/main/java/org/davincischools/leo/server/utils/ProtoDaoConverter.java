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
import com.google.protobuf.MessageOrBuilder;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
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
import org.davincischools.leo.database.utils.repos.ClassXRepository.FullClassX;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository.FullProjectDefinition;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.FullProjectInput;
import org.davincischools.leo.database.utils.repos.ProjectRepository.MilestoneWithSteps;
import org.davincischools.leo.database.utils.repos.ProjectRepository.ProjectWithMilestones;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.davincischools.leo.protos.pl_types.Project.ThumbsState;
import org.davincischools.leo.protos.pl_types.ProjectDefinition.State;
import org.davincischools.leo.protos.pl_types.ProjectInputValue;
import org.davincischools.leo.protos.user_management.FullUserDetails;

public class ProtoDaoConverter {

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

  @SafeVarargs
  private static <T> T coalesce(Callable<T>... values) throws NullPointerException {
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

  public static org.davincischools.leo.protos.pl_types.User toUserProto(UserX user) {
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

  public static FullUserDetails toFullUserDetailsProto(UserX userX) {
    var proto = FullUserDetails.newBuilder().setUser(toUserProto(userX));
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

  public static org.davincischools.leo.protos.pl_types.School toSchoolProto(School school) {
    return org.davincischools.leo.protos.pl_types.School.newBuilder()
        .setId(coalesce(school::getId, () -> -1))
        .setDistrictId(coalesce(school.getDistrict()::getId, () -> -1))
        .setName(school.getName())
        .setAddress(Strings.nullToEmpty(school.getAddress()))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.ProjectDefinition toProjectDefinition(
      FullProjectDefinition fullProjectDefinition) {
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
    return projectDefinitionProto.build();
  }

  public static org.davincischools.leo.protos.pl_types.ProjectDefinition toProjectDefinition(
      FullProjectInput fullProjectInput) {
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
    return projectDefinitionProto.build();
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

  public static org.davincischools.leo.protos.pl_types.Project toProjectProto(Project project) {
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

  public static org.davincischools.leo.protos.pl_types.Project.Milestone toMilestoneProto(
      ProjectMilestone projectMilestone) {
    return org.davincischools.leo.protos.pl_types.Project.Milestone.newBuilder()
        .setId(projectMilestone.getId())
        .setName(projectMilestone.getName())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project.Milestone.Step toMilestoneStepProto(
      ProjectMilestoneStep projectMilestoneStep) {
    return org.davincischools.leo.protos.pl_types.Project.Milestone.Step.newBuilder()
        .setId(projectMilestoneStep.getId())
        .setName(projectMilestoneStep.getName())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project.Milestone toMilestoneProto(
      MilestoneWithSteps milestone) {
    return toMilestoneProto(milestone.milestone()).toBuilder()
        .addAllSteps(
            milestone.steps().stream().map(ProtoDaoConverter::toMilestoneStepProto).toList())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project toProjectProto(
      ProjectWithMilestones projectWithMilestones) {
    return toProjectProto(projectWithMilestones.project()).toBuilder()
        .addAllMilestones(
            projectWithMilestones.milestones().stream()
                .map(ProtoDaoConverter::toMilestoneProto)
                .toList())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.ProjectPost toProjectPostProto(
      ProjectPost projectPost) {
    return org.davincischools.leo.protos.pl_types.ProjectPost.newBuilder()
        .setUser(toUserProto(projectPost.getUserX()))
        .setName(projectPost.getName())
        .setMessageHtml(projectPost.getMessageHtml())
        .setPostEpochSec((int) projectPost.getCreationTime().getEpochSecond())
        .build();
  }

  //
  // ---- Automated and tested converters. ----
  //

  public static org.davincischools.leo.protos.pl_types.Assignment.Builder toAssignmentProto(
      Assignment d, @Nullable org.davincischools.leo.protos.pl_types.Assignment.Builder p) {
    var assignment =
        translateToProto(
            d,
            p != null ? p : org.davincischools.leo.protos.pl_types.Assignment.newBuilder(),
            org.davincischools.leo.protos.pl_types.Assignment.CLASS_X_FIELD_NUMBER);
    toClassXProto(d.getClassX(), assignment.getClassXBuilder());
    return assignment;
  }

  public static ClassX toClassXDao(org.davincischools.leo.protos.pl_types.ClassXOrBuilder p) {
    return translateToDao(
        p,
        new ClassX().setCreationTime(Instant.now()),
        org.davincischools.leo.protos.pl_types.ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER);
  }

  public static FullClassX toFullClassXRecord(
      org.davincischools.leo.protos.pl_types.ClassXOrBuilder p) {
    return new FullClassX(
        toClassXDao(p),
        p.getKnowledgeAndSkillsOrBuilderList().stream()
            .map(ProtoDaoConverter::toKnowledgeAndSkillDao)
            .collect(Collectors.toList()));
  }

  public static org.davincischools.leo.protos.pl_types.ClassX.Builder toClassXProto(
      ClassX d, @Nullable org.davincischools.leo.protos.pl_types.ClassX.Builder p) {
    return translateToProto(
        d,
        p != null ? p : org.davincischools.leo.protos.pl_types.ClassX.newBuilder(),
        org.davincischools.leo.protos.pl_types.ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER);
  }

  public static org.davincischools.leo.protos.pl_types.ClassX.Builder toFullClassXProto(
      FullClassX fullClassX, @Nullable org.davincischools.leo.protos.pl_types.ClassX.Builder p) {
    org.davincischools.leo.protos.pl_types.ClassX.Builder cp =
        toClassXProto(fullClassX.classX(), p);
    fullClassX
        .knowledgeAndSkills()
        .forEach(ks -> toKnowledgeAndSkillProto(ks, cp.addKnowledgeAndSkillsBuilder()));
    return cp;
  }

  public static KnowledgeAndSkill toKnowledgeAndSkillDao(
      org.davincischools.leo.protos.pl_types.KnowledgeAndSkillOrBuilder p) {
    return translateToDao(p, new KnowledgeAndSkill().setCreationTime(Instant.now()));
  }

  public static org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder
      toKnowledgeAndSkillProto(
          KnowledgeAndSkill d,
          @Nullable org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Builder p) {
    return translateToProto(
        d, p != null ? p : org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.newBuilder());
  }

  // TODO: These need to be code generated at compile time with compile time errors. It's
  // dangerous that fields could be overlooked.

  static <M extends MessageOrBuilder, D> D translateToDao(
      M fromMessage, D toDao, int... ignoreFieldNumbers) {
    checkNotNull(fromMessage);
    checkNotNull(toDao);

    Class<? extends MessageOrBuilder> protoClass = fromMessage.getClass();
    Descriptor protoDescriptor = fromMessage.getDescriptorForType();
    Class<?> daoClass = toDao.getClass();

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
                            if (message.hasField(field)) {
                              Object innerDao = constructors[0].newInstance();
                              innerDao
                                  .getClass()
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

                  // Check for an enum field, it needs to be translated to a string.
                  if (field.getType() == FieldDescriptor.Type.ENUM) {
                    setters.put(
                        field.getNumber(),
                        (message, dao) -> {
                          try {
                            if (message.hasField(field)) {
                              setMethod
                                  .get()
                                  .invoke(
                                      dao,
                                      ((EnumValueDescriptor) message.getField(field)).getName());
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

  // TODO: This needs to be code generated at compile time with compile time errors. It's
  // dangerous that fields could be overlooked.
  static <M extends Message.Builder, D> M translateToProto(
      D fromDao, M toMessage, int... ignoreFieldNumbers) {
    checkNotNull(fromDao);
    checkNotNull(toMessage);

    Class<? extends Message.Builder> protoClass = toMessage.getClass();
    Descriptor protoDescriptor = toMessage.getDescriptorForType();
    Class<?> daoClass = fromDao.getClass();

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
                  Optional<Method> getMethod =
                      Optional.ofNullable(getMethods.get(toDaoGetMethod(field)));

                  // Check for a proto ID field that maps to a dao foreign object field.
                  if (getMethod.isEmpty()) {
                    // Make sure it's an ID field.
                    if (!field.getName().endsWith("_id")
                        || field.getType() != FieldDescriptor.Type.INT32) {
                      continue;
                    }

                    // Make sure the dao has a getter for a foreign object.
                    Optional<Method> getDaoMethod =
                        Optional.ofNullable(getMethods.get(toDaoGetDaoMethod(field)));
                    if (getDaoMethod.isEmpty()) {
                      continue;
                    }

                    // Get supposed dao type id.
                    Class<?> daoType = getDaoMethod.get().getReturnType();
                    Method getDaoId = daoType.getMethod("getId");
                    if (getDaoId.getReturnType() != Integer.class) {
                      continue;
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
      protoSetters.get(field.getNumber()).accept(fromDao, toMessage);
    }

    return toMessage;
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

  private static String toDaoGetMethod(FieldDescriptor field) {
    checkNotNull(field);

    return "get" + capitalizeFirst(field.getJsonName());
  }

  private static String toDaoGetDaoMethod(FieldDescriptor field) {
    checkNotNull(field);
    checkArgument(field.getName().endsWith("_id"));

    return "get" + capitalizeFirst(field.getJsonName().replaceFirst("Id$", ""));
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
