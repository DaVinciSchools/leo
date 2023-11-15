package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;
import static org.davincischools.leo.database.utils.DaoUtils.saveJoinTableAndTargets;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.School_;
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.StudentClassX_;
import org.davincischools.leo.database.daos.Student_;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.daos.Teacher_;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.QueryHelper.QueryHelperUtils;
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

    return getQueryHelper()
        .query(ClassX.class, (u, classX, builder) -> configureQuery(u, classX, builder, params));
  }

  public static void configureQuery(
      QueryHelperUtils u,
      From<?, ClassX> classX,
      CriteriaBuilder builder,
      GetClassXsParams params) {
    checkNotNull(u);
    checkNotNull(classX);
    checkNotNull(builder);
    checkNotNull(params);

    u.notDeleted(classX);

    if (params.getIncludeSchool().orElse(false)) {
      var school = u.notDeleted(u.fetch(classX, ClassX_.school, JoinType.LEFT));
      u.notDeleted(u.fetch(school, School_.district, JoinType.LEFT));
    }

    if (params.getIncludeAssignments().isPresent()) {
      var assignment =
          u.notDeleted(
              u.fetch(
                  classX,
                  ClassX_.assignments,
                  JoinType.LEFT,
                  Assignment::getClassX,
                  ClassX::setAssignments));
      AssignmentRepository.configureQuery(
          u, assignment, builder, params.getIncludeAssignments().get());
    }

    if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
      var classXKnowledgeAndSkills =
          u.notDeleted(
              u.fetch(
                  classX,
                  ClassX_.classXKnowledgeAndSkills,
                  JoinType.LEFT,
                  ClassXKnowledgeAndSkill::getClassX,
                  ClassX::setClassXKnowledgeAndSkills));
      u.notDeleted(
          u.fetch(
              classXKnowledgeAndSkills, ClassXKnowledgeAndSkill_.knowledgeAndSkill, JoinType.LEFT));
    }

    if (params.getSchoolIds().isPresent()) {
      u.where(
          classX
              .get(ClassX_.school)
              .get(School_.id)
              .in(ImmutableList.copyOf(params.getSchoolIds().get())));
    }

    if (params.getClassXIds().isPresent()) {
      u.where(classX.get(ClassX_.id).in(ImmutableList.copyOf(params.getClassXIds().get())));
    }

    if (params.getTeacherIds().isPresent()) {
      var teacherClassXs =
          u.notDeleted(
              u.join(
                  classX,
                  ClassX_.teacherClassXES,
                  JoinType.INNER,
                  TeacherClassX::getClassX,
                  ClassX::setTeacherClassXES));
      teacherClassXs
          .get(TeacherClassX_.teacher)
          .get(Teacher_.id)
          .in(ImmutableList.copyOf(params.getTeacherIds().get()));
    }

    // Where student ids.
    if (params.getStudentIds().isPresent()) {
      var studentClassXs =
          u.notDeleted(
              u.join(
                  classX,
                  ClassX_.studentClassXES,
                  JoinType.INNER,
                  StudentClassX::getClassX,
                  ClassX::setStudentClassXES));
      studentClassXs
          .get(StudentClassX_.student)
          .get(Student_.id)
          .in(ImmutableList.copyOf(params.getStudentIds().get()));
    }
  }
}
