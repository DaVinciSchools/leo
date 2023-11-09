package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition_;
import org.davincischools.leo.database.daos.Assignment_;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory_;
import org.davincischools.leo.database.daos.ProjectDefinition_;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.QueryHelper.QueryHelperUtils;
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
            (u, projectDefinition, builder) ->
                configureQuery(u, projectDefinition, builder, params));
  }

  static void configureQuery(
      QueryHelperUtils u,
      From<?, org.davincischools.leo.database.daos.ProjectDefinition> projectDefinition,
      CriteriaBuilder builder,
      GetProjectDefinitionsParams params) {
    checkNotNull(u);
    checkNotNull(projectDefinition);
    checkNotNull(builder);
    checkNotNull(params);

    u.notDeleted(projectDefinition);

    u.notDeleted(u.fetch(projectDefinition, ProjectDefinition_.userX, JoinType.LEFT));

    var projectDefinitionCategory =
        u.fetch(
            projectDefinition,
            ProjectDefinition_.projectDefinitionCategories,
            JoinType.LEFT,
            ProjectDefinitionCategory::getProjectDefinition,
            org.davincischools.leo.database.daos.ProjectDefinition::setProjectDefinitionCategories);

    u.fetch(
        projectDefinitionCategory,
        ProjectDefinitionCategory_.projectDefinitionCategoryType,
        JoinType.LEFT);

    if (params.getProjectDefinitionIds().isPresent()) {
      u.where(
          projectDefinition
              .get(ProjectDefinition_.id)
              .in(ImmutableList.copyOf(params.getProjectDefinitionIds().get())));
    }

    if (params.getAssignmentIds().isPresent()) {
      var assignmentProjectDefinition =
          projectDefinition.join(ProjectDefinition_.assignmentProjectDefinitions, JoinType.LEFT);
      u.where(
          assignmentProjectDefinition
              .get(AssignmentProjectDefinition_.assignment)
              .get(Assignment_.id)
              .in(ImmutableList.copyOf(params.getAssignmentIds().get())));
    }
  }
}
