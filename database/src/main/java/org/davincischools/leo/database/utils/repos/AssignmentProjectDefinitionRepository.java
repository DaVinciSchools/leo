package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.AssignmentProjectDefinitionId;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentProjectDefinitionRepository
    extends JpaRepository<AssignmentProjectDefinition, AssignmentProjectDefinitionId> {

  default AssignmentProjectDefinition newAssignmentProjectDefinition(
      Assignment assignment, ProjectDefinition projectDefinition) {
    return new AssignmentProjectDefinition()
        .setId(
            new AssignmentProjectDefinitionId()
                .setAssignmentId(assignment.getId())
                .setProjectDefinitionId(projectDefinition.getId()))
        .setAssignment(assignment)
        .setProjectDefinition(projectDefinition);
  }
}
