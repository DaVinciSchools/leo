package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInputFulfillment;
import org.davincischools.leo.database.daos.ProjectInput_;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.davincischools.leo.database.daos.ProjectMilestone_;
import org.davincischools.leo.database.daos.Project_;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.davincischools.leo.database.utils.query_helper.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository
    extends JpaRepository<Project, Integer>, AutowiredRepositoryValues {

  default List<Project> getProjects(GetProjectsParams params) {
    checkNotNull(params);

    return getQueryHelper().query(Project.class, project -> configureQuery(project, params));
  }

  static void configureQuery(Entity<?, Project> project, GetProjectsParams params) {
    checkNotNull(project);
    checkNotNull(params);

    project.notDeleted().fetch().requireId(params.getProjectIds());

    project.supplier(
        () ->
            project
                .join(Project_.projectInput, JoinType.LEFT)
                .notDeleted()
                .join(ProjectInput_.userX, JoinType.LEFT)
                .notDeleted(),
        params.getUserXIds());

    if (!params.getIncludeInactive().orElse(false)) {
      project.where(Predicate.isTrue(project.get(Project_.active)));
    }

    if (params.getIncludeTags().orElse(false)) {
      project
          .join(Project_.tags, JoinType.LEFT, Tag::getProject, Project::setTags)
          .notDeleted()
          .fetch();
    }

    if (params.getIncludeInputs().isPresent()) {
      ProjectInputRepository.configureQuery(
          project.join(Project_.projectInput, JoinType.LEFT), params.getIncludeInputs().get());
    }

    if (params.getIncludeFulfillments().orElse(false)) {
      project
          .join(
              Project_.projectInputFulfillments,
              JoinType.LEFT,
              ProjectInputFulfillment::getProject,
              Project::setProjectInputFulfillments)
          .notDeleted()
          .fetch();
    }

    if (params.getIncludeAssignment().isPresent()) {
      AssignmentRepository.configureQuery(
          project.join(Project_.assignment, JoinType.LEFT), params.getIncludeAssignment().get());
    }

    if (params.getIncludeMilestones().orElse(false)) {
      project
          .join(
              Project_.projectMilestones,
              JoinType.LEFT,
              ProjectMilestone::getProject,
              Project::setProjectMilestones)
          .notDeleted()
          .join(
              ProjectMilestone_.projectMilestoneSteps,
              JoinType.LEFT,
              ProjectMilestoneStep::getProjectMilestone,
              ProjectMilestone::setProjectMilestoneSteps)
          .notDeleted()
          .fetch();
    }
  }
}
