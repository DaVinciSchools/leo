package org.davincischools.leo.database.utils.repos;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectInputCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDefinitionRepository extends JpaRepository<ProjectDefinition, Integer> {

  // TODO: For development, remove.
  Optional<ProjectDefinition> findByName(String name);

  record ProjectDefinitionInputCategories(
      ProjectDefinition definition, ImmutableList<ProjectInputCategory> inputCategories) {}

  @Query(
      "SELECT d, i FROM ProjectDefinition d"
          + " LEFT JOIN FETCH ProjectInputCategory i"
          + " ON d.id = i.projectDefinition.id"
          + " WHERE d.id = (:projectDefinitionId)"
          + " ORDER BY i.position ASC")
  Iterable<Object[]> _internal_getProjectDefinition(
      @Param("projectDefinitionId") int projectDefinitionId);

  default Optional<ProjectDefinitionInputCategories> getProjectDefinition(int projectDefinitionId) {
    ProjectDefinition definition = null;
    ImmutableList.Builder<ProjectInputCategory> inputCategories = ImmutableList.builder();

    for (Object[] values : _internal_getProjectDefinition(projectDefinitionId)) {
      definition = (ProjectDefinition) values[0];
      inputCategories.add((ProjectInputCategory) values[1]);
    }

    if (definition != null) {
      return Optional.of(new ProjectDefinitionInputCategories(definition, inputCategories.build()));
    } else {
      return Optional.empty();
    }
  }
}
