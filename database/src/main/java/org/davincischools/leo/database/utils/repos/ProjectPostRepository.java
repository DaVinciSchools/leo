package org.davincischools.leo.database.utils.repos;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Integer> {

  public record FullProjectPost(ProjectPost projectPost, List<Tag> tags) {}

  record ProjectPostRow(ProjectPost projectPost, Tag tag) {}

  @Deprecated
  @Query(
      "SELECT pp"
          + " FROM ProjectPost pp"
          + " INNER JOIN FETCH pp.userX"
          + " INNER JOIN pp.project p"
          + " INNER JOIN p.projectInput"
          + " WHERE p.id = (:projectId)")
  List<ProjectPost> findAllByProjectId(@Param("projectId") int projectId);

  @Query(
      """
          SELECT pp, t
          FROM ProjectPost pp
          LEFT JOIN Tag t
          ON pp.id = t.projectPost.id
          WHERE pp.project = (:project)
          AND (
              ((:beingEdited) IS NULL)
              OR (pp.beingEdited = (:beingEdited)))
          ORDER BY pp.id DESC
          """)
  List<ProjectPostRow> findProjectPostRowsByProject(
      @Param("project") Project project, @Nullable @Param("beingEdited") Boolean beingEdited);

  default List<FullProjectPost> findProjectPostsByProject(
      Project project, @Nullable Boolean beingEdited) {
    List<FullProjectPost> projectPosts = new ArrayList<>();
    FullProjectPost projectPost = null;
    List<Tag> tags = null;

    for (var row : findProjectPostRowsByProject(project, beingEdited)) {
      if (projectPost == null
          || !Objects.equals(projectPost.projectPost().getId(), row.projectPost().getId())) {
        projectPosts.add(
            projectPost = new FullProjectPost(row.projectPost(), tags = new ArrayList<>()));
      }
      if (row.tag() != null) {
        tags.add(row.tag());
      }
    }

    return projectPosts;
  }

  default void upsert(Database db, UserX tagUserX, FullProjectPost fullProjectPost) {
    DaoUtils.removeTransientValues(fullProjectPost.projectPost, this::save);
    DaoUtils.updateCollection(
        db.getTagRepository().findAllTagsByProjectPost(fullProjectPost.projectPost()).stream()
            .filter(tag -> Objects.equals(tag.getUserX().getId(), tagUserX.getId()))
            .map(Tag::getText)
            .toList(),
        fullProjectPost.tags.stream()
            .filter(tag -> Objects.equals(tag.getUserX().getId(), tagUserX.getId()))
            .map(Tag::getText)
            .toList(),
        Function.identity(),
        (Iterable<String> saveTags) ->
            db.getTagRepository()
                .saveAll(
                    Streams.stream(saveTags)
                        .map(
                            text ->
                                new Tag()
                                    .setCreationTime(Instant.now())
                                    .setUserX(tagUserX)
                                    .setProjectPost(fullProjectPost.projectPost())
                                    .setText(text))
                        .toList()),
        (Iterable<String> deleteTags) ->
            db.getTagRepository()
                .deleteTagsByProjectPost(
                    tagUserX,
                    fullProjectPost.projectPost(),
                    ImmutableList.<String>builder().addAll(deleteTags).add("").build()));
  }
}
