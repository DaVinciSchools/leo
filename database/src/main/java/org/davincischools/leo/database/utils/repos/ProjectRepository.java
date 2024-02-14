package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory_;
import org.davincischools.leo.database.daos.ProjectInputFulfillment_;
import org.davincischools.leo.database.daos.ProjectInputValue_;
import org.davincischools.leo.database.daos.ProjectInput_;
import org.davincischools.leo.database.daos.ProjectMilestone_;
import org.davincischools.leo.database.daos.Project_;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.davincischools.leo.database.utils.query_helper.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectRepository
    extends JpaRepository<Project, Integer>, AutowiredRepositoryValues {

  default List<Project> getProjects(GetProjectsParams params) {
    checkNotNull(params);

    return getQueryHelper().query(Project.class, project -> configureQuery(project, params));
  }

  static Entity<?, ?, Project> configureQuery(
      Entity<?, ?, Project> project, GetProjectsParams params) {
    checkNotNull(project);
    checkNotNull(params);

    project.fetch().requireId(params.getProjectIds());
    project.join(Project_.projectInput, JoinType.LEFT).fetch();

    project.supplier(
        () ->
            project
                .join(Project_.projectInput, JoinType.LEFT)
                .join(ProjectInput_.userX, JoinType.LEFT),
        params.getUserXIds());

    if (!params.getIncludeInactive().orElse(false)) {
      project.where(Predicate.isTrue(project.get(Project_.active)));
    }

    if (params.getIncludeTags().orElse(false)) {
      project.join(Project_.tags, JoinType.LEFT).notDeleted().fetch();
    }

    if (params.getIncludeInputs().isPresent()) {
      ProjectInputRepository.configureQuery(
              project.join(Project_.projectInput, JoinType.LEFT), params.getIncludeInputs().get())
          .notDeleted();
    }

    if (params.getIncludeFulfillments().orElse(false)) {
      project
          .join(Project_.projectInputFulfillments, JoinType.LEFT)
          .notDeleted()
          .join(ProjectInputFulfillment_.projectInputValue, JoinType.LEFT)
          .notDeleted()
          .join(ProjectInputValue_.projectDefinitionCategory, JoinType.LEFT)
          .notDeleted()
          .join(ProjectDefinitionCategory_.projectDefinitionCategoryType, JoinType.LEFT)
          .notDeleted()
          .fetch();
    }

    if (params.getIncludeAssignment().isPresent()) {
      AssignmentRepository.configureQuery(
              project.join(Project_.assignment, JoinType.LEFT), params.getIncludeAssignment().get())
          .notDeleted();
    }

    if (params.getIncludeMilestones().orElse(false)) {
      project
          .join(Project_.projectMilestones, JoinType.LEFT)
          .notDeleted()
          .join(ProjectMilestone_.projectMilestoneSteps, JoinType.LEFT)
          .notDeleted()
          .fetch();
    }

    if (params.getIncludeProjectPosts().isPresent()) {
      ProjectPostRepository.configureQuery(
              project.join(Project_.projectPosts, JoinType.LEFT),
              params.getIncludeProjectPosts().get())
          .notDeleted();
    }

    return project;
  }

  @Transactional
  default void deeplySaveProjects(Database db, List<Project> projects) {
    checkNotNull(projects);

    db.getProjectRepository().saveAll(projects);
    db.getProjectInputFulfillmentRepository()
        .saveAll(projects.stream().flatMap(p -> p.getProjectInputFulfillments().stream()).toList());
    db.getProjectMilestoneRepository()
        .saveAll(projects.stream().flatMap(p -> p.getProjectMilestones().stream()).toList());
    db.getProjectMilestoneStepRepository()
        .saveAll(
            projects.stream()
                .flatMap(p -> p.getProjectMilestones().stream())
                .flatMap(m -> m.getProjectMilestoneSteps().stream())
                .toList());
  }
}
