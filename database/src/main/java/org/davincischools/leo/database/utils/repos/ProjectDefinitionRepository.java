package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory_;
import org.davincischools.leo.database.daos.ProjectDefinition_;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectDefinitionRepository
    extends JpaRepository<ProjectDefinition, Integer>, AutowiredRepositoryValues {

  default org.davincischools.leo.database.daos.ProjectDefinition upsert(
      String name,
      UserX userX,
      Consumer<org.davincischools.leo.database.daos.ProjectDefinition> modifier) {
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(modifier);

    org.davincischools.leo.database.daos.ProjectDefinition projectDefinition =
        findByName(name)
            .orElseGet(
                () ->
                    new org.davincischools.leo.database.daos.ProjectDefinition()
                        .setCreationTime(Instant.now()))
            .setUserX(userX)
            .setName(name);

    modifier.accept(projectDefinition);

    return saveAndFlush(projectDefinition);
  }

  // TODO: For development, remove.
  Optional<org.davincischools.leo.database.daos.ProjectDefinition> findByName(String name);

  @Modifying
  @Transactional
  @Query(
      "UPDATE ProjectDefinition p SET p.userX.id = (:userXId) WHERE p.id = (:id) AND p.userX.id IS"
          + " NULL")
  void updateUserX(@Param("id") int id, @Param("userXId") int userXId);

  default List<org.davincischools.leo.database.daos.ProjectDefinition> getProjectDefinitions(
      GetProjectDefinitionsParams params) {
    checkNotNull(params);

    return getQueryHelper()
        .query(
            org.davincischools.leo.database.daos.ProjectDefinition.class,
            projectDefinition -> configureQuery(projectDefinition, params));
  }

  static <P, S> Entity<P, S, ProjectDefinition> configureQuery(
      Entity<P, S, ProjectDefinition> projectDefinition, GetProjectDefinitionsParams params) {
    checkNotNull(projectDefinition);
    checkNotNull(params);

    projectDefinition.fetch().requireId(params.getProjectDefinitionIds());

    // var assignment =
    projectDefinition.supplier(
        () ->
            projectDefinition
                .join(ProjectDefinition_.assignmentProjectDefinitions, JoinType.LEFT)
                .notDeleted()
                .requireId(params.getAssignmentIds()));

    // var categoryType =
    projectDefinition
        .join(ProjectDefinition_.projectDefinitionCategories, JoinType.LEFT)
        .notDeleted()
        .join(ProjectDefinitionCategory_.projectDefinitionCategoryType, JoinType.LEFT)
        .fetch();

    return projectDefinition;
  }
}
