package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectInputCategory;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDefinitionRepository extends JpaRepository<ProjectDefinition, Integer> {

  record FullProjectDefinition(
      ProjectDefinition definition, Instant selected, List<ProjectDefinitionCategory> categories) {}

  @Deprecated
  record ProjectDefinitionInputCategories(
      ProjectDefinition definition, ImmutableList<ProjectInputCategory> inputCategories) {}

  record FullProjectDefinitionRow(
      @Nullable AssignmentProjectDefinition assignmentProjectDefinition,
      ProjectDefinition definition,
      ProjectDefinitionCategory category) {}

  @Deprecated
  record FullProjectInputDefinitionRow(
      ProjectDefinition definition, ProjectInputCategory category) {}

  default ProjectDefinition upsert(String name, UserX userX, Consumer<ProjectDefinition> modifier) {
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(modifier);

    ProjectDefinition projectDefinition =
        findByName(name)
            .orElseGet(() -> new ProjectDefinition().setCreationTime(Instant.now()))
            .setUserX(userX)
            .setName(name);

    modifier.accept(projectDefinition);

    return saveAndFlush(projectDefinition);
  }

  // TODO: For development, remove.
  Optional<ProjectDefinition> findByName(String name);

  @Deprecated
  @Query(
      """
          SELECT
              new org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository$FullProjectInputDefinitionRow(
                  d, i)
          FROM ProjectDefinition d
          LEFT JOIN FETCH ProjectInputCategory i
          ON d.id = i.projectDefinition.id
          WHERE d.id = (:projectDefinitionId)
          ORDER BY i.position, i.id
                """)
  List<FullProjectInputDefinitionRow> getProjectDefinitionRows(
      @Param("projectDefinitionId") int projectDefinitionId);

  @Deprecated
  default Optional<ProjectDefinitionInputCategories> getProjectDefinition(int projectDefinitionId) {
    ProjectDefinition definition = null;
    ImmutableList.Builder<ProjectInputCategory> inputCategories = ImmutableList.builder();

    for (FullProjectInputDefinitionRow row : getProjectDefinitionRows(projectDefinitionId)) {
      definition = row.definition();
      inputCategories.add(row.category());
    }

    if (definition != null) {
      return Optional.of(new ProjectDefinitionInputCategories(definition, inputCategories.build()));
    } else {
      return Optional.empty();
    }
  }

  @Query(
      """
SELECT new org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository$FullProjectDefinitionRow(
    NULL, d, c)
FROM ProjectDefinition d
LEFT JOIN FETCH d.userX
LEFT JOIN FETCH ProjectDefinitionCategory c
ON d.id = c.projectDefinition.id
LEFT JOIN FETCH c.projectDefinitionCategoryType
WHERE d.id = (:projectDefinitionId)
ORDER BY d.id, c.position, c.id
      """)
  List<FullProjectDefinitionRow> findFullProjectDefinitionRows(
      @Param("projectDefinitionId") int projectDefinitionId);

  default Optional<FullProjectDefinition> findFullProjectDefinition(int projectDefinitionId) {
    return Optional.ofNullable(
        Iterables.getOnlyElement(
            toFullProjectDefinitions(findFullProjectDefinitionRows(projectDefinitionId)), null));
  }

  @Query(
      """
SELECT new org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository$FullProjectDefinitionRow(
    apd, d, c)
FROM AssignmentProjectDefinition apd
LEFT JOIN FETCH apd.projectDefinition d
LEFT JOIN FETCH d.userX
LEFT JOIN ProjectDefinitionCategory c
ON d.id = c.projectDefinition.id
LEFT JOIN FETCH c.projectDefinitionCategoryType
WHERE apd.assignment.id = (:assignmentId)
ORDER BY apd.selected DESC, d.id, c.position, c.id
      """)
  List<FullProjectDefinitionRow> findFullProjectDefinitionRowsByAssignmentId(
      @Param("assignmentId") int assignmentId);

  default List<FullProjectDefinition> findFullProjectDefinitionsByAssignmentId(int assignmentId) {
    return toFullProjectDefinitions(findFullProjectDefinitionRowsByAssignmentId(assignmentId));
  }

  private List<FullProjectDefinition> toFullProjectDefinitions(
      Iterable<FullProjectDefinitionRow> rows) {
    List<FullProjectDefinition> allDefinitions = new ArrayList<>();
    FullProjectDefinition definition = null;

    for (FullProjectDefinitionRow row : rows) {
      if (definition == null
          || !Objects.equals(definition.definition.getId(), row.definition().getId())) {
        allDefinitions.add(
            definition =
                new FullProjectDefinition(
                    row.definition(),
                    row.assignmentProjectDefinition() != null
                        ? row.assignmentProjectDefinition().getSelected()
                        : null,
                    new ArrayList<>()));
      }
      if (row.category() != null) {
        definition.categories.add(row.category());
      }
    }

    return allDefinitions;
  }
}
