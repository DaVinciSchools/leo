package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputFulfillment;
import org.davincischools.leo.database.daos.ProjectInputFulfillment_;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectInputValue_;
import org.davincischools.leo.database.daos.ProjectInput_;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.daos.ProjectMilestone_;
import org.davincischools.leo.database.daos.Project_;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.utils.QueryHelper.QueryHelperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository
    extends JpaRepository<Project, Integer>, AutowiredRepositoryValues {

  default List<Project> getProjects(GetProjectsParams params) {
    checkNotNull(params);

    return getQueryHelper()
        .query(Project.class, (u, project, builder) -> configureQuery(u, project, builder, params));
  }

  public static void configureQuery(
      QueryHelperUtils u,
      From<?, Project> project,
      CriteriaBuilder builder,
      GetProjectsParams params) {
    checkNotNull(u);
    checkNotNull(project);
    checkNotNull(builder);
    checkNotNull(params);

    u.notDeleted(project);

    var projectInput = u.notDeleted(u.fetch(project, Project_.projectInput, JoinType.LEFT));
    var userX = u.notDeleted(u.fetch(projectInput, ProjectInput_.userX, JoinType.LEFT));

    if (params.getUserXIds().isPresent()) {
      u.where(userX.get("id").in(ImmutableList.of(params.getUserXIds().get())));
    }

    if (params.getProjectIds().isPresent()) {
      u.where(project.get(Project_.id).in(ImmutableList.of(params.getProjectIds().get())));
    }

    if (!params.getIncludeInactive().orElse(false)) {
      u.where(builder.isTrue(project.get(Project_.active)));
    }

    if (params.getIncludeTags().orElse(false)) {
      u.notDeleted(
          u.fetch(project, Project_.tags, JoinType.LEFT, Tag::getProject, Project::setTags));
    }

    if (params.getIncludeInputs().orElse(false) || params.getIncludeFulfillments().orElse(false)) {
      var projectInputValue =
          u.notDeleted(
              u.fetch(
                  projectInput,
                  ProjectInput_.projectInputValues,
                  JoinType.LEFT,
                  ProjectInputValue::getProjectInput,
                  ProjectInput::setProjectInputValues));

      var projectDefinition =
          u.notDeleted(u.fetch(projectInput, ProjectInput_.projectDefinition, JoinType.LEFT));
      ProjectDefinitionRepository.configureQuery(
          u, projectDefinition, builder, new GetProjectDefinitionsParams());

      if (params.getIncludeFulfillments().orElse(false)) {
        var projectInputFulfillment =
            u.notDeleted(
                u.fetch(
                    projectInputValue,
                    ProjectInputValue_.projectInputFulfillments,
                    JoinType.LEFT,
                    ProjectInputFulfillment::getProjectInputValue,
                    ProjectInputValue::setProjectInputFulfillments));
        u.addJoinOn(
            projectInputFulfillment,
            builder.equal(projectInputFulfillment.get(ProjectInputFulfillment_.project), project));
      }
    }

    if (params.getIncludeAssignment().isPresent()) {
      var assignment = u.notDeleted(u.fetch(project, Project_.assignment, JoinType.LEFT));
      AssignmentRepository.configureQuery(
          u, assignment, builder, params.getIncludeAssignment().get());
    }

    if (params.getIncludeMilestones().orElse(false)) {
      var milestone =
          u.notDeleted(
              u.fetch(
                  project,
                  Project_.projectMilestones,
                  JoinType.LEFT,
                  ProjectMilestone::getProject,
                  Project::setProjectMilestones));

      u.notDeleted(
          u.fetch(
              milestone,
              ProjectMilestone_.projectMilestoneSteps,
              JoinType.LEFT,
              ProjectMilestoneStep::getProjectMilestone,
              ProjectMilestone::setProjectMilestoneSteps));
    }
  }
}
