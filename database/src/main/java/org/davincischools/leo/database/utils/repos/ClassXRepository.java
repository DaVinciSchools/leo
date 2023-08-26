package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
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
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXRepository extends JpaRepository<ClassX, Integer> {

  record FullClassX(ClassX classX, boolean enrolled, List<KnowledgeAndSkill> knowledgeAndSkills) {}

  record FullClassXRow(
      ClassX classX,
      TeacherClassX teacherClassX,
      StudentClassX studentClassX,
      ClassXKnowledgeAndSkill classKnowledgeAndSkill) {}

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
          SELECT c, tcx, scx, cxks

          FROM ClassX c
          LEFT JOIN FETCH c.school
          LEFT JOIN FETCH c.school.district

          LEFT JOIN TeacherClassX tcx
          ON
            tcx.teacher = (:teacher)
            AND tcx.classX = c

          LEFT JOIN StudentClassX scx
          ON
            scx.student = (:student)
            AND scx.classX = c

          LEFT JOIN TeacherSchool ts
          ON
            (:includeAllAvailableClassXs) = TRUE
            AND ts.teacher = (:teacher)
            AND ts.school = c.school

          LEFT JOIN StudentSchool ss
          ON
            (:includeAllAvailableClassXs) = TRUE
            AND ss.student = (:student)
            AND ss.school = c.school

          LEFT JOIN ClassXKnowledgeAndSkill cxks
          ON
            (:includeKnowledgeAndSkills) = TRUE
            AND c = cxks.classX
          LEFT JOIN FETCH cxks.knowledgeAndSkill

          WHERE
              c.school IN (:schools)
              OR (
                  ((:teacher) IS NULL OR tcx IS NOT NULL OR ts IS NOT NULL)
                  AND ((:student) IS NULL OR scx IS NOT NULL OR ss IS NOT NULL)
                  AND ((:teacher) IS NOT NULL OR (:student) IS NOT NULL))
          ORDER BY c.id""")
  List<FullClassXRow> findFullClassXsRows(
      @Nullable @Param("teacher") Teacher teacher,
      @Nullable @Param("student") Student student,
      @Param("schools") Iterable<School> schools,
      @Param("includeAllAvailableClassXs") boolean includeAllAvailableClassXs,
      @Param("includeKnowledgeAndSkills") boolean includeKnowledgeAndSkills);

  default List<FullClassX> findFullClassXs(
      @Nullable Teacher teacher,
      @Nullable Student student,
      Iterable<School> schools,
      boolean includeAllAvailableClassXs,
      boolean includeKnowledgeAndSkills) {
    return toFullClassX(
        findFullClassXsRows(
            teacher,
            student,
            ImmutableList.<School>builder().addAll(schools).add(new School().setId(0)).build(),
            includeAllAvailableClassXs,
            includeKnowledgeAndSkills),
        includeAllAvailableClassXs);
  }

  private List<FullClassX> toFullClassX(
      Iterable<FullClassXRow> rows, boolean includeAllAvailableClassXs) {
    List<FullClassX> fullClassXs = new ArrayList<>();
    FullClassX classX = null;

    for (FullClassXRow row : rows) {
      if (classX == null || !Objects.equals(row.classX.getId(), classX.classX.getId())) {
        fullClassXs.add(
            classX =
                new FullClassX(
                    row.classX,
                    !includeAllAvailableClassXs
                        || row.teacherClassX() != null
                        || row.studentClassX() != null,
                    new ArrayList<>()));
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
            .canTeacherUpdateClassX(new Teacher().setId(requiredTeacherId), fullClassX.classX())) {
      throw new UnauthorizedUserX("Teacher does not have write access to this class.");
    }

    DaoUtils.removeTransientValues(fullClassX.classX(), this::save);
    db.getClassXKnowledgeAndSkillRepository()
        .setClassXKnoweldgeAndSkills(fullClassX.classX(), fullClassX.knowledgeAndSkills());
  }
}
