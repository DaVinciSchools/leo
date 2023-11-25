package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;
import static org.davincischools.leo.database.utils.DaoUtils.saveJoinTableAndTargets;

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
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.query_helper.Entity;
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
        .query(Assignment.class, assignment -> configureQuery(assignment, params));
  }

  public static void configureQuery(Entity<?, Assignment> assignment, GetAssignmentsParams params) {
    checkNotNull(assignment);
    checkNotNull(params);

    assignment.notDeleted().fetch().requireId(params.getAssignmentIds());

    var classX =
        assignment.supplier(
            () -> assignment.join(Assignment_.classX, JoinType.LEFT).notDeleted(),
            params.getClassXIds());

    // var teacher =
    assignment.supplier(
        () ->
            classX
                .get()
                .join(
                    ClassX_.teacherClassXES,
                    JoinType.LEFT,
                    TeacherClassX::getClassX,
                    ClassX::setTeacherClassXES)
                .notDeleted()
                .join(TeacherClassX_.teacher, JoinType.LEFT)
                .notDeleted(),
        params.getTeacherIds());

    // var student =
    assignment.supplier(
        () ->
            classX
                .get()
                .join(
                    ClassX_.studentClassXES,
                    JoinType.LEFT,
                    StudentClassX::getClassX,
                    ClassX::setStudentClassXES)
                .notDeleted()
                .join(StudentClassX_.student, JoinType.LEFT)
                .notDeleted(),
        params.getStudentIds());

    if (params.getIncludeClassXs().isPresent()) {
      ClassXRepository.configureQuery(classX.get(), params.getIncludeClassXs().get());
    }

    if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
      assignment
          .join(
              Assignment_.assignmentKnowledgeAndSkills,
              JoinType.LEFT,
              AssignmentKnowledgeAndSkill::getAssignment,
              Assignment::setAssignmentKnowledgeAndSkills)
          .notDeleted()
          .join(AssignmentKnowledgeAndSkill_.knowledgeAndSkill, JoinType.LEFT)
          .notDeleted()
          .fetch();
    }

    if (params.getIncludeProjectDefinitions().isPresent()) {
      ProjectDefinitionRepository.configureQuery(
          assignment
              .join(
                  Assignment_.assignmentProjectDefinitions,
                  JoinType.LEFT,
                  AssignmentProjectDefinition::getAssignment,
                  Assignment::setAssignmentProjectDefinitions)
              .notDeleted()
              .join(AssignmentProjectDefinition_.projectDefinition, JoinType.LEFT),
          params.getIncludeProjectDefinitions().get());
    }
  }
}
