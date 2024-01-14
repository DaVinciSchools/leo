package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;
import static org.davincischools.leo.database.utils.DaoUtils.saveJoinTableAndTargets;

import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.StudentClassX_;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXRepository
    extends JpaRepository<ClassX, Integer>, AutowiredRepositoryValues {

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

  default List<ClassX> getClassXs(GetClassXsParams params) {
    checkNotNull(params);

    return getQueryHelper().query(ClassX.class, classX -> configureQuery(classX, params));
  }

  static Entity<?, ClassX> configureQuery(Entity<?, ClassX> classX, GetClassXsParams params) {
    checkNotNull(classX);
    checkNotNull(params);

    classX.fetch().requireId(params.getClassXIds());

    var school =
        classX.supplier(
            () -> classX.join(ClassX_.school, JoinType.LEFT).notDeleted(), params.getSchoolIds());

    // var teacher =
    classX.supplier(
        () ->
            classX
                .join(ClassX_.teacherClassXES, JoinType.LEFT)
                .notDeleted()
                .join(TeacherClassX_.teacher, JoinType.LEFT),
        params.getTeacherIds());

    // var student =
    classX.supplier(
        () ->
            classX
                .join(ClassX_.studentClassXES, JoinType.LEFT)
                .notDeleted()
                .join(StudentClassX_.student, JoinType.LEFT),
        params.getStudentIds());

    if (params.getIncludeSchool().orElse(false)) {
      school.get().fetch();
    }

    if (params.getIncludeAssignments().isPresent()) {
      AssignmentRepository.configureQuery(
              classX.join(ClassX_.assignments, JoinType.LEFT), params.getIncludeAssignments().get())
          .notDeleted();
    }

    if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
      classX
          .join(ClassX_.classXKnowledgeAndSkills, JoinType.LEFT)
          .notDeleted()
          .join(ClassXKnowledgeAndSkill_.knowledgeAndSkill, JoinType.LEFT)
          .fetch();
    }

    return classX;
  }
}
