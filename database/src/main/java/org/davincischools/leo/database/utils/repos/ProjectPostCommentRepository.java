package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.utils.DaoUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query(
      """
    SELECT ppc
    FROM ProjectPostComment ppc
    LEFT JOIN FETCH ppc.userX
    WHERE ppc.id = :commentId
    AND ppc.deleted IS NULL
    """)
  Optional<ProjectPostComment> getProjectPostCommentById(@Param("commentId") int commentId);

  default Optional<FullProjectPostComment> getFullProjectPostCommentById(int id) {
    return getProjectPostCommentById(id)
        .map(e -> new FullProjectPostComment().setProjectPostComment(e));
  }
}
