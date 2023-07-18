package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

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

  interface ClassXAssignment {

    ClassX getClassX();

    Assignment getAssignment();
  }

  /** Note: this includes all classes, even when there's no assignment. */
  @Query(
      "SELECT sc.classX AS classX, a AS assignment"
          + " FROM StudentClassX sc"
          + " LEFT JOIN Assignment a"
          + " ON sc.classX.id = a.classX.id"
          + " WHERE sc.student.id = (:studentId)")
  List<ClassXAssignment> findAllByStudentId(@Param("studentId") int studentId);

  /** Note: this includes all classes, even when there's no assignment. */
  @Query(
      "SELECT tc.classX AS classX, a AS assignment"
          + " FROM TeacherClassX tc"
          + " LEFT JOIN Assignment a"
          + " ON tc.classX.id = a.classX.id"
          + " WHERE tc.teacher.id = (:teacherId)")
  List<ClassXAssignment> findAllByTeacherId(@Param("teacherId") int teacherId);

  @Query(
      "SELECT a"
          + " FROM Assignment a"
          + " WHERE a.classX.id = (:classXId)"
          + " AND a.name = (:name)")
  Optional<Assignment> findByName(@Param("classXId") int classXId, @Param("name") String name);
}
