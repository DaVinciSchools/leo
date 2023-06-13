package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectMilestoneStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMilestoneStepRepository
    extends JpaRepository<ProjectMilestoneStep, Integer> {}
