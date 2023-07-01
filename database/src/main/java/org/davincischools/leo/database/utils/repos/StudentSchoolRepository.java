package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentSchool;
import org.davincischools.leo.database.daos.StudentSchoolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSchoolRepository extends JpaRepository<StudentSchool, StudentSchoolId> {

  default StudentSchool upsert(Student student, School school) {
    checkNotNull(student);
    checkNotNull(school);

    return saveAndFlush(
        new StudentSchool()
            .setCreationTime(Instant.now())
            .setId(new StudentSchoolId().setStudentId(student.getId()).setSchoolId(school.getId()))
            .setStudent(student)
            .setSchool(school));
  }
}
