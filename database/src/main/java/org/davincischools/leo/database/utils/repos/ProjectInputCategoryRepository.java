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

@Deprecated
@Repository
public interface ProjectInputCategoryRepository
    extends JpaRepository<ProjectInputCategory, Integer> {

  @Deprecated
  default ProjectInputCategory upsert(
      String name, ProjectDefinition projectDefinition, Consumer<ProjectInputCategory> modifier) {
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(projectDefinition);
    checkNotNull(modifier);

    ProjectInputCategory projectInputCategory =
        findByName(name)
            .orElseGet(() -> new ProjectInputCategory().setCreationTime(Instant.now()))
            .setName(name)
            .setProjectDefinition(projectDefinition);

    modifier.accept(projectInputCategory);

    return saveAndFlush(projectInputCategory);
  }

  // TODO: For development, remove.
  @Deprecated
  Optional<ProjectInputCategory> findByName(String name);
}
