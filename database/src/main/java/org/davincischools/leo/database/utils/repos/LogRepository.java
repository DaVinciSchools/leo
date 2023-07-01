package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

  enum Status {
    ERROR,
    SUCCESS
  }
}
