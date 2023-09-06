package org.davincischools.leo.database.utils.repos;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Streams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.daos.Assignment_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.ProjectPostComment_;
import org.davincischools.leo.database.daos.ProjectPost_;
import org.davincischools.leo.database.daos.Project_;
import org.davincischools.leo.database.daos.School_;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.daos.UserX_;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository.FullProjectPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Integer> {

  @Setter
  @Getter
  @Builder
  @Accessors(chain = true)
  @NoArgsConstructor
  @AllArgsConstructor
  class FullProjectPost {

    private ProjectPost projectPost;

    @Default
    private final SortedMap<Instant, FullProjectPostComment> projectPostComments =
        new TreeMap<>(Comparator.<Instant>naturalOrder().reversed());

    @Default
    private final SetMultimap</* UserX.id= */ Integer, String> tags = HashMultimap.create();
  }

  @Setter
  @Getter
  @Accessors(chain = true)
  @NoArgsConstructor
  class GetProjectPostsParams {

    @Nullable private Boolean includeTags;
    @Nullable private Boolean includeComments;
    @Nullable private Boolean includeProjects;

    @Nullable private List<Integer> projectIds;
    @Nullable private List<Integer> projectPostIds;
    @Nullable private List<Integer> assignmentIds;
    @Nullable private List<Integer> classXIds;
    @Nullable private List<Integer> schoolIds;
    @Nullable private List<Integer> userXIds;
    @Nullable private Boolean beingEdited;

    Optional<Boolean> getIncludeTags() {
      return Optional.ofNullable(includeTags);
    }

    Optional<Boolean> getIncludeComments() {
      return Optional.ofNullable(includeComments);
    }

    Optional<Boolean> getIncludeProjects() {
      return Optional.ofNullable(includeProjects);
    }

    Optional<List<Integer>> getProjectIds() {
      return Optional.ofNullable(projectIds);
    }

    Optional<List<Integer>> getProjectPostIds() {
      return Optional.ofNullable(projectPostIds);
    }

    Optional<List<Integer>> getAssignmentIds() {
      return Optional.ofNullable(assignmentIds);
    }

    Optional<List<Integer>> getClassXIds() {
      return Optional.ofNullable(classXIds);
    }

    Optional<List<Integer>> getSchoolIds() {
      return Optional.ofNullable(schoolIds);
    }

    Optional<List<Integer>> getUserXIds() {
      return Optional.ofNullable(userXIds);
    }

    Optional<Boolean> getBeingEdited() {
      return Optional.ofNullable(beingEdited);
    }
  }

  default List<FullProjectPost> getProjectPosts(
      EntityManager entityManager, GetProjectPostsParams params) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    // Set up tables.
    CriteriaQuery<Tuple> query = builder.createTupleQuery();
    List<Predicate> whereConjunctions = new ArrayList<>();

    // FROM ProjectPost.
    Root<ProjectPost> projectPost = query.from(ProjectPost.class);
    projectPost.fetch(ProjectPost_.userX, JoinType.LEFT);

    // Fetch includeProjects
    if (params.getIncludeProjects().orElse(false)) {
      projectPost.fetch(ProjectPost_.project, JoinType.LEFT);
    }

    // JOIN includeTags.
    // TODO: This should be a separate query because it's going to cause a huge result.
    Join<ProjectPost, Tag> tag = null;
    if (params.getIncludeTags().orElse(false)) {
      tag = projectPost.join(ProjectPost_.tags, JoinType.LEFT);
    }

    // JOIN includeComments.
    Join<ProjectPost, ProjectPostComment> projectPostComment = null;
    if (params.getIncludeComments().orElse(false)) {
      projectPostComment = projectPost.join(ProjectPost_.projectPostComments, JoinType.LEFT);
      projectPostComment.fetch(ProjectPostComment_.userX, JoinType.LEFT);
    }

    // WHERE - General
    whereConjunctions.add(builder.isNull(projectPost.get(ProjectPost_.deleted)));

    // WHERE projectIds.
    if (params.getProjectIds().isPresent()) {
      var inPredicate =
          builder.in(
              DaoUtils.toJoin(projectPost.fetch(ProjectPost_.project, JoinType.LEFT))
                  .get(Project_.id));
      params.getProjectIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE projectIds.
    if (params.getProjectPostIds().isPresent()) {
      var inPredicate = builder.in(projectPost.get(ProjectPost_.id));
      params.getProjectPostIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE assignmentIds.
    if (params.getAssignmentIds().isPresent()) {
      var inPredicate =
          builder.in(
              DaoUtils.toJoin(
                      projectPost
                          .fetch(ProjectPost_.project, JoinType.LEFT)
                          .fetch(Project_.assignment, JoinType.LEFT))
                  .get(Assignment_.id));
      params.getAssignmentIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE classXIds.
    if (params.getClassXIds().isPresent()) {
      var inPredicate =
          builder.in(
              DaoUtils.toJoin(
                      projectPost
                          .fetch(ProjectPost_.project, JoinType.LEFT)
                          .fetch(Project_.assignment, JoinType.LEFT)
                          .fetch(Assignment_.classX, JoinType.LEFT))
                  .get(ClassX_.id));
      params.getClassXIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE schoolIds.
    if (params.getSchoolIds().isPresent()) {
      var inPredicate =
          builder.in(
              DaoUtils.toJoin(
                      projectPost
                          .fetch(ProjectPost_.project, JoinType.LEFT)
                          .fetch(Project_.assignment, JoinType.LEFT)
                          .fetch(Assignment_.classX, JoinType.LEFT)
                          .fetch(ClassX_.school, JoinType.LEFT))
                  .get(School_.id));
      params.getSchoolIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE userXIds.
    if (params.getUserXIds().isPresent()) {
      var inPredicate =
          builder.in(
              DaoUtils.toJoin(projectPost.fetch(ProjectPost_.userX, JoinType.LEFT)).get(UserX_.id));
      params.getUserXIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE beingEdited.
    if (params.getBeingEdited().isPresent()) {
      whereConjunctions.add(
          builder.equal(projectPost.get(ProjectPost_.beingEdited), params.getBeingEdited().get()));
    }

    // Register WHERE conjunctions.
    query.where(builder.and(whereConjunctions.toArray(new Predicate[0])));

    // ORDER BY.
    query.orderBy(
        Stream.of(
                builder.desc(projectPost.get(ProjectPost_.postTime)),
                builder.desc(projectPost.get(ProjectPost_.id)),
                projectPostComment != null
                    ? builder.desc(projectPostComment.get(ProjectPostComment_.id))
                    : null)
            .filter(Objects::nonNull)
            .toList());

    // SELECT.
    query.select(
        builder.tuple(
            Stream.of(projectPost, projectPostComment, tag)
                .filter(Objects::nonNull)
                .toArray(Selection<?>[]::new)));

    // Parse results.
    List<FullProjectPost> fullProjectPosts = new ArrayList<>();
    FullProjectPost fullProjectPost = null;
    for (Tuple row : entityManager.createQuery(query).getResultList()) {
      if (fullProjectPost == null
          || !Objects.equals(
              fullProjectPost.getProjectPost().getId(), row.get(projectPost).getId())) {
        fullProjectPosts.add(
            fullProjectPost = new FullProjectPost().setProjectPost(row.get(projectPost)));
      }
      if (tag != null && row.get(tag) != null) {
        fullProjectPost.getTags().put(row.get(tag).getUserX().getId(), row.get(tag).getText());
      }
      if (projectPostComment != null && row.get(projectPostComment) != null) {
        fullProjectPost
            .getProjectPostComments()
            .put(
                row.get(projectPostComment).getPostTime(),
                new FullProjectPostComment().setProjectPostComment(row.get(projectPostComment)));
      }
    }

    return fullProjectPosts;
  }

  default void upsert(Database db, UserX tagUserX, FullProjectPost fullProjectPost) {
    DaoUtils.removeTransientValues(fullProjectPost.projectPost, this::save);
    DaoUtils.updateCollection(
        db.getTagRepository().findAllTagsByProjectPost(fullProjectPost.getProjectPost()).stream()
            .filter(tag -> Objects.equals(tag.getUserX().getId(), tagUserX.getId()))
            .map(Tag::getText)
            .toList(),
        fullProjectPost.tags.get(tagUserX.getId()),
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
                                    .setProjectPost(fullProjectPost.getProjectPost())
                                    .setText(text))
                        .toList()),
        (Iterable<String> deleteTags) ->
            db.getTagRepository()
                .deleteTagsByProjectPost(
                    tagUserX,
                    fullProjectPost.getProjectPost(),
                    ImmutableList.<String>builder().addAll(deleteTags).add("").build()));
  }
}
