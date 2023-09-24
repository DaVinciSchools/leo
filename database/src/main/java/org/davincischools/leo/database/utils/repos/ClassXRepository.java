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
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.School_;
import org.davincischools.leo.database.daos.StudentClassX_;
import org.davincischools.leo.database.daos.Student_;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.daos.Teacher_;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXRepository extends JpaRepository<ClassX, Integer> {

  default ClassX upsert(ClassX classX) {
    checkNotNull(classX);

    return removeTransientValues(classX, this::save);
  }

  @Query("SELECT c FROM ClassX c WHERE c.school.id = (:schoolId) AND c.name = (:name)")
  Optional<ClassX> findByName(@Param("schoolId") int schoolId, @Param("name") String name);

  @Transactional
  default void guardedUpsert(Database db, ClassX classX, Integer requiredTeacherId)
      throws UnauthorizedUserX {
    checkNotNull(db);
    checkNotNull(classX);

    if (classX.getId() != null
        && requiredTeacherId != null
        && !db.getTeacherClassXRepository()
            .canTeacherUpdateClassX(new Teacher().setId(requiredTeacherId), classX)) {
      throw new UnauthorizedUserX("Teacher does not have write access to this class.");
    }

    removeTransientValues(classX, this::save);
    saveJoinTableAndTargets(
        classX,
        ClassX::getClassXKnowledgeAndSkills,
        ClassXKnowledgeAndSkill::getKnowledgeAndSkill,
        db.getKnowledgeAndSkillRepository()::save,
        db.getClassXKnowledgeAndSkillRepository()::setClassXKnowledgeAndSkills);
  }

  default List<ClassX> getClassXs(EntityManager entityManager, GetClassXsParams params) {
    checkNotNull(entityManager);
    checkNotNull(params);

    var builder = entityManager.getCriteriaBuilder();
    var where = new ArrayList<Predicate>();

    // From classX.
    var query = builder.createQuery(ClassX.class);
    var classX = notDeleted(where, query.from(ClassX.class));

    // Build query.
    getClassXs(params, classX, where);

    // Select.
    query.select(classX).distinct(true).where(where.toArray(new Predicate[0]));
    return entityManager.createQuery(query).getResultList();
  }

  static From<?, ClassX> getClassXs(
      GetClassXsParams params, From<?, ClassX> classX, List<Predicate> where) {
    checkNotNull(params);
    checkNotNull(classX);
    checkNotNull(where);

    // includeSchool.
    if (params.getIncludeSchool().orElse(false)) {
      var school = notDeleted(classX.fetch(ClassX_.school, JoinType.LEFT));
      notDeleted(school.fetch(School_.district, JoinType.LEFT));
    }

    // includeAssignments.
    if (params.getIncludeAssignments().orElse(false)) {
      AssignmentRepository.getAssignments(
          new GetAssignmentsParams()
              .setIncludeKnowledgeAndSkills(params.getIncludeKnowledgeAndSkills().orElse(false)),
          notDeleted(classX.fetch(ClassX_.assignments, JoinType.LEFT)),
          where);
    }

    // includeKnowledgeAndSkills.
    if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
      var classXKnowledgeAndSkills =
          notDeleted(classX.fetch(ClassX_.classXKnowledgeAndSkills, JoinType.LEFT));
      notDeleted(
          classXKnowledgeAndSkills.fetch(
              ClassXKnowledgeAndSkill_.knowledgeAndSkill, JoinType.LEFT));
    }

    // schoolIds.
    if (params.getSchoolIds().isPresent()) {
      where.add(
          classX
              .get(ClassX_.school)
              .get(School_.id)
              .in(ImmutableList.copyOf(params.getSchoolIds().get())));
    }

    // classXIds.
    if (params.getClassXIds().isPresent()) {
      where.add(classX.get(ClassX_.id).in(ImmutableList.copyOf(params.getClassXIds().get())));
    }

    // teacherIds.
    if (params.getTeacherIds().isPresent()) {
      var teacherClassXs = notDeleted(classX.fetch(ClassX_.teacherClassXES, JoinType.INNER));
      notDeleted(teacherClassXs.get(TeacherClassX_.teacher))
          .get(Teacher_.id)
          .in(ImmutableList.copyOf(params.getTeacherIds().get()));
    }

    // Where student ids.
    if (params.getStudentIds().isPresent()) {
      var studentClassXs = notDeleted(classX.fetch(ClassX_.studentClassXES, JoinType.INNER));
      notDeleted(studentClassXs.get(StudentClassX_.student))
          .get(Student_.id)
          .in(ImmutableList.copyOf(params.getStudentIds().get()));
    }

    return classX;
  }
}
