package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXRepository extends JpaRepository<ClassX, Integer> {

  record FullClassX(ClassX classX, List<KnowledgeAndSkill> knowledgeAndSkills) {}

  record FullClassXRow(ClassX classX, ClassXKnowledgeAndSkill classKnowledgeAndSkill) {}

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

  @Query(
      """
          SELECT c, cxks
          FROM ClassX c
          LEFT JOIN FETCH c.school
          LEFT JOIN FETCH c.school.district

          LEFT JOIN ClassXKnowledgeAndSkill cxks
          ON
              (:includeKnowledgeAndSkills = TRUE
              AND c.id = cxks.classX.id)
          LEFT JOIN FETCH cxks.knowledgeAndSkill

          WHERE c.id IN (
              SELECT c.id
              FROM ClassX c

              LEFT JOIN TeacherClassX tcx
              ON
                  (:teacherId) IS NOT NULL
                  AND c.id = tcx.classX.id
              LEFT JOIN StudentClassX scx
              ON
                  (:studentId) IS NOT NULL
                  AND c.id = scx.classX.id

              LEFT JOIN TeacherSchool ts
              ON
                  (:includeAvailableClassXs) = TRUE
                  AND (:teacherId) IS NOT NULL
                  AND c.school.id = ts.school.id
              LEFT JOIN StudentSchool ss
              ON
                  (:includeAvailableClassXs) = TRUE
                  AND (:studentId) IS NOT NULL
                  AND c.school.id = ss.school.id

              WHERE (
                  (:teacherId) IS NULL
                  OR tcx.teacher.id = (:teacherId)
                  OR ts.teacher.id = (:teacherId))
              AND (
                  (:studentId) IS NULL
                  OR scx.student.id = (:studentId)
                  OR ss.student.id = (:studentId)))
          ORDER BY c.id""")
  List<FullClassXRow> findFullClassXsRows(
      @Nullable @Param("teacherId") Integer teacherId,
      @Nullable @Param("studentId") Integer studentId,
      @Param("includeAvailableClassXs") boolean includeAvailableClassXs,
      @Param("includeKnowledgeAndSkills") boolean includeKnowledgeAndSkills);

  default List<FullClassX> findFullClassXs(
      @Nullable Teacher teacher,
      @Nullable Student student,
      boolean includeAvailableClassXs,
      boolean includeKnowledgeAndSkills) {
    return toFullClassX(
        findFullClassXsRows(
            teacher != null ? teacher.getId() : null,
            student != null ? student.getId() : null,
            includeAvailableClassXs,
            includeKnowledgeAndSkills));
  }

  private List<FullClassX> toFullClassX(Iterable<FullClassXRow> rows) {
    List<FullClassX> fullClassXs = new ArrayList<>();
    FullClassX classX = null;

    for (FullClassXRow row : rows) {
      if (classX == null || !Objects.equals(row.classX.getId(), classX.classX.getId())) {
        fullClassXs.add(classX = new FullClassX(row.classX, new ArrayList<>()));
      }
      if (row.classKnowledgeAndSkill() != null) {
        classX.knowledgeAndSkills.add(row.classKnowledgeAndSkill().getKnowledgeAndSkill());
      }
    }

    return fullClassXs;
  }

  List<ClassX> findAllBySchool(School school);

  @Transactional
  default void guardedUpsert(Database db, FullClassX fullClassX, Integer requiredTeacherId)
      throws UnauthorizedUserX {
    checkNotNull(db);
    checkNotNull(fullClassX);

    if (fullClassX.classX().getId() != null
        && requiredTeacherId != null
        && !db.getTeacherClassXRepository()
            .canTeacherUpdateClassX(requiredTeacherId, fullClassX.classX().getId())) {
      throw new UnauthorizedUserX("Teacher does not have write access to this class.");
    }

    DaoUtils.removeTransientValues(fullClassX.classX(), this::save);
    db.getClassXKnowledgeAndSkillRepository()
        .setClassXKnoweldgeAndSkills(fullClassX.classX(), fullClassX.knowledgeAndSkills());
  }
}
