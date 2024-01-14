package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.query_helper.QueryHelper.DEFAULT_PAGE_SIZE;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.davincischools.leo.database.daos.ProjectInputFulfillment_;
import org.davincischools.leo.database.daos.ProjectInputValue_;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment_;
import org.davincischools.leo.database.daos.ProjectPostRating_;
import org.davincischools.leo.database.daos.ProjectPost_;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.davincischools.leo.database.utils.query_helper.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository
    extends JpaRepository<ProjectPost, Integer>, AutowiredRepositoryValues {

  @Query("SELECT id FROM #{#entityName}")
  List<Integer> getAllIds();

  default Page<ProjectPost> getProjectPosts(GetProjectPostsParams params) {
    checkNotNull(params);

    return getQueryHelper()
        .query(
            ProjectPost.class,
            projectPost -> configureQuery(projectPost, params),
            params
                .getPage()
                .map(
                    page ->
                        Pageable.ofSize(params.getPageSize().orElse(DEFAULT_PAGE_SIZE))
                            .withPage(page))
                .orElse(Pageable.unpaged()));
  }

  static Entity<?, ProjectPost> configureQuery(
      Entity<?, ProjectPost> projectPost, GetProjectPostsParams params) {
    checkNotNull(projectPost);
    checkNotNull(params);

    projectPost.fetch().requireId(params.getProjectPostIds());
    projectPost.join(ProjectPost_.userX, JoinType.LEFT).fetch().requireId(params.getUserXIds());

    if (params.getIncludeTags().orElse(false)) {
      projectPost.join(ProjectPost_.tags, JoinType.LEFT).notDeleted().fetch();
    }

    if (params.getIncludeComments().orElse(false)) {
      var commentUserX =
          projectPost
              .join(ProjectPost_.projectPostComments, JoinType.LEFT)
              .notDeleted()
              .join(ProjectPostComment_.userX, JoinType.LEFT)
              .fetch();
      commentUserX.requireNotId(params.getExcludeCommentsByUserXIds());
    }

    var project =
        projectPost.supplier(
            () -> projectPost.join(ProjectPost_.project, JoinType.LEFT), params.getProjectIds());
    if (params.getIncludeProjects().isPresent()) {
      ProjectRepository.configureQuery(project.get(), params.getIncludeProjects().get());
    }

    if (params.getIncludeRatings().orElse(false)) {
      var projectPostRating =
          projectPost.join(ProjectPost_.projectPostRatings, JoinType.LEFT).notDeleted().fetch();
      projectPostRating.join(ProjectPostRating_.userX, JoinType.LEFT).fetch();
      var projectInputValue =
          projectPostRating
              .join(ProjectPostRating_.projectInputFulfillment, JoinType.LEFT)
              .join(ProjectInputFulfillment_.projectInputValue, JoinType.LEFT)
              .notDeleted()
              .fetch();
      projectInputValue
          .join(ProjectInputValue_.motivationValue, JoinType.LEFT)
          .notDeleted()
          .fetch();
      projectInputValue
          .join(ProjectInputValue_.knowledgeAndSkillValue, JoinType.LEFT)
          .notDeleted()
          .fetch();
    }

    if (params.getBeingEdited().isPresent()) {
      projectPost.on(
          Predicate.eq(projectPost.get(ProjectPost_.beingEdited), params.getBeingEdited().get()));
    }

    projectPost.orderByDesc(projectPost.get(ProjectPost_.creationTime));

    return projectPost;
  }

  default void upsert(Database db, UserX tagUserX, ProjectPost projectPost) {
    DaoUtils.removeTransientValues(projectPost, this::save);
    DaoUtils.updateCollection(
        db.getTagRepository().findAllTagsByProjectPost(projectPost).stream()
            .filter(tag -> Objects.equals(tag.getUserX().getId(), tagUserX.getId()))
            .map(Tag::getText)
            .toList(),
        projectPost.getTags().stream()
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
                                    .setProjectPost(projectPost)
                                    .setText(text))
                        .toList()),
        (Iterable<String> deleteTags) ->
            db.getTagRepository()
                .deleteTagsByProjectPost(
                    tagUserX,
                    projectPost,
                    ImmutableList.<String>builder().addAll(deleteTags).add("").build()));
  }
}
