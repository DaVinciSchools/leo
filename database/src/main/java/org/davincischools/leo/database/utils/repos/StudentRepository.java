package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

  default UserX upsert(UserX userX, Consumer<Student> modifier) {
    checkNotNull(userX);
    checkNotNull(modifier);

    Optional<Student> optionalStudent = Optional.empty();
    if (UserXRepository.isStudent(userX)) {
      optionalStudent = findById(userX.getStudent().getId());
    }

    Student student = optionalStudent.orElseGet(() -> new Student().setCreationTime(Instant.now()));

    modifier.accept(student);

    userX.setStudent(saveAndFlush(student));

    return userX;
  }
}
