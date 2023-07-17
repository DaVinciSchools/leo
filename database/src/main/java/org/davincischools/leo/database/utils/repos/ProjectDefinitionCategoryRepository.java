package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDefinitionCategoryRepository
    extends JpaRepository<ProjectDefinitionCategory, Integer> {

  default ProjectDefinitionCategory upsert(
      ProjectDefinition projectDefinition,
      ProjectDefinitionCategoryType projectDefinitionCategoryType,
      Consumer<ProjectDefinitionCategory> modifier) {
    checkNotNull(projectDefinition);
    checkNotNull(projectDefinitionCategoryType);
    checkNotNull(modifier);

    ProjectDefinitionCategory projectDefinitionCategory =
        new ProjectDefinitionCategory()
            .setCreationTime(Instant.now())
            .setProjectDefinition(projectDefinition)
            .setProjectDefinitionCategoryType(projectDefinitionCategoryType);

    modifier.accept(projectDefinitionCategory);

    return saveAndFlush(projectDefinitionCategory);
  }
}
