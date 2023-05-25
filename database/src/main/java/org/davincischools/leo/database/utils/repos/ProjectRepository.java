package org.davincischools.leo.database.utils.repos;

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
          + " INNER JOIN ProjectInput pi"
          + " ON pi.id = p.projectInput.id"
          + " WHERE pi.userX.id = (:userXId)")
  Iterable<Project> findAllByUserXId(@Param("userXId") int userXId);
}
