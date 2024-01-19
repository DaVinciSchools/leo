package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {}
