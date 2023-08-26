package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Streams;
import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassXId;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherClassXRepository extends JpaRepository<TeacherClassX, TeacherClassXId> {

  default TeacherClassXId createId(Teacher teacher, ClassX classX) {
    checkNotNull(teacher);
    checkNotNull(classX);

    return new TeacherClassXId().setTeacherId(teacher.getId()).setClassXId(classX.getId());
  }

  default TeacherClassX create(Teacher teacher, ClassX classX) {
    checkNotNull(teacher);
    checkNotNull(classX);

    return new TeacherClassX()
        .setId(createId(teacher, classX))
        .setCreationTime(Instant.now())
        .setTeacher(teacher)
        .setClassX(classX);
  }

  // TODO: What about created time?
  default TeacherClassX upsert(Teacher teacher, ClassX classX) {
    checkNotNull(teacher);
    checkNotNull(classX);

    return save(create(teacher, classX));
  }

  List<TeacherClassX> findAllByTeacher(Teacher teacher);

  default void setTeacherClassXs(Teacher teacher, Iterable<ClassX> classXs) {
    DaoUtils.updateCollection(
        findAllByTeacher(teacher),
        Streams.stream(classXs).map(classX -> create(teacher, classX)).toList(),
        tcx -> tcx.getClassX().getId(),
        this::saveAll,
        this::deleteAll);
  }

  default boolean canTeacherUpdateClassX(Teacher teacher, ClassX classX) {
    return findById(createId(teacher, classX)).isPresent();
  }
}
