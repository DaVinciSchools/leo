package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDefinitionCategoryTypeRepository
    extends JpaRepository<ProjectDefinitionCategoryType, Integer> {

  enum ValueType {
    FREE_TEXT,
    EKS,
    XQ_COMPETENCY,
    MOTIVATION
  }

  default ProjectDefinitionCategoryType upsert(
      String name, Consumer<ProjectDefinitionCategoryType> modifier) {
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(modifier);

    ProjectDefinitionCategoryType projectDefinitionCategoryType =
        findByName(name)
            .orElseGet(() -> new ProjectDefinitionCategoryType().setCreationTime(Instant.now()))
            .setName(name);

    modifier.accept(projectDefinitionCategoryType);

    return saveAndFlush(projectDefinitionCategoryType);
  }

  Optional<ProjectDefinitionCategoryType> findByName(String name);

  @Query(
      """
      SELECT pdct
      FROM ProjectDefinitionCategoryType pdct
      WHERE pdct.includeInDemo = (:include_demos)""")
  List<ProjectDefinitionCategoryType> findAllByCategories(
      @Param("include_demos") boolean include_demos);
}
