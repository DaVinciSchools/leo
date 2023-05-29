package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInputRepository extends JpaRepository<ProjectInput, Integer> {

  enum State {
    PROCESSING,
    COMPLETED,
    FAILED
  }
}
