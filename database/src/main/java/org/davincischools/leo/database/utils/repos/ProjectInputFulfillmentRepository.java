package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectInputFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInputFulfillmentRepository
    extends JpaRepository<ProjectInputFulfillment, Integer> {}
