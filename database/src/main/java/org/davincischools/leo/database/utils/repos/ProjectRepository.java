package org.davincischools.leo.database.utils.repos;

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

  @Query(
      "SELECT p"
          + " FROM Project p"
          + " INNER JOIN FETCH p.projectInput pi"
          + " WHERE pi.userX.id = (:userXId)")
  Iterable<Project> findAllByUserXId(@Param("userXId") int userXId);

  @Query(
      "SELECT p"
          + " FROM Project p"
          + " INNER JOIN FETCH p.projectInput pi"
          + " WHERE pi.userX.id = (:userXId)"
          + " AND p.active")
  Iterable<Project> findAllActiveByUserXId(@Param("userXId") int userXId);

  @Query("SELECT p FROM Project p INNER JOIN FETCH p.projectInput WHERE p.id = (:projectId)")
  Optional<Project> findById(@Param("projectId") int projectId);

  @Query(
      "SELECT p, m, s"
          + " FROM Project p"
          + " LEFT JOIN FETCH p.projectInput pi"
          + " LEFT JOIN FETCH p.projectInput.userX"
          + " LEFT JOIN ProjectMilestone m"
          + " ON p.id = m.project.id"
          + " LEFT JOIN ProjectMilestoneStep s"
          + " ON m.id = s.projectMilestone.id"
          + " WHERE p.id = (:projectId)"
          + " ORDER BY m.position, s.position")
  Iterable<Object[]> _internal_findByProjectId(@Param("projectId") int projectId);

  default Optional<ProjectWithMilestones> findByProjectId(int projectId) {
    ProjectWithMilestones result = null;
    MilestoneWithSteps lastMilestone = null;
    for (Object[] row : _internal_findByProjectId(projectId)) {
      Project project = (Project) row[0];
      ProjectMilestone milestone = (ProjectMilestone) row[1];
      ProjectMilestoneStep step = (ProjectMilestoneStep) row[2];

      if (result == null) {
        result = new ProjectWithMilestones(project, new ArrayList<>());
      }

      if (milestone == null) {
        break;
      }

      if (lastMilestone == null
          || !Objects.equals(lastMilestone.milestone().getId(), milestone.getId())) {
        result
            .milestones()
            .add(lastMilestone = new MilestoneWithSteps(milestone, new ArrayList<>()));
      }

      if (step != null) {
        lastMilestone.steps().add(step);
      }
    }
    return Optional.ofNullable(result);
  }
}
