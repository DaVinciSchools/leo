package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.Motivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivationRepository extends JpaRepository<Motivation, Integer> {

  default Motivation upsert(String name, String descr, Consumer<Motivation> modifier) {
    checkArgument(!Strings.isNullOrEmpty(name));
    checkArgument(!Strings.isNullOrEmpty(descr));
    checkNotNull(modifier);

    Motivation motivation =
        findByName(name)
            .orElseGet(() -> new Motivation().setCreationTime(Instant.now()))
            .setName(name)
            .setShortDescr(descr);

    modifier.accept(motivation);

    return saveAndFlush(motivation);
  }

  Optional<Motivation> findByName(String name);

  @Query("SELECT m FROM Motivation m WHERE m.id IN (:ids)")
  List<Motivation> findAllByIds(@Param("ids") List<Integer> ids);
}
