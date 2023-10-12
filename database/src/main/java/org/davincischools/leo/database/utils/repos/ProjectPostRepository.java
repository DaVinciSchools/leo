package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill_;
import org.davincischools.leo.database.daos.Assignment_;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.ProjectPostComment_;
import org.davincischools.leo.database.daos.ProjectPostRating;
import org.davincischools.leo.database.daos.ProjectPost_;
import org.davincischools.leo.database.daos.Project_;
import org.davincischools.leo.database.daos.School_;
import org.davincischools.leo.database.daos.Tag;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.daos.UserX_;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.QueryHelper.QueryHelperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository
    extends JpaRepository<ProjectPost, Integer>, AutowiredRepositoryValues {

  default List<ProjectPost> getProjectPosts(GetProjectPostsParams params) {
    checkNotNull(params);

    return getQueryHelper()
        .query(
            ProjectPost.class,
            (u, projectPost, builder) -> configureQuery(u, projectPost, builder, params));
  }

  public static void configureQuery(
      QueryHelperUtils u,
      From<?, ProjectPost> projectPost,
      CriteriaBuilder builder,
      GetProjectPostsParams params) {
    checkNotNull(u);
    checkNotNull(projectPost);
    checkNotNull(builder);
    checkNotNull(params);

    u.notDeleted(projectPost);
    u.notDeleted(u.fetch(projectPost, ProjectPost_.userX, JoinType.LEFT));

    if (params.getIncludeTags().orElse(false)) {
      u.notDeleted(
          u.fetch(
              projectPost,
              ProjectPost_.tags,
              JoinType.LEFT,
              Tag::getProjectPost,
              ProjectPost::setTags));
    }

    if (params.getIncludeComments().orElse(false)) {
      var projectPostComment =
          u.notDeleted(
              u.fetch(
                  projectPost,
                  ProjectPost_.projectPostComments,
                  JoinType.LEFT,
                  ProjectPostComment::getProjectPost,
                  ProjectPost::setProjectPostComments));
      u.notDeleted(u.fetch(projectPostComment, ProjectPostComment_.userX, JoinType.LEFT));
    }

    if (params.getIncludeProjects().orElse(false)) {
      u.notDeleted(u.fetch(projectPost, ProjectPost_.project, JoinType.LEFT));
    }

    if (params.getIncludeRatings().orElse(false)) {
      u.notDeleted(
          u.fetch(
              projectPost,
              ProjectPost_.projectPostRatings,
              JoinType.LEFT,
              ProjectPostRating::getProjectPost,
              ProjectPost::setProjectPostRatings));
    }

    if (params.getIncludeAssignments().orElse(false) || params.getIncludeRatings().orElse(false)) {
      var project = u.notDeleted(u.fetch(projectPost, ProjectPost_.project, JoinType.LEFT));
      var assignment = u.notDeleted(u.fetch(project, Project_.assignment, JoinType.LEFT));
      var assignmentKnowledgeAndSkills =
          u.notDeleted(
              u.fetch(
                  assignment,
                  Assignment_.assignmentKnowledgeAndSkills,
                  JoinType.LEFT,
                  AssignmentKnowledgeAndSkill::getAssignment,
                  Assignment::setAssignmentKnowledgeAndSkills));
      u.notDeleted(
          u.fetch(
              assignmentKnowledgeAndSkills,
              AssignmentKnowledgeAndSkill_.knowledgeAndSkill,
              JoinType.LEFT));
    }

    if (params.getProjectPostIds().isPresent()) {
      u.where(
          projectPost
              .get(ProjectPost_.id)
              .in(ImmutableList.copyOf(params.getProjectPostIds().get())));
    }

    if (params.getProjectIds().isPresent()) {
      var project = u.notDeleted(u.fetch(projectPost, ProjectPost_.project, JoinType.INNER));
      u.addJoinOn(
          project, project.get(Project_.id).in(ImmutableList.copyOf(params.getProjectIds().get())));
    }

    if (params.getClassXIds().isPresent()) {
      var project = u.notDeleted(u.fetch(projectPost, ProjectPost_.project, JoinType.INNER));
      var assignment = u.notDeleted(u.fetch(project, Project_.assignment, JoinType.INNER));
      var classX = u.notDeleted(u.fetch(assignment, Assignment_.classX, JoinType.INNER));
      u.addJoinOn(
          classX, classX.get(ClassX_.id).in(ImmutableList.copyOf(params.getClassXIds().get())));
    }

    if (params.getSchoolIds().isPresent()) {
      var project = u.notDeleted(u.fetch(projectPost, ProjectPost_.project, JoinType.INNER));
      var assignment = u.notDeleted(u.fetch(project, Project_.assignment, JoinType.INNER));
      var classX = u.notDeleted(u.fetch(assignment, Assignment_.classX, JoinType.INNER));
      var school = u.notDeleted(u.fetch(classX, ClassX_.school, JoinType.INNER));
      u.addJoinOn(
          school, school.get(School_.id).in(ImmutableList.copyOf(params.getSchoolIds().get())));
    }

    if (params.getUserXIds().isPresent()) {
      var userX = u.notDeleted(u.fetch(projectPost, ProjectPost_.userX, JoinType.INNER));
      u.addJoinOn(userX, userX.get(UserX_.id).in(ImmutableList.copyOf(params.getUserXIds().get())));
    }

    if (params.getBeingEdited().isPresent()) {
      u.where(
          builder.equal(projectPost.get(ProjectPost_.beingEdited), params.getBeingEdited().get()));
    }

    u.orderBy(builder.desc(projectPost.get(ProjectPost_.creationTime)));
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
