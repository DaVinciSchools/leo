package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectPostRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRatingRepository extends JpaRepository<ProjectPostRating, Integer> {}
