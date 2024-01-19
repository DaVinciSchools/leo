package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Streams;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputAssignment;
import org.davincischools.leo.database.daos.ProjectInputAssignmentId;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInputAssignmentRepository
    extends JpaRepository<ProjectInputAssignment, ProjectInputAssignmentId> {

  default ProjectInputAssignmentId createId(ProjectInput projectInput, Assignment assignment) {
    checkNotNull(projectInput);
    checkNotNull(assignment);

    return new ProjectInputAssignmentId()
        .setProjectInputId(projectInput.getId())
        .setAssignmentId(assignment.getId());
  }

  default ProjectInputAssignment create(ProjectInput projectInput, Assignment assignment) {
    checkNotNull(projectInput);
    checkNotNull(assignment);

    return new ProjectInputAssignment()
        .setId(createId(projectInput, assignment))
        .setCreationTime(Instant.now())
        .setProjectInput(projectInput)
        .setAssignment(assignment);
  }

  // TODO: What about created time?
  default ProjectInputAssignment upsert(ProjectInput projectInput, Assignment assignment) {
    checkNotNull(projectInput);
    checkNotNull(assignment);

    return save(create(projectInput, assignment));
  }

  List<ProjectInputAssignment> findAllByProjectInput(ProjectInput projectInput);

  @Transactional
  default void setProjectInputAssignments(
      ProjectInput projectInput, Iterable<Assignment> assignments) {
    DaoUtils.updateCollection(
        findAllByProjectInput(projectInput),
        Streams.stream(assignments).map(assignment -> create(projectInput, assignment)).toList(),
        pa -> pa.getAssignment().getId(),
        this::saveAll,
        this::deleteAll);
  }
}
