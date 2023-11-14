package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory_;
import org.davincischools.leo.database.daos.ProjectDefinition_;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectInputValue_;
import org.davincischools.leo.database.daos.ProjectInput_;
import org.davincischools.leo.database.utils.QueryHelper.QueryHelperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectInputRepository
    extends JpaRepository<ProjectInput, Integer>, AutowiredRepositoryValues {

  enum State {
    PROCESSING,
    COMPLETED,
    FAILED
  }

  @Modifying
  @Transactional
  @Query("UPDATE ProjectInput p SET p.state = (:state) WHERE p.id = (:id)")
  void updateState(@Param("id") int id, @Param("state") String state);

  @Modifying
  @Transactional
  @Query(
      "UPDATE ProjectInput p SET p.userX.id = (:userXId) WHERE p.id = (:id) AND p.userX.id IS NULL")
  void updateUserX(@Param("id") int id, @Param("userXId") int userXId);

  default List<ProjectInput> getProjectInputs(GetProjectInputsParams params) {
    checkNotNull(params);

    return getQueryHelper()
        .query(
            ProjectInput.class,
            (u, projectInput, builder) -> configureQuery(u, projectInput, builder, params));
  }

  public static void configureQuery(
      QueryHelperUtils u,
      From<?, ProjectInput> projectInput,
      CriteriaBuilder builder,
      GetProjectInputsParams params) {
    checkNotNull(u);
    checkNotNull(projectInput);
    checkNotNull(builder);
    checkNotNull(params);

    u.notDeleted(projectInput);

    var userX = u.notDeleted(u.fetch(projectInput, ProjectInput_.userX, JoinType.LEFT));

    var projectDefinition =
        u.notDeleted(u.fetch(projectInput, ProjectInput_.projectDefinition, JoinType.LEFT));
    var projectDefintionCategories =
        u.notDeleted(
            u.fetch(
                projectDefinition,
                ProjectDefinition_.projectDefinitionCategories,
                JoinType.LEFT,
                ProjectDefinitionCategory::getProjectDefinition,
                ProjectDefinition::setProjectDefinitionCategories));
    u.notDeleted(
        u.fetch(
            projectDefintionCategories,
            ProjectDefinitionCategory_.projectDefinitionCategoryType,
            JoinType.LEFT));

    var projectInputValue =
        u.fetch(
            projectInput,
            ProjectInput_.projectInputValues,
            JoinType.LEFT,
            ProjectInputValue::getProjectInput,
            ProjectInput::setProjectInputValues);
    u.fetch(projectInputValue, ProjectInputValue_.knowledgeAndSkillValue, JoinType.LEFT);
    u.fetch(projectInputValue, ProjectInputValue_.motivationValue, JoinType.LEFT);

    if (params.getUserXIds().isPresent()) {
      u.where(userX.get("id").in(ImmutableList.of(params.getUserXIds().get())));
    }

    if (params.getProjectInputIds().isPresent()) {
      u.where(
          projectInput
              .get(ProjectInput_.id)
              .in(ImmutableList.of(params.getProjectInputIds().get())));
    }

    u.where(
        builder.or(
            params.getIncludeComplete().orElse(false)
                ? builder.equal(projectInput.get(ProjectInput_.state), State.COMPLETED.name())
                : builder.literal(false),
            params.getIncludeProcessing().orElse(false)
                ? builder.equal(projectInput.get(ProjectInput_.state), State.PROCESSING.name())
                : builder.literal(false)));

    if (params.getIncludeAssignment().isPresent()) {
      var assignment = u.notDeleted(u.fetch(projectInput, ProjectInput_.assignment, JoinType.LEFT));
      AssignmentRepository.configureQuery(
          u, assignment, builder, params.getIncludeAssignment().get());
    }
  }
}
