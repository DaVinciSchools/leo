package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;
import static org.davincischools.leo.database.utils.DaoUtils.saveJoinTableAndTargets;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition_;
import org.davincischools.leo.database.daos.Assignment_;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.StudentClassX_;
import org.davincischools.leo.database.daos.Student_;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.daos.Teacher_;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.QueryHelper.QueryHelperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository
    extends JpaRepository<Assignment, Integer>, AutowiredRepositoryValues {

  default Assignment upsert(Database db, Assignment assignment) {
    checkNotNull(assignment);

    removeTransientValues(assignment, this::save);
    saveJoinTableAndTargets(
        assignment,
        Assignment::getAssignmentKnowledgeAndSkills,
        AssignmentKnowledgeAndSkill::getKnowledgeAndSkill,
        db.getKnowledgeAndSkillRepository()::save,
        db.getAssignmentKnowledgeAndSkillRepository()::setAssignmentKnowledgeAndSkills);

    return assignment;
  }

  default List<Assignment> getAssignments(GetAssignmentsParams params) {
    checkNotNull(params);

    return getQueryHelper()
        .query(
            Assignment.class,
            (u, assignment, builder) -> configureQuery(u, assignment, builder, params));
  }

  public static void configureQuery(
      QueryHelperUtils u,
      From<?, Assignment> assignment,
      CriteriaBuilder builder,
      GetAssignmentsParams params) {
    checkNotNull(u);
    checkNotNull(assignment);
    checkNotNull(builder);
    checkNotNull(params);

    u.notDeleted(assignment);

    if (params.getIncludeClassXs().isPresent()) {
      ClassXRepository.configureQuery(
          u,
          u.notDeleted(u.fetch(assignment, Assignment_.classX, JoinType.LEFT)),
          builder,
          params.getIncludeClassXs().get());
    }

    if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
      var assignmentKnowledgeAndSkills =
          u.notDeleted(
              u.fetch(
                  assignment,
                  Assignment_.assignmentKnowledgeAndSkills,
                  JoinType.LEFT,
                  AssignmentKnowledgeAndSkill::getAssignment,
                  Assignment::setAssignmentKnowledgeAndSkills));
      u.notDeleted(
          u.fetch(
              assignmentKnowledgeAndSkills,
              AssignmentKnowledgeAndSkill_.knowledgeAndSkill,
              JoinType.LEFT));
    }

    if (params.getIncludeProjectDefinitions().isPresent()) {
      var assignmentProjectDefinition =
          u.notDeleted(
              u.fetch(
                  assignment,
                  Assignment_.assignmentProjectDefinitions,
                  JoinType.LEFT,
                  AssignmentProjectDefinition::getAssignment,
                  Assignment::setAssignmentProjectDefinitions));

      var projectDefinition =
          u.notDeleted(
              u.fetch(
                  assignmentProjectDefinition,
                  AssignmentProjectDefinition_.projectDefinition,
                  JoinType.LEFT));
      ProjectDefinitionRepository.configureQuery(
          u, projectDefinition, builder, params.getIncludeProjectDefinitions().get());
    }

    if (params.getAssignmentIds().isPresent()) {
      u.where(
          assignment.get(Assignment_.id).in(ImmutableList.copyOf(params.getAssignmentIds().get())));
    }

    if (params.getClassXIds().isPresent()) {
      u.where(
          assignment
              .get(Assignment_.classX)
              .get(ClassX_.id)
              .in(ImmutableList.copyOf(params.getClassXIds().get())));
    }

    if (params.getTeacherIds().isPresent()) {
      var classXs = u.notDeleted(u.join(assignment, Assignment_.classX, JoinType.INNER));
      var teacherClassXs =
          u.notDeleted(
              u.join(
                  classXs,
                  ClassX_.teacherClassXES,
                  JoinType.INNER,
                  TeacherClassX::getClassX,
                  ClassX::setTeacherClassXES));
      u.where(
          teacherClassXs
              .get(TeacherClassX_.teacher)
              .get(Teacher_.id)
              .in(ImmutableList.copyOf(params.getTeacherIds().get())));
    }

    if (params.getStudentIds().isPresent()) {
      var classXs = u.notDeleted(u.join(assignment, Assignment_.classX, JoinType.INNER));
      var studentClassXs =
          u.notDeleted(
              u.join(
                  classXs,
                  ClassX_.studentClassXES,
                  JoinType.INNER,
                  StudentClassX::getClassX,
                  ClassX::setStudentClassXES));
      u.where(
          studentClassXs
              .get(StudentClassX_.student)
              .get(Student_.id)
              .in(ImmutableList.copyOf(params.getStudentIds().get())));
    }
  }
}
