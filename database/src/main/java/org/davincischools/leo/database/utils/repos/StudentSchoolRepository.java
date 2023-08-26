package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Streams;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentSchool;
import org.davincischools.leo.database.daos.StudentSchoolId;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSchoolRepository extends JpaRepository<StudentSchool, StudentSchoolId> {

  default StudentSchoolId createId(Student student, School school) {
    checkNotNull(student);
    checkNotNull(school);

    return new StudentSchoolId().setStudentId(student.getId()).setSchoolId(school.getId());
  }

  default StudentSchool create(Student student, School school) {
    checkNotNull(student);
    checkNotNull(school);

    return new StudentSchool()
        .setId(createId(student, school))
        .setCreationTime(Instant.now())
        .setStudent(student)
        .setSchool(school);
  }

  // TODO: What about created time?
  default StudentSchool upsert(Student student, School school) {
    checkNotNull(student);
    checkNotNull(school);

    return save(create(student, school));
  }

  List<StudentSchool> findAllByStudent(Student student);

  @Transactional
  default void setStudentSchools(Student student, Iterable<School> schools) {
    DaoUtils.updateCollection(
        findAllByStudent(student),
        Streams.stream(schools).map(school -> create(student, school)).toList(),
        ss -> ss.getSchool().getId(),
        this::saveAll,
        this::deleteAll);
  }
}
