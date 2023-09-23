package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.notDeleted;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;
import static org.davincischools.leo.database.utils.DaoUtils.saveJoinTableAndTargets;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.Assignment_;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.School;
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

  default ClassX upsert(School school, String name, Consumer<ClassX> modifier) {
    checkNotNull(school);
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(modifier);

    ClassX classX =
        findByName(school.getId(), name)
            .orElseGet(() -> new ClassX().setCreationTime(Instant.now()))
            .setSchool(school)
            .setName(name);

    modifier.accept(classX);

    return saveAndFlush(classX);
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

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    List<Predicate> where = new ArrayList<>();

    // From classX.
    CriteriaQuery<ClassX> query = builder.createQuery(ClassX.class);
    Root<ClassX> classX = query.from(ClassX.class);
    where.add(builder.isNull(classX.get(ClassX_.deleted)));

    // Fetch include school.
    if (params.getIncludeSchool().orElse(false)) {
      var school = notDeleted(classX.fetch(ClassX_.school, JoinType.LEFT));
      notDeleted(school.fetch(School_.district, JoinType.LEFT));
    }

    // Fetch include assignments.
    if (params.getIncludeAssignments().orElse(false)) {
      var assignment = notDeleted(classX.fetch(ClassX_.assignments, JoinType.LEFT));

      // Fetch include knowledge and skills.
      if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
        var assignmentKnowledgeAndSkills =
            notDeleted(assignment.fetch(Assignment_.assignmentKnowledgeAndSkills, JoinType.LEFT));
        notDeleted(
            assignmentKnowledgeAndSkills.fetch(
                AssignmentKnowledgeAndSkill_.knowledgeAndSkill, JoinType.LEFT));
      }
    }

    // Fetch include knowledge and skills.
    if (params.getIncludeKnowledgeAndSkills().orElse(false)) {
      var classXKnowledgeAndSkills =
          notDeleted(classX.fetch(ClassX_.classXKnowledgeAndSkills, JoinType.LEFT));
      notDeleted(
          classXKnowledgeAndSkills.fetch(
              ClassXKnowledgeAndSkill_.knowledgeAndSkill, JoinType.LEFT));
    }

    // Where school ids.
    if (params.getSchoolIds().isPresent()) {
      where.add(
          classX
              .get(ClassX_.school)
              .get(School_.id)
              .in(ImmutableList.copyOf(params.getSchoolIds().get())));
    }

    // Where classX ids.
    if (params.getClassXIds().isPresent()) {
      where.add(classX.get(ClassX_.id).in(ImmutableList.copyOf(params.getClassXIds().get())));
    }

    // Where teacher ids.
    if (params.getTeacherIds().isPresent()) {
      var teacherClassXs = notDeleted(classX.fetch(ClassX_.teacherClassXES, JoinType.RIGHT));
      notDeleted(teacherClassXs.get(TeacherClassX_.teacher))
          .get(Teacher_.id)
          .in(ImmutableList.copyOf(params.getTeacherIds().get()));
    }

    // Where student ids.
    if (params.getStudentIds().isPresent()) {
      var studentClassXs = notDeleted(classX.fetch(ClassX_.studentClassXES, JoinType.RIGHT));
      notDeleted(studentClassXs.get(StudentClassX_.student))
          .get(Student_.id)
          .in(ImmutableList.copyOf(params.getStudentIds().get()));
    }

    // Select.
    query.select(classX).where(where.toArray(new Predicate[0]));
    return entityManager.createQuery(query).getResultList();
  }
}
