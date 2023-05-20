package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.AssignmentProjectDefinitionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentProjectDefinitionRepository
    extends JpaRepository<AssignmentProjectDefinition, AssignmentProjectDefinitionId> {
}
