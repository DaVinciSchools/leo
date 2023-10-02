package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.addJoinOn;
import static org.davincischools.leo.database.utils.DaoUtils.notDeleted;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.Assignment_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment_;
import org.davincischools.leo.database.daos.ProjectPostRating_;
import org.davincischools.leo.database.daos.ProjectPost_;
import org.davincischools.leo.database.daos.Project_;
import org.davincischools.leo.database.daos.School_;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.daos.UserX_;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Integer> {

  static List<ProjectPost> getProjectPosts(
      EntityManager entityManager, GetProjectPostsParams params) {
    checkNotNull(entityManager);
    checkNotNull(params);

    var builder = entityManager.getCriteriaBuilder();
    var where = new ArrayList<Predicate>();

    // From projectPost.
    var query = builder.createQuery(ProjectPost.class);
    var projectPost = notDeleted(where, query.from(ProjectPost.class));

    // Build query.
    getProjectPosts(params, builder, projectPost, where);

    // Select.
    query
        .select(projectPost)
        .distinct(true)
        .where(where.toArray(new Predicate[0]))
        .orderBy(builder.desc(projectPost.get(ProjectPost_.postTime)));
    return entityManager.createQuery(query).getResultList();
  }

  static From<?, ProjectPost> getProjectPosts(
      GetProjectPostsParams params,
      CriteriaBuilder builder,
      From<?, ProjectPost> projectPost,
      List<Predicate> where) {
    checkNotNull(params);
    checkNotNull(builder);
    checkNotNull(projectPost);
    checkNotNull(where);

    // includeTags.
    if (params.getIncludeTags().orElse(false)) {
      notDeleted(projectPost.fetch(ProjectPost_.tags, JoinType.LEFT));
    }

    // includeComments.
    if (params.getIncludeComments().orElse(false)) {
      var projectPostComment =
          notDeleted(projectPost.fetch(ProjectPost_.projectPostComments, JoinType.LEFT));
      notDeleted(projectPostComment.fetch(ProjectPostComment_.userX, JoinType.LEFT));
    }

    // includeProjects
    if (params.getIncludeProjects().orElse(false)) {
      notDeleted(projectPost.fetch(ProjectPost_.project, JoinType.LEFT));
    }

    // includeRatings.
    if (params.getIncludeRatings().orElse(false)) {
      var rating = notDeleted(projectPost.fetch(ProjectPost_.projectPostRatings, JoinType.LEFT));
      notDeleted(rating.fetch(ProjectPostRating_.userX, JoinType.LEFT));
    }

    // includeAssignments.
    if (params.getIncludeAssignments().orElse(false)) {
      var project = notDeleted(projectPost.fetch(ProjectPost_.project, JoinType.LEFT));
      var assignment = notDeleted(project.fetch(Project_.assignment, JoinType.LEFT));
      var assignmentKnowledgeAndSkills =
          notDeleted(assignment.fetch(Assignment_.assignmentKnowledgeAndSkills, JoinType.LEFT));
      notDeleted(
          assignmentKnowledgeAndSkills.fetch(
              AssignmentKnowledgeAndSkill_.knowledgeAndSkill.getName(), JoinType.LEFT));
    }

    // projectPostIds.
    if (params.getProjectPostIds().isPresent()) {
      where.add(
          projectPost
              .get(ProjectPost_.id)
              .in(ImmutableList.copyOf(params.getProjectPostIds().get())));
    }

    // projectIds.
    if (params.getProjectIds().isPresent()) {
      var project = notDeleted(projectPost.fetch(ProjectPost_.project, JoinType.INNER));
      addJoinOn(
          project, project.get(Project_.id).in(ImmutableList.copyOf(params.getProjectIds().get())));
    }

    // assignmentIds.
    if (params.getAssignmentIds().isPresent()) {
      var project = notDeleted(projectPost.fetch(ProjectPost_.project, JoinType.INNER));
      var assignment = notDeleted(project.fetch(Project_.assignment, JoinType.INNER));
      addJoinOn(
          assignment,
          assignment.get(Assignment_.id).in(ImmutableList.copyOf(params.getAssignmentIds().get())));
    }

    // classXIds.
    if (params.getClassXIds().isPresent()) {
      var project = notDeleted(projectPost.fetch(ProjectPost_.project, JoinType.INNER));
      var assignment = notDeleted(project.fetch(Project_.assignment, JoinType.INNER));
      var classX = notDeleted(assignment.fetch(Assignment_.classX, JoinType.INNER));
      addJoinOn(
          classX, classX.get(ClassX_.id).in(ImmutableList.copyOf(params.getClassXIds().get())));
    }

    // schoolIds.
    if (params.getSchoolIds().isPresent()) {
      var project = notDeleted(projectPost.fetch(ProjectPost_.project, JoinType.INNER));
      var assignment = notDeleted(project.fetch(Project_.assignment, JoinType.INNER));
      var classX = notDeleted(assignment.fetch(Assignment_.classX, JoinType.INNER));
      var school = notDeleted(classX.fetch(ClassX_.school, JoinType.INNER));
      addJoinOn(
          school, school.get(School_.id).in(ImmutableList.copyOf(params.getSchoolIds().get())));
    }

    // userXIds.
    if (params.getUserXIds().isPresent()) {
      var userX = notDeleted(projectPost.fetch(ProjectPost_.userX, JoinType.INNER));
      addJoinOn(userX, userX.get(UserX_.id).in(ImmutableList.copyOf(params.getUserXIds().get())));
    }

    // beingEdited.
    if (params.getBeingEdited().isPresent()) {
      where.add(
          builder.equal(projectPost.get(ProjectPost_.beingEdited), params.getBeingEdited().get()));
    }

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
