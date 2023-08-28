package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  default Tag create(UserX userX, Project project, String text) {
    checkNotNull(userX);
    checkNotNull(project);
    checkNotNull(text);

    return new Tag()
        .setCreationTime(Instant.now())
        .setUserX(userX)
        .setProject(project)
        .setText(text);
  }

  default Tag create(UserX userX, ProjectPost projectPost, String text) {
    checkNotNull(userX);
    checkNotNull(projectPost);
    checkNotNull(text);

    return new Tag()
        .setCreationTime(Instant.now())
        .setUserX(userX)
        .setProjectPost(projectPost)
        .setText(text);
  }

  default Tag create(UserX userX, ProjectPostComment projectPostComment, String text) {
    checkNotNull(userX);
    checkNotNull(projectPostComment);
    checkNotNull(text);

    return new Tag()
        .setCreationTime(Instant.now())
        .setUserX(userX)
        .setProjectPostComment(projectPostComment)
        .setText(text);
  }

  @Query(
      """
          SELECT DISTINCT(t.text)
          FROM Tag t
          WHERE t.userX = (:userX)
          """)
  List<String> findAllTagTextsByUserX(@Param("userX") UserX userX);

  @Query(
      """
          SELECT t
          FROM Tag t
          WHERE t.projectPost = (:projectPost)
          """)
  List<Tag> findAllTagsByProjectPost(@Param("projectPost") ProjectPost projectPost);

  @Modifying
  @Query(
      """
          DELETE FROM Tag t
          WHERE t.userX = (:userX)
          AND t.projectPost = (:projectPost)
          AND t.text IN (:texts)
          """)
  void deleteTagsByProjectPost(
      @Param("userX") UserX userX,
      @Param("projectPost") ProjectPost projectPost,
      @Param("texts") Iterable<String> texts);
}
