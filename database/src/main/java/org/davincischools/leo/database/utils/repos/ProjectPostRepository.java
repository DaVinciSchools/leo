package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.notDeleted;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
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
import org.davincischools.leo.database.utils.PagedQuery;
import org.davincischools.leo.database.utils.repos.custom.CustomEntityManagerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository
    extends JpaRepository<ProjectPost, Integer>, CustomEntityManagerRepository {

  default Page<ProjectPost> getProjectPosts(GetProjectPostsParams params) {
    checkNotNull(params);

    return PagedQuery.getPageResults(
        getEntityManager(),
        ProjectPost.class,
        (u, projectPost, query, builder) -> {
          u.notDeleted(projectPost);
          notDeleted(u.fetch(projectPost, ProjectPost_.userX, JoinType.LEFT));

          // includeTags.
          if (!u.isCount() && params.getIncludeTags().orElse(false)) {
            notDeleted(u.fetch(projectPost, ProjectPost_.tags, JoinType.LEFT));
          }

          // includeComments.
          if (!u.isCount() && params.getIncludeComments().orElse(false)) {
            var projectPostComment =
                notDeleted(u.fetch(projectPost, ProjectPost_.projectPostComments, JoinType.LEFT));
            notDeleted(u.fetch(projectPostComment, ProjectPostComment_.userX, JoinType.LEFT));
          }

          // includeProjects
          if (!u.isCount() && params.getIncludeProjects().orElse(false)) {
            notDeleted(u.fetch(projectPost, ProjectPost_.project, JoinType.LEFT));
          }

          // includeRatings.
          if (!u.isCount() && params.getIncludeRatings().orElse(false)) {
            var rating =
                notDeleted(u.fetch(projectPost, ProjectPost_.projectPostRatings, JoinType.LEFT));
            notDeleted(u.fetch(rating, ProjectPostRating_.userX, JoinType.LEFT));
          }

          // includeAssignments.
          if (!u.isCount()
              && (params.getIncludeAssignments().orElse(false)
                  || params.getIncludeRatings().orElse(false))) {
            var project = notDeleted(u.fetch(projectPost, ProjectPost_.project, JoinType.LEFT));
            var assignment = notDeleted(u.fetch(project, Project_.assignment, JoinType.LEFT));
            var assignmentKnowledgeAndSkills =
                notDeleted(
                    u.fetch(assignment, Assignment_.assignmentKnowledgeAndSkills, JoinType.LEFT));
            notDeleted(
                u.fetch(
                    assignmentKnowledgeAndSkills,
                    AssignmentKnowledgeAndSkill_.knowledgeAndSkill,
                    JoinType.LEFT));
          }

          // projectPostIds.
          if (params.getProjectPostIds().isPresent()) {
            u.where(
                projectPost
                    .get(ProjectPost_.id)
                    .in(ImmutableList.copyOf(params.getProjectPostIds().get())));
          }

          // projectIds.
          if (params.getProjectIds().isPresent()) {
            u.where(
                projectPost
                    .get(ProjectPost_.project)
                    .get(Project_.id)
                    .in(ImmutableList.copyOf(params.getProjectIds().get())));
          }

          // assignmentIds.
          if (params.getAssignmentIds().isPresent()) {
            var project = notDeleted(projectPost.join(ProjectPost_.project, JoinType.INNER));
            u.where(
                project
                    .get(Project_.assignment)
                    .get(Assignment_.id)
                    .in(ImmutableList.copyOf(params.getAssignmentIds().get())));
          }

          // classXIds.
          if (params.getClassXIds().isPresent()) {
            var project = notDeleted(projectPost.join(ProjectPost_.project, JoinType.INNER));
            var assignment = notDeleted(project.join(Project_.assignment, JoinType.INNER));
            u.where(
                assignment
                    .get(Assignment_.classX)
                    .get(ClassX_.id)
                    .in(ImmutableList.copyOf(params.getClassXIds().get())));
          }

          // schoolIds.
          if (params.getSchoolIds().isPresent()) {
            var project = notDeleted(projectPost.join(ProjectPost_.project, JoinType.INNER));
            var assignment = notDeleted(project.join(Project_.assignment, JoinType.INNER));
            var classX = notDeleted(assignment.join(Assignment_.classX, JoinType.INNER));
            u.where(
                classX
                    .get(ClassX_.school)
                    .get(School_.id)
                    .in(ImmutableList.copyOf(params.getSchoolIds().get())));
          }

          // userXIds.
          if (params.getUserXIds().isPresent()) {
            u.where(
                projectPost
                    .get(ProjectPost_.userX)
                    .get(UserX_.id)
                    .in(ImmutableList.copyOf(params.getUserXIds().get())));
          }

          // beingEdited.
          if (params.getBeingEdited().isPresent()) {
            u.where(
                u.isTrueOrFalse(
                    projectPost.get(ProjectPost_.beingEdited), params.getBeingEdited().get()));
          }

          query.orderBy(builder.desc(projectPost.get(ProjectPost_.creationTime)));
        },
        PageRequest.of(params.getPage().orElse(0), params.getPageSize().orElse(Integer.MAX_VALUE)));
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
