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

  default TeacherClassXId createId(int teacherId, int classXId) {
    return new TeacherClassXId().setTeacherId(teacherId).setClassXId(classXId);
  }

  default TeacherClassX upsert(Teacher teacher, ClassX classX) {
    checkNotNull(teacher);
    checkNotNull(classX);

    return saveAndFlush(
        new TeacherClassX()
            .setCreationTime(Instant.now())
            .setId(createId(teacher.getId(), classX.getId()))
            .setTeacher(teacher)
            .setClassX(classX));
  }

  default boolean canTeacherUpdateClassX(int teacherId, int classXId) {
    return findById(createId(teacherId, classXId)).isPresent();
  }
}
