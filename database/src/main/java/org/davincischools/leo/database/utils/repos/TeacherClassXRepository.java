package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassXId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherClassXRepository extends JpaRepository<TeacherClassX, TeacherClassXId> {

  default TeacherClassX upsert(Teacher teacher, ClassX classX) {
    checkNotNull(teacher);
    checkNotNull(classX);

    return saveAndFlush(
        new TeacherClassX()
            .setCreationTime(Instant.now())
            .setId(new TeacherClassXId().setTeacherId(teacher.getId()).setClassXId(classX.getId()))
            .setTeacher(teacher)
            .setClassX(classX));
  }
}
