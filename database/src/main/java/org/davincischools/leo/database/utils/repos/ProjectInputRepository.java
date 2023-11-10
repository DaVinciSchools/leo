package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectInputRepository extends JpaRepository<ProjectInput, Integer> {

  enum State {
    PROCESSING,
    COMPLETED,
    FAILED
  }

  @Modifying
  @Transactional
  @Query("UPDATE ProjectInput p SET p.state = (:state) WHERE p.id = (:id)")
  void updateState(@Param("id") int id, @Param("state") String state);

  @Modifying
  @Transactional
  @Query(
      "UPDATE ProjectInput p SET p.userX.id = (:userXId) WHERE p.id = (:id) AND p.userX.id IS NULL")
  void updateUserX(@Param("id") int id, @Param("userXId") int userXId);
}
