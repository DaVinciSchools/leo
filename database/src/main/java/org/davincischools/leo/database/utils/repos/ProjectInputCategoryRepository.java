package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import org.davincischools.leo.database.daos.ProjectInputCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInputCategoryRepository
    extends JpaRepository<ProjectInputCategory, Integer> {

  enum ValueType {
    FREE_TEXT,
    EKS,
    XQ_COMPETENCY,
    MOTIVATION
  }

  // TODO: For development, remove.
  Optional<ProjectInputCategory> findByTitle(String title);
}
