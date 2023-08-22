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
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
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
          LEFT JOIN TeacherClassX tcx
          ON (:teacherId) IS NOT NULL AND c.id = tcx.classX.id
          LEFT JOIN StudentClassX scx
          ON (:studentId) IS NOT NULL AND c.id = scx.classX.id
          LEFT JOIN ClassXKnowledgeAndSkill cxks
          ON (c.id = cxks.classX.id)
          LEFT JOIN FETCH cxks.knowledgeAndSkill
          WHERE ((:teacherId) IS NULL OR tcx.teacher.id = (:teacherId))
          AND ((:studentId) IS NULL OR scx.student.id = (:studentId))
          ORDER BY c.id""")
  List<FullClassXRow> findFullClassXRowsByUserXId(
      @Nullable @Param("teacherId") Integer teacherId,
      @Nullable @Param("studentId") Integer studentId);

  default List<FullClassX> findFullClassXsByUserXId(
      @Nullable @Param("teacherId") Integer teacherId,
      @Nullable @Param("studentId") Integer studentId) {
    return toFullClassX(findFullClassXRowsByUserXId(teacherId, studentId));
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

    // TODO: Find a better way to "clone without transient children".
    School oldSchool = fullClassX.classX().getSchool();
    try {
      if (fullClassX.classX().getSchool() != null) {
        fullClassX.classX().setSchool(new School().setId(fullClassX.classX().getSchool().getId()));
      }

      save(fullClassX.classX());
      db.getClassXKnowledgeAndSkillRepository()
          .setClassXKnoweldgeAndSkills(fullClassX.classX(), fullClassX.knowledgeAndSkills());
    } finally {
      fullClassX.classX().setSchool(oldSchool);
    }
  }
}
