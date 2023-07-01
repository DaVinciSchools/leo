package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.Optional;
import org.davincischools.leo.database.daos.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {

  default District upsert(String name) {
    checkArgument(!Strings.isNullOrEmpty(name));

    return saveAndFlush(
        findByName(name)
            .orElseGet(() -> new District().setCreationTime(Instant.now()))
            .setName(name));
  }

  Optional<District> findByName(String name);
}
