package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.AssignmentProjectDefinitionId;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentProjectDefinitionRepository
    extends JpaRepository<AssignmentProjectDefinition, AssignmentProjectDefinitionId> {

  default AssignmentProjectDefinition upsert(
      Assignment assignment,
      ProjectDefinition projectDefinition,
      Consumer<AssignmentProjectDefinition> modifier) {
    checkNotNull(assignment);
    checkNotNull(projectDefinition);
    checkNotNull(modifier);

    AssignmentProjectDefinition assignmentProjectDefinition =
        new AssignmentProjectDefinition()
            .setId(
                new AssignmentProjectDefinitionId()
                    .setAssignmentId(assignment.getId())
                    .setProjectDefinitionId(projectDefinition.getId()))
            .setCreationTime(Instant.now())
            .setAssignment(assignment)
            .setProjectDefinition(projectDefinition);

    modifier.accept(assignmentProjectDefinition);

    return saveAndFlush(assignmentProjectDefinition);
  }
}
