package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, Integer> {}
