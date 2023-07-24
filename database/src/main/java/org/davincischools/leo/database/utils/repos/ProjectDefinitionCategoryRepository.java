package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
        findByProjectDefinition(projectDefinition.getId(), projectDefinitionCategoryType.getId())
            .orElseGet(
                () ->
                    new ProjectDefinitionCategory()
                        .setCreationTime(Instant.now())
                        .setProjectDefinition(projectDefinition)
                        .setProjectDefinitionCategoryType(projectDefinitionCategoryType));

    modifier.accept(projectDefinitionCategory);

    return saveAndFlush(projectDefinitionCategory);
  }

  @Query(
      """
      SELECT pdc
      FROM ProjectDefinitionCategory pdc
      WHERE pdc.projectDefinition.id = :definitionId
      AND pdc.projectDefinitionCategoryType.id= :categoryTypeId""")
  Optional<ProjectDefinitionCategory> findByProjectDefinition(
      @Param("definitionId") int definitionId, @Param("categoryTypeId") int categoryTypeId);
}
