package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import org.davincischools.leo.database.daos.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

  @Query(
      "SELECT p"
          + " FROM Project p"
          + " INNER JOIN FETCH p.projectInput pi"
          + " WHERE pi.userX.id = (:userXId)")
  Iterable<Project> findAllByUserXId(@Param("userXId") int userXId);

  @Query(
      "SELECT p"
          + " FROM Project p"
          + " INNER JOIN FETCH p.projectInput pi"
          + " WHERE pi.userX.id = (:userXId)"
          + " AND p.active")
  Iterable<Project> findAllActiveByUserXId(@Param("userXId") int userXId);

  @Query("SELECT p FROM Project p INNER JOIN FETCH p.projectInput WHERE p.id = (:projectId)")
  Optional<Project> findById(@Param("projectId") int projectId);
}
