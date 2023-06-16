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
import org.davincischools.leo.database.utils.repos.ProjectRepository.MilestoneWithSteps;
import org.davincischools.leo.database.utils.repos.ProjectRepository.ProjectWithMilestones;
import org.davincischools.leo.protos.pl_types.Project.ThumbsState;
import org.davincischools.leo.protos.user_management.FullUserDetails;
import org.davincischools.leo.server.utils.http_user.HttpUser;

public class DataAccess {

  @SafeVarargs
  public static <T> T firstNonNull(Callable<T>... values) throws NullPointerException {
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

  public static String getShortDescr(Object daoWithNameAndShortDescAndLongDescr) {
    return firstNonNull(
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getShortDescr")
                    .invoke(daoWithNameAndShortDescAndLongDescr),
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getName")
                    .invoke(daoWithNameAndShortDescAndLongDescr));
  }

  public static String getLongDescr(Object daoWithNameAndShortDescAndLongDescr) {
    return firstNonNull(
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getLongDescr")
                    .invoke(daoWithNameAndShortDescAndLongDescr),
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getShortDescr")
                    .invoke(daoWithNameAndShortDescAndLongDescr),
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getName")
                    .invoke(daoWithNameAndShortDescAndLongDescr));
  }

  public static String getStepsDescr(Object daoWithNameAndShortDescAndLongDescr) {
    return firstNonNull(
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getStepsDescr")
                    .invoke(daoWithNameAndShortDescAndLongDescr),
        () -> "");
  }

  public static org.davincischools.leo.protos.pl_types.User convertFullUserXToProto(UserX user) {
    var userProto =
        org.davincischools.leo.protos.pl_types.User.newBuilder()
            .setId(firstNonNull(user::getId, () -> -1))
            .setDistrictId(firstNonNull(user.getDistrict()::getId, () -> -1))
            .setFirstName(user.getFirstName())
            .setLastName(user.getLastName())
            .setEmailAddress(user.getEmailAddress())
            .setIsAdmin(HttpUser.isAdmin(user))
            .setIsTeacher(HttpUser.isTeacher(user))
            .setIsStudent(HttpUser.isStudent(user));
    return userProto.build();
  }

  public static FullUserDetails convertFullUserXToDetailsProto(UserX userX) {
    var proto = FullUserDetails.newBuilder().setUser(convertFullUserXToProto(userX));
    if (HttpUser.isStudent(userX)) {
      if (userX.getStudent().getStudentId() != null) {
        proto.setStudentId(userX.getStudent().getStudentId());
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
        .setId(firstNonNull(school::getId, () -> -1))
        .setDistrictId(firstNonNull(school.getDistrict()::getId, () -> -1))
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
        .setId(firstNonNull(classX::getId, () -> -1))
        .setName(classX.getName())
        .setShortDescr(getShortDescr(classX))
        .setLongDescr(getLongDescr(classX))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Assignment convertAssignmentToProto(
      ClassX classX, Assignment assignment) {
    return org.davincischools.leo.protos.pl_types.Assignment.newBuilder()
        .setId(firstNonNull(assignment::getId, () -> -1))
        .setName(assignment.getName())
        .setShortDescr(getShortDescr(assignment))
        .setLongDescr(getLongDescr(assignment))
        .setClassX(convertClassToProto(classX))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Project convertProjectToProto(
      Project project) {
    return org.davincischools.leo.protos.pl_types.Project.newBuilder()
        .setId(firstNonNull(project::getId, () -> -1))
        .setName(project.getName())
        .setShortDescr(getShortDescr(project))
        .setLongDescr(getLongDescr(project))
        .setStepsDescr(getStepsDescr(project))
        .setFavorite(Boolean.TRUE.equals(project.getFavorite()))
        .setThumbsState(
            ThumbsState.valueOf(firstNonNull(project::getThumbsState, ThumbsState.UNSET::name)))
        .setThumbsStateReason(firstNonNull(project::getThumbsStateReason, () -> ""))
        .setArchived(Boolean.TRUE.equals(project.getArchived()))
        .setNeedsReview(Boolean.TRUE.equals(project.getNeedsReview()))
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
        .setTitle(projectPost.getTitle())
        .setMessage(projectPost.getMessage())
        .setPostEpochSec((int) projectPost.getCreationTime().getEpochSecond())
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.Eks getProtoEks(KnowledgeAndSkill kas) {
    return org.davincischools.leo.protos.pl_types.Eks.newBuilder()
        .setId(kas.getId())
        .setName(kas.getName())
        .setShortDescr(getShortDescr(kas))
        .build();
  }

  public static org.davincischools.leo.protos.pl_types.XqCompetency orProtoXqCompetency(
      KnowledgeAndSkill kas) {
    return org.davincischools.leo.protos.pl_types.XqCompetency.newBuilder()
        .setId(kas.getId())
        .setName(kas.getName())
        .setShortDescr(getShortDescr(kas))
        .build();
  }
}
