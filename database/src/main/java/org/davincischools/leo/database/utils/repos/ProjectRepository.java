package org.davincischools.leo.database.utils.repos;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectMilestone;
import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

  record MilestoneWithSteps(ProjectMilestone milestone, List<ProjectMilestoneStep> steps) {}

  record ProjectWithMilestones(Project project, List<MilestoneWithSteps> milestones) {}

  record ProjectMilestoneStepRow(
      Project project, ProjectMilestone milestone, ProjectMilestoneStep step) {}

  @Query(
      """
      SELECT p, m, s
          FROM Project p
          LEFT JOIN FETCH p.projectInput pi
          LEFT JOIN FETCH p.projectInput.userX
          LEFT JOIN ProjectMilestone m
          ON p.id = m.project.id
          LEFT JOIN ProjectMilestoneStep s
          ON m.id = s.projectMilestone.id
          WHERE p.id = :projectId
          ORDER BY p.creationTime DESC, m.position, s.position""")
  List<ProjectMilestoneStepRow> findFullProjectRowsByProjectId(@Param("projectId") int projectId);

  @Query(
      """
          SELECT p
          FROM Project p
          LEFT JOIN FETCH p.projectInput pi
          LEFT JOIN FETCH p.projectInput.userX
          WHERE pi.userX.id = :userXId
          AND ((:activeOnly <> TRUE) OR p.active)
          ORDER BY p.creationTime DESC""")
  List<Project> findProjectsByUserXId(
      @Param("userXId") int userXId, @Param("activeOnly") boolean activeOnly);

  default Optional<ProjectWithMilestones> findFullProjectById(int projectId) {
    return Optional.ofNullable(
        Iterables.getOnlyElement(
            splitIntoProjectsWithMilestones(findFullProjectRowsByProjectId(projectId)), null));
  }

  private List<ProjectWithMilestones> splitIntoProjectsWithMilestones(
      List<ProjectMilestoneStepRow> rows) {
    ProjectWithMilestones projectWithMilestones = null;
    MilestoneWithSteps milestoneWithSteps = null;

    List<ProjectWithMilestones> projectsWithMilestones = new ArrayList<>();

    for (ProjectMilestoneStepRow row : rows) {
      if (projectWithMilestones == null
          || !Objects.equals(row.project().getId(), projectWithMilestones.project().getId())) {
        milestoneWithSteps = null;
        projectsWithMilestones.add(
            projectWithMilestones = new ProjectWithMilestones(row.project(), new ArrayList<>()));
      }
      if (row.milestone() == null) {
        continue;
      }
      if (milestoneWithSteps == null
          || !Objects.equals(row.milestone().getId(), milestoneWithSteps.milestone().getId())) {
        projectWithMilestones
            .milestones()
            .add(milestoneWithSteps = new MilestoneWithSteps(row.milestone(), new ArrayList<>()));
      }
      if (row.step() == null) {
        continue;
      }
      milestoneWithSteps.steps().add(row.step());
    }

    return projectsWithMilestones;
  }
}
