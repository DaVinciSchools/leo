package org.davincischools.leo.database.utils.repos;

import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.Motivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivationRepository extends JpaRepository<Motivation, Integer> {

  Optional<Motivation> findByName(String name);

  @Query("SELECT m FROM Motivation m WHERE m.id IN (:ids)")
  Iterable<Motivation> findAllByIds(@Param("ids") List<Integer> ids);
}
