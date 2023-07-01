package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXRepository extends JpaRepository<ClassX, Integer> {

  default ClassX upsert(School school, String name, Consumer<ClassX> modifier) {
    checkNotNull(school);
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(modifier);

    ClassX classX =
        findByName(school.getId(), name)
            .orElseGet(() -> new ClassX().setCreationTime(Instant.now()))
            .setSchool(school)
            .setName(name);

    modifier.accept(classX);

    return saveAndFlush(classX);
  }

  @Query("SELECT c FROM ClassX c WHERE c.school.id = (:schoolId) AND c.name = (:name)")
  Optional<ClassX> findByName(@Param("schoolId") int schoolId, @Param("name") String name);

  Iterable<ClassX> findAllBySchool(School school);
}
