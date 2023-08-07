package org.davincischools.leo.server.utils;

import com.google.common.base.Strings;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository.FullProjectDefinition;
import org.davincischools.leo.database.utils.repos.ProjectRepository.MilestoneWithSteps;
import org.davincischools.leo.database.utils.repos.ProjectRepository.ProjectWithMilestones;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.davincischools.leo.protos.pl_types.Project.ThumbsState;
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

  public static org.davincischools.leo.protos.pl_types.ClassX convertClassToProto(ClassX classX) {
    return org.davincischools.leo.protos.pl_types.ClassX.newBuilder()
        .setId(coalesce(classX::getId, () -> -1))
        .setName(classX.getName())
        .setShortDescr(coalesce(classX::getShortDescr, () -> ""))
        .setLongDescrHtml(coalesce(classX::getLongDescrHtml, () -> ""))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Assignment convertAssignmentToProto(
      ClassX classX, Assignment assignment) {
    return org.davincischools.leo.protos.pl_types.Assignment.newBuilder()
        .setId(coalesce(assignment::getId, () -> -1))
        .setName(assignment.getName())
        .setShortDescr(coalesce(assignment::getShortDescr, () -> ""))
        .setLongDescrHtml(coalesce(assignment::getLongDescrHtml, () -> ""))
        .setClassX(convertClassToProto(classX))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder
      convertFullProjectDefinition(FullProjectDefinition fullProjectdefinition) {
    var projectDefinitionProto =
        org.davincischools.leo.protos.pl_types.ProjectDefinition.newBuilder()
            .setId(fullProjectdefinition.definition().getId())
            .setName(fullProjectdefinition.definition().getName())
            .setTemplate(Boolean.TRUE.equals(fullProjectdefinition.definition().getTemplate()));
    for (var categoryDao : fullProjectdefinition.categories()) {
      var type = categoryDao.getProjectDefinitionCategoryType();
      projectDefinitionProto
          .addInputsBuilder()
          .getCategoryBuilder()
          .setId(categoryDao.getId())
          .setShortDescr(type.getShortDescr())
          .setName(type.getName())
          .setHint(type.getHint())
          .setPlaceholder(type.getInputPlaceholder())
          .setValueType(
              org.davincischools.leo.protos.pl_types.ProjectInputCategory.ValueType.valueOf(
                  categoryDao.getProjectDefinitionCategoryType().getValueType()));
    }
    return projectDefinitionProto;
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

  public static org.davincischools.leo.protos.pl_types.Eks getProtoEks(KnowledgeAndSkill kas) {
    return org.davincischools.leo.protos.pl_types.Eks.newBuilder()
        .setId(kas.getId())
        .setName(kas.getName())
        .setShortDescr(coalesce(kas::getShortDescr, () -> ""))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.XqCompetency orProtoXqCompetency(
      KnowledgeAndSkill kas) {
    return org.davincischools.leo.protos.pl_types.XqCompetency.newBuilder()
        .setId(kas.getId())
        .setName(kas.getName())
        .setShortDescr(coalesce(kas::getShortDescr, () -> ""))
        .build();
  }
}
