package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Streams;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.TeacherSchoolId;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherSchoolRepository extends JpaRepository<TeacherSchool, TeacherSchoolId> {

  default TeacherSchoolId createId(Teacher teacher, School school) {
    checkNotNull(teacher);
    checkNotNull(school);

    return new TeacherSchoolId().setTeacherId(teacher.getId()).setSchoolId(school.getId());
  }

  default TeacherSchool create(Teacher teacher, School school) {
    checkNotNull(teacher);
    checkNotNull(school);

    return new TeacherSchool()
        .setId(createId(teacher, school))
        .setCreationTime(Instant.now())
        .setTeacher(teacher)
        .setSchool(school);
  }

  // TODO: What about created time?
  default TeacherSchool upsert(Teacher teacher, School school) {
    checkNotNull(teacher);
    checkNotNull(school);

    return save(create(teacher, school));
  }

  List<TeacherSchool> findAllByTeacher(Teacher teacher);

  @Transactional
  default void setTeacherSchools(Teacher teacher, Iterable<School> schools) {
    DaoUtils.updateCollection(
        findAllByTeacher(teacher),
        Streams.stream(schools).map(school -> create(teacher, school)).toList(),
        ts -> ts.getSchool().getId(),
        this::saveAll,
        this::deleteAll);
  }
}
