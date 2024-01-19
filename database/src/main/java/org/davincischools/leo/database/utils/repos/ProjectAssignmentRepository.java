package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Streams;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectAssignment;
import org.davincischools.leo.database.daos.ProjectAssignmentId;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAssignmentRepository
    extends JpaRepository<ProjectAssignment, ProjectAssignmentId> {

  default ProjectAssignmentId createId(Project project, Assignment assignment) {
    checkNotNull(project);
    checkNotNull(assignment);

    return new ProjectAssignmentId()
        .setProjectId(project.getId())
        .setAssignmentId(assignment.getId());
  }

  default ProjectAssignment create(Project project, Assignment assignment) {
    checkNotNull(project);
    checkNotNull(assignment);

    return new ProjectAssignment()
        .setId(createId(project, assignment))
        .setCreationTime(Instant.now())
        .setProject(project)
        .setAssignment(assignment);
  }

  // TODO: What about created time?
  default ProjectAssignment upsert(Project project, Assignment assignment) {
    checkNotNull(project);
    checkNotNull(assignment);

    return save(create(project, assignment));
  }

  List<ProjectAssignment> findAllByProject(Project project);

  @Transactional
  default void setProjectAssignments(Project project, Iterable<Assignment> assignments) {
    DaoUtils.updateCollection(
        findAllByProject(project),
        Streams.stream(assignments).map(assignment -> create(project, assignment)).toList(),
        pa -> pa.getAssignment().getId(),
        this::saveAll,
        this::deleteAll);
  }
}
