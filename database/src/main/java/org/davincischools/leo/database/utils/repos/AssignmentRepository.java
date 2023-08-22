package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

  record ClassXAssignmentRow(
      Object _ignore_1,
      ClassX classX,
      Assignment assignment,
      Object _ignore_2,
      KnowledgeAndSkill knowledgeAndSkill) {}

  record FullAssignment(Assignment assignment, List<KnowledgeAndSkill> knowledgeAndSkills) {}

  record FullClassXAssignment(ClassX classX, List<FullAssignment> assignments) {}

  default Assignment upsert(ClassX classX, String name, Consumer<Assignment> modifier) {
    checkNotNull(classX);
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(modifier);

    Assignment assignment =
        findByName(classX.getId(), name)
            .orElseGet(() -> new Assignment().setCreationTime(Instant.now()))
            .setClassX(classX)
            .setName(name);

    modifier.accept(assignment);

    return saveAndFlush(assignment);
  }

  /** Note: this includes all classes, even when they have no assignment. */
  @Query(
      "SELECT sc, sc.classX, a, ks, ks.knowledgeAndSkill"
          + " FROM StudentClassX sc"
          + " LEFT JOIN FETCH sc.classX"
          + " LEFT JOIN Assignment a"
          + " ON sc.classX.id = a.classX.id"
          + " LEFT JOIN AssignmentKnowledgeAndSkill ks"
          + " ON a.id = ks.assignment.id"
          + " LEFT JOIN FETCH ks.knowledgeAndSkill"
          + " WHERE sc.student.id = (:studentId)"
          + " ORDER BY sc.classX.id, a.id")
  List<ClassXAssignmentRow> findAllRowsByStudentId(@Param("studentId") int studentId);

  default List<FullClassXAssignment> findAllByStudentId(int studentId) {
    return processClassXAssignmentRows(findAllRowsByStudentId(studentId));
  }

  /** Note: this includes all classes, even when they have no assignment. */
  @Query(
      "SELECT tc, tc.classX, a, ks, ks.knowledgeAndSkill"
          + " FROM TeacherClassX tc"
          + " LEFT JOIN FETCH tc.classX"
          + " LEFT JOIN Assignment a"
          + " ON tc.classX.id = a.classX.id"
          + " LEFT JOIN AssignmentKnowledgeAndSkill ks"
          + " ON a.id = ks.assignment.id"
          + " LEFT JOIN FETCH ks.knowledgeAndSkill"
          + " WHERE tc.teacher.id = (:teacherId)"
          + " ORDER BY tc.classX.id, a.id")
  List<ClassXAssignmentRow> findAllRowsByTeacherId(@Param("teacherId") int teacherId);

  default List<FullClassXAssignment> findAllByTeacherId(int teacherId) {
    return processClassXAssignmentRows(findAllRowsByTeacherId(teacherId));
  }

  private static List<FullClassXAssignment> processClassXAssignmentRows(
      List<ClassXAssignmentRow> rows) {
    List<FullClassXAssignment> fullClassXAssignments = new ArrayList<>();
    FullClassXAssignment fullClassXAssignment = null;
    FullAssignment fullAssignment = null;

    for (var row : rows) {
      if (fullClassXAssignment == null
          || !Objects.equals(row.classX().getId(), fullClassXAssignment.classX().getId())) {
        fullClassXAssignments.add(
            fullClassXAssignment = new FullClassXAssignment(row.classX(), new ArrayList<>()));
        fullAssignment = null;
      }
      if (row.assignment() != null) {
        if (fullAssignment == null
            || !Objects.equals(row.assignment().getId(), fullAssignment.assignment().getId())) {
          fullClassXAssignment
              .assignments()
              .add(fullAssignment = new FullAssignment(row.assignment(), new ArrayList<>()));
        }

        if (row.knowledgeAndSkill() != null) {
          fullAssignment.knowledgeAndSkills().add(row.knowledgeAndSkill());
        }
      }
    }

    return fullClassXAssignments;
  }

  @Query(
      "SELECT a"
          + " FROM Assignment a"
          + " LEFT JOIN FETCH a.classX"
          + " WHERE a.classX.id = (:classXId)"
          + " AND a.name = (:name)")
  Optional<Assignment> findByName(@Param("classXId") int classXId, @Param("name") String name);
}
