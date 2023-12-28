package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import org.davincischools.leo.database.daos.FileX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileXRepository extends JpaRepository<FileX, Integer> {

  @Query(
      """
      SELECT f
      FROM FileX f
      LEFT JOIN FETCH f.userX u
      LEFT JOIN FETCH u.adminX
      LEFT JOIN FETCH u.teacher
      LEFT JOIN FETCH u.student
      LEFT JOIN FETCH u.district
      WHERE f.id = :id
      AND f.deleted IS NULL
      """)
  Optional<FileX> findByIdWithUserX(@Param("id") Integer id);
}
