package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.ProjectDefinition;
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

  default ProjectInputCategory upsert(
      String title, ProjectDefinition projectDefinition, Consumer<ProjectInputCategory> modifier) {
    checkArgument(!Strings.isNullOrEmpty(title));
    checkNotNull(projectDefinition);
    checkNotNull(modifier);

    ProjectInputCategory projectInputCategory =
        findByTitle(title)
            .orElseGet(() -> new ProjectInputCategory().setCreationTime(Instant.now()))
            .setTitle(title)
            .setProjectDefinition(projectDefinition);

    modifier.accept(projectInputCategory);

    return saveAndFlush(projectInputCategory);
  }

  // TODO: For development, remove.
  Optional<ProjectInputCategory> findByTitle(String title);
}
