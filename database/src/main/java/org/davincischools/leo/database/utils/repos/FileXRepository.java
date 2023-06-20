package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.FileX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileXRepository extends JpaRepository<FileX, Integer> {}
