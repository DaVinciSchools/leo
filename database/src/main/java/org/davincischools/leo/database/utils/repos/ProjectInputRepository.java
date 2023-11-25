package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition_;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory_;
import org.davincischools.leo.database.daos.ProjectDefinition_;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectInputValue_;
import org.davincischools.leo.database.daos.ProjectInput_;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.davincischools.leo.database.utils.query_helper.Expression;
import org.davincischools.leo.database.utils.query_helper.Predicate;
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
        .query(ProjectInput.class, projectInput -> configureQuery(projectInput, params));
  }

  public static Entity<?, ProjectInput> configureQuery(
      Entity<?, ProjectInput> projectInput, GetProjectInputsParams params) {
    checkNotNull(projectInput);
    checkNotNull(params);

    projectInput.notDeleted().fetch().requireId(params.getProjectInputIds());

    // UserX
    projectInput
        .join(ProjectInput_.userX, JoinType.LEFT)
        .notDeleted()
        .fetch()
        .requireId(params.getUserXIds());

    // includeComplete/Processing
    projectInput.where(
        Predicate.or(
            params.getIncludeComplete().orElse(false)
                ? Predicate.eq(projectInput.get(ProjectInput_.state), State.COMPLETED.name())
                : Expression.FALSE,
            params.getIncludeProcessing().orElse(false)
                ? Predicate.eq(projectInput.get(ProjectInput_.state), State.PROCESSING.name())
                : Expression.FALSE));

    var projectDefinition =
        projectInput.join(ProjectInput_.projectDefinition, JoinType.LEFT).notDeleted().fetch();
    projectDefinition
        .join(
            ProjectDefinition_.projectDefinitionCategories,
            JoinType.LEFT,
            ProjectDefinitionCategory::getProjectDefinition,
            ProjectDefinition::setProjectDefinitionCategories)
        .notDeleted()
        .fetch()
        .join(ProjectDefinitionCategory_.projectDefinitionCategoryType, JoinType.LEFT)
        .notDeleted()
        .fetch();

    var projectInputValue =
        projectInput
            .join(
                ProjectInput_.projectInputValues,
                JoinType.LEFT,
                ProjectInputValue::getProjectInput,
                ProjectInput::setProjectInputValues)
            .notDeleted()
            .fetch();
    projectInputValue
        .join(ProjectInputValue_.knowledgeAndSkillValue, JoinType.LEFT)
        .notDeleted()
        .fetch();
    projectInputValue.join(ProjectInputValue_.motivationValue, JoinType.LEFT).notDeleted().fetch();

    if (params.getIncludeAssignment().isPresent()) {
      for (var assignment :
          List.of(
              projectInput.join(ProjectInput_.assignment, JoinType.LEFT),
              projectDefinition
                  .join(
                      ProjectDefinition_.assignmentProjectDefinitions,
                      JoinType.LEFT,
                      AssignmentProjectDefinition::getProjectDefinition,
                      ProjectDefinition::setAssignmentProjectDefinitions)
                  .notDeleted()
                  .fetch()
                  .join(AssignmentProjectDefinition_.assignment, JoinType.LEFT))) {
        AssignmentRepository.configureQuery(assignment, params.getIncludeAssignment().get());
      }
    }

    return projectInput;
  }
}
