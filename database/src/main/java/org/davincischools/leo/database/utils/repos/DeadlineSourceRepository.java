package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.DeadlineSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadlineSourceRepository extends JpaRepository<DeadlineSource, Integer> {}
