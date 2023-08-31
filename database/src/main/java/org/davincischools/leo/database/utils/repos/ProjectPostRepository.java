package org.davincischools.leo.database.utils.repos;

import com.google.common.collect.ImmutableList;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
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

    private List<Tag> tags;
  }

  @Setter
  @Getter
  @Accessors(chain = true)
  @NoArgsConstructor
  class GetProjectPostsParams {

    @Nullable private Boolean includeTags;

    @Nullable private List<Integer> projectIds;
    @Nullable private List<Integer> assignmentIds;
    @Nullable private List<Integer> classXIds;
    @Nullable private List<Integer> schoolIds;
    @Nullable private List<Integer> userXIds;
    @Nullable private Boolean beingEdited;

    Optional<Boolean> getIncludeTags() {
      return Optional.ofNullable(includeTags);
    }

    Optional<List<Integer>> getProjectIds() {
      return Optional.ofNullable(projectIds);
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
    projectPost.fetch("userX", JoinType.LEFT);

    // WHERE projectIds.
    if (params.getProjectIds().isPresent()) {
      var inPredicate =
          builder.in(((Join<?, ?>) projectPost.fetch("project", JoinType.LEFT)).get("id"));
      params.getProjectIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE assignmentIds.
    if (params.getAssignmentIds().isPresent()) {
      var inPredicate =
          builder.in(
              ((Join<?, ?>)
                      projectPost
                          .fetch("project", JoinType.LEFT)
                          .fetch("assignment", JoinType.LEFT))
                  .get("id"));
      params.getAssignmentIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE classXIds.
    if (params.getClassXIds().isPresent()) {
      var inPredicate =
          builder.in(
              ((Join<?, ?>)
                      projectPost
                          .fetch("project", JoinType.LEFT)
                          .fetch("assignment", JoinType.LEFT)
                          .fetch("classX", JoinType.LEFT))
                  .get("id"));
      params.getClassXIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE schoolIds.
    if (params.getSchoolIds().isPresent()) {
      var inPredicate =
          builder.in(
              ((Join<?, ?>)
                      projectPost
                          .fetch("project", JoinType.LEFT)
                          .fetch("assignment", JoinType.LEFT)
                          .fetch("classX", JoinType.LEFT)
                          .fetch("school", JoinType.LEFT))
                  .get("id"));
      params.getSchoolIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE userXIds.
    if (params.getUserXIds().isPresent()) {
      var inPredicate =
          builder.in(((Join<?, ?>) projectPost.fetch("userX", JoinType.LEFT)).get("id"));
      params.getUserXIds().get().forEach(inPredicate::value);
      whereConjunctions.add(inPredicate);
    }

    // WHERE beingEdited.
    if (params.getBeingEdited().isPresent()) {
      whereConjunctions.add(
          builder.equal(projectPost.get("beingEdited"), params.getBeingEdited().get()));
    }

    // Register WHERE conjunctions.
    query.where(builder.and(whereConjunctions.toArray(new Predicate[0])));

    // ORDER BY.
    query.orderBy(
        Stream.of(builder.desc(projectPost.get("postTime")), builder.desc(projectPost.get("id")))
            .filter(Objects::nonNull)
            .toList());

    // SELECT.
    query.select(
        builder.tuple(
            Stream.of(projectPost).filter(Objects::nonNull).toArray(Selection<?>[]::new)));

    // Parse results.
    List<FullProjectPost> fullProjectPosts = new ArrayList<>();
    for (Tuple row : entityManager.createQuery(query).getResultList()) {
      FullProjectPost fullProjectPost = null;
      if (fullProjectPost == null
          || !Objects.equals(
              fullProjectPost.getProjectPost().getId(), row.get(projectPost).getId())) {
        fullProjectPosts.add(
            fullProjectPost = new FullProjectPost(row.get(projectPost), new ArrayList<>()));
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
