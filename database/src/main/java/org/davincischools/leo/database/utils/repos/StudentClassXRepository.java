package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Streams;
import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.StudentClassXId;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentClassXRepository extends JpaRepository<StudentClassX, StudentClassXId> {

  default StudentClassXId createId(Student student, ClassX classX) {
    checkNotNull(student);
    checkNotNull(classX);

    return new StudentClassXId().setStudentId(student.getId()).setClassXId(classX.getId());
  }

  default StudentClassX create(Student student, ClassX classX) {
    checkNotNull(student);
    checkNotNull(classX);

    return new StudentClassX()
        .setId(createId(student, classX))
        .setCreationTime(Instant.now())
        .setStudent(student)
        .setClassX(classX);
  }

  // TODO: What about created time?
  default StudentClassX upsert(Student student, ClassX classX) {
    checkNotNull(student);
    checkNotNull(classX);

    return save(create(student, classX));
  }

  List<StudentClassX> findAllByStudent(Student student);

  default void setStudentClassXs(Student student, Iterable<ClassX> classXs) {
    DaoUtils.updateCollection(
        findAllByStudent(student),
        Streams.stream(classXs).map(classX -> create(student, classX)).toList(),
        scx -> scx.getClassX().getId(),
        this::saveAll,
        this::deleteAll);
  }
}
