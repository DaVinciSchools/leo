package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.notDeleted;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;
import static org.davincischools.leo.database.utils.DaoUtils.saveJoinTableAndTargets;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.Assignment_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.StudentClassX_;
import org.davincischools.leo.database.daos.Student_;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.daos.Teacher_;
import org.davincischools.leo.database.utils.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

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

  default List<Assignment> getAssignments(
      EntityManager entityManager, GetAssignmentsParams params) {
    checkNotNull(entityManager);
    checkNotNull(params);

    var builder = entityManager.getCriteriaBuilder();
    var where = new ArrayList<Predicate>();

    // From assignment.
    var query = builder.createQuery(Assignment.class);
    var assignment = notDeleted(where, query.from(Assignment.class));

    // Build query.
    getAssignments(params, assignment, where);

    // Select.
    query.select(assignment).where(where.toArray(new Predicate[0]));
    return entityManager.createQuery(query).getResultList();
  }

  static From<?, Assignment> getAssignments(
      GetAssignmentsParams params, From<?, Assignment> assignment, List<Predicate> where) {
    checkNotNull(params);
    checkNotNull(assignment);
    checkNotNull(where);

    // includeClassXs.
    if (params.getIncludeClassXs().orElse(false)) {
      ClassXRepository.getClassXs(
          new GetClassXsParams()
              .setIncludeKnowledgeAndSkills(params.getIncludeKnowledgeAndSkills().orElse(false)),
          notDeleted(assignment.fetch(Assignment_.classX, JoinType.LEFT)),
          where);
    }

    // includeKnowledgeAndSkills.
    if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
      var assignmentKnowledgeAndSkills =
          notDeleted(assignment.fetch(Assignment_.assignmentKnowledgeAndSkills, JoinType.LEFT));
      notDeleted(
          assignmentKnowledgeAndSkills.fetch(
              AssignmentKnowledgeAndSkill_.knowledgeAndSkill, JoinType.LEFT));
    }

    // assignmentIds.
    if (params.getAssignmentIds().isPresent()) {
      where.add(
          assignment.get(Assignment_.id).in(ImmutableList.copyOf(params.getAssignmentIds().get())));
    }

    // classXIds.
    if (params.getClassXIds().isPresent()) {
      where.add(
          assignment
              .get(Assignment_.classX)
              .get(ClassX_.id)
              .in(ImmutableList.copyOf(params.getClassXIds().get())));
    }

    // teacherIds.
    if (params.getTeacherIds().isPresent()) {
      var classXs = notDeleted(assignment.fetch(Assignment_.classX, JoinType.INNER));
      var teacherClassXs = notDeleted(classXs.fetch(ClassX_.teacherClassXES, JoinType.INNER));
      notDeleted(teacherClassXs.fetch(TeacherClassX_.teacher))
          .get(Teacher_.id)
          .in(ImmutableList.copyOf(params.getTeacherIds().get()));
    }

    // studentIds.
    if (params.getStudentIds().isPresent()) {
      var classXs = notDeleted(assignment.fetch(Assignment_.classX, JoinType.INNER));
      var studentClassXs = notDeleted(classXs.fetch(ClassX_.studentClassXES, JoinType.INNER));
      notDeleted(studentClassXs.fetch(StudentClassX_.student))
          .get(Student_.id)
          .in(ImmutableList.copyOf(params.getStudentIds().get()));
    }

    return assignment;
  }
}
