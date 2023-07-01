package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

  default UserX upsert(UserX userX) {
    checkNotNull(userX);

    if (!UserXRepository.isTeacher(userX)) {
      userX.setTeacher(saveAndFlush(new Teacher().setCreationTime(Instant.now())));
    }
    return userX;
  }
}
