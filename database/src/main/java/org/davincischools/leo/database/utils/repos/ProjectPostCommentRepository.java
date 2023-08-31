package org.davincischools.leo.database.utils.repos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, Integer> {

  @Setter
  @Getter
  @Builder
  @Accessors(chain = true)
  @NoArgsConstructor
  @AllArgsConstructor
  class FullProjectPostComment {

    private ProjectPostComment projectPostComment;
  }

  default void upsert(FullProjectPostComment FullProjectPostComment) {
    DaoUtils.removeTransientValues(FullProjectPostComment.projectPostComment, this::save);
  }
}
