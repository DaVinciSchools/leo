package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectPostRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPostRatingRepository extends JpaRepository<ProjectPostRating, Integer> {
  enum RatingType {
    INITIAL_1_TO_5
  }
}
