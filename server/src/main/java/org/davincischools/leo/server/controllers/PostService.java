package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.ProjectPostRating;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectPostsParams;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository.FullProjectPostComment;
import org.davincischools.leo.database.utils.repos.ProjectPostRepository;
import org.davincischools.leo.protos.post_service.DeleteProjectPostCommentRequest;
import org.davincischools.leo.protos.post_service.DeleteProjectPostCommentResponse;
import org.davincischools.leo.protos.post_service.GetProjectPostsRequest;
import org.davincischools.leo.protos.post_service.GetProjectPostsResponse;
import org.davincischools.leo.protos.post_service.UpsertProjectPostCommentRequest;
import org.davincischools.leo.protos.post_service.UpsertProjectPostCommentResponse;
import org.davincischools.leo.protos.post_service.UpsertProjectPostRatingRequest;
import org.davincischools.leo.protos.post_service.UpsertProjectPostRatingResponse;
import org.davincischools.leo.protos.post_service.UpsertProjectPostRequest;
import org.davincischools.leo.protos.post_service.UpsertProjectPostResponse;
import org.davincischools.leo.server.utils.ProtoDaoUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostService {

  @Autowired EntityManager entityManager;
  @Autowired Database db;

  @PostMapping(value = "/api/protos/PostService/GetProjectPosts")
  @ResponseBody
  public GetProjectPostsResponse getProjectPosts(
      @RequestBody Optional<GetProjectPostsRequest> optionalRequest, HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(GetProjectPostsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              // TODO: Determine what sort of security to have on project posts.

              var response = GetProjectPostsResponse.newBuilder();

              ProjectPostRepository.getProjectPosts(
                      entityManager,
                      new GetProjectPostsParams()
                          .setIncludeTags(
                              request.hasIncludeTags() ? request.getIncludeTags() : null)
                          .setIncludeComments(
                              request.hasIncludeComments() ? request.getIncludeComments() : null)
                          .setIncludeProjects(
                              request.hasIncludeProjects() ? request.getIncludeProjects() : null)
                          .setIncludeRatings(
                              request.hasIncludeRatings() ? request.getIncludeRatings() : null)
                          .setIncludeAssignments(
                              request.hasIncludeAssignments()
                                  ? request.getIncludeAssignments()
                                  : null)
                          .setProjectIds(
                              request.getProjectIdsList().isEmpty()
                                  ? null
                                  : request.getProjectIdsList())
                          .setProjectPostIds(
                              request.getProjectPostIdsList().isEmpty()
                                  ? null
                                  : request.getProjectPostIdsList())
                          .setAssignmentIds(
                              request.getAssignmentIdsList().isEmpty()
                                  ? null
                                  : request.getAssignmentIdsList())
                          .setClassXIds(
                              request.getClassXIdsList().isEmpty()
                                  ? null
                                  : request.getClassXIdsList())
                          .setSchoolIds(
                              request.getSchoolIdsList().isEmpty()
                                  ? null
                                  : request.getSchoolIdsList())
                          .setUserXIds(
                              request.getUserXIdsList().isEmpty()
                                  ? null
                                  : request.getUserXIdsList())
                          .setBeingEdited(
                              !request.hasBeingEdited() ? null : request.getBeingEdited()))
                  .forEach(
                      fullProjectPost ->
                          ProtoDaoUtils.toProjectPostProto(
                              fullProjectPost, true, response::addProjectPostsBuilder));

              return response.build();
            })
        .finish();
  }

  @Transactional
  @PostMapping(value = "/api/protos/PostService/UpsertProjectPost")
  @ResponseBody
  public UpsertProjectPostResponse upsertProjectPost(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpsertProjectPostRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = UpsertProjectPostResponse.newBuilder();
    return httpExecutors
        .start(optionalRequest.orElse(UpsertProjectPostRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              UserX postUserX =
                  request.getProjectPost().getUserX().hasId()
                      ? ProtoDaoUtils.toUserXDao(request.getProjectPost().getUserX())
                      : checkNotNull(userX.getUserXOrNull());

              if (!userX.isAdminX() && request.getProjectPost().getUserX().hasId()) {
                Optional<ProjectPost> existingPost =
                    db.getProjectPostRepository().findById(request.getProjectPost().getId());
                if (existingPost.isPresent()) {
                  if (!Objects.equals(existingPost.get().getUserX().getId(), postUserX.getId())) {
                    return userX.returnForbidden(response.build());
                  }
                }
              }

              var projectPost = ProtoDaoUtils.toProjectPostDao(request.getProjectPost());
              projectPost.setUserX(postUserX);
              projectPost.setPostTime(Instant.now());

              db.getProjectPostRepository().upsert(db, postUserX, projectPost);
              response.setProjectPostId(projectPost.getId());

              return response.build();
            })
        .finish();
  }

  @Transactional
  @PostMapping(value = "/api/protos/PostService/UpsertProjectPostComment")
  @ResponseBody
  public UpsertProjectPostCommentResponse upsertProjectPostComment(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpsertProjectPostCommentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = UpsertProjectPostCommentResponse.newBuilder();
    return httpExecutors
        .start(optionalRequest.orElse(UpsertProjectPostCommentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              UserX postUserX =
                  request.getProjectPostComment().getUserX().hasId()
                      ? ProtoDaoUtils.toUserXDao(request.getProjectPostComment().getUserX())
                      : checkNotNull(userX.getUserXOrNull());

              Optional<ProjectPostComment> existingComment =
                  db.getProjectPostCommentRepository()
                      .findById(request.getProjectPostComment().getId());

              if (existingComment.isPresent()
                  && !userX.isAdminX()
                  && !userX.isTeacher()
                  && !Objects.equals(
                      existingComment.get().getUserX().getId(), userX.getUserXIdOrNull())) {
                return userX.returnForbidden(response.build());
              }

              ProjectPostComment comment =
                  ProtoDaoUtils.toProjectPostCommentDao(request.getProjectPostComment())
                      .setCreationTime(Instant.now())
                      .setUserX(postUserX)
                      .setPostTime(Instant.now());
              existingComment.ifPresent(
                  existing ->
                      comment
                          .setUserX(existing.getUserX())
                          .setCreationTime(existing.getCreationTime())
                          .setPostTime(existing.getPostTime()));

              DaoUtils.removeTransientValues(comment, db.getProjectPostCommentRepository()::save);

              ProtoDaoUtils.toProjectPostCommentProto(
                  comment, response::getProjectPostCommentBuilder);

              return response.build();
            })
        .finish();
  }

  @Transactional
  @PostMapping(value = "/api/protos/PostService/DeleteProjectPostComment")
  @ResponseBody
  public DeleteProjectPostCommentResponse deleteProjectPostComment(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<DeleteProjectPostCommentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(DeleteProjectPostCommentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (userX.isNotAuthorized()) {
                return userX.returnForbidden(DeleteProjectPostCommentResponse.getDefaultInstance());
              }

              FullProjectPostComment fullProjectPostComment =
                  db.getProjectPostCommentRepository()
                      .getFullProjectPostCommentById(request.getProjectPostCommentId())
                      .orElse(null);
              if (fullProjectPostComment == null) {
                if (userX.isAdminX()) {
                  return DeleteProjectPostCommentResponse.getDefaultInstance();
                } else {
                  return userX.returnForbidden(
                      DeleteProjectPostCommentResponse.getDefaultInstance());
                }
              }

              if (!userX.isAdminX()
                  && !Objects.equals(
                      fullProjectPostComment.getProjectPostComment().getUserX().getId(),
                      userX.getUserXIdOrNull())) {
                return userX.returnForbidden(DeleteProjectPostCommentResponse.getDefaultInstance());
              }

              fullProjectPostComment.getProjectPostComment().setDeleted(Instant.now());
              db.getProjectPostCommentRepository().upsert(fullProjectPostComment);

              return DeleteProjectPostCommentResponse.getDefaultInstance();
            })
        .finish();
  }

  @Transactional
  @PostMapping(value = "/api/protos/PostService/UpsertProjectPostRating")
  @ResponseBody
  public UpsertProjectPostRatingResponse upsertProjectPostRating(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpsertProjectPostRatingRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(UpsertProjectPostRatingRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (userX.isNotAuthorized()) {
                return userX.returnForbidden(UpsertProjectPostRatingResponse.getDefaultInstance());
              }

              if (!userX.isAdminX()) {
                if (!Objects.equals(
                    request.getProjectPostRating().getUserX().getId(), userX.getUserXIdOrNull())) {
                  return userX.returnForbidden(
                      UpsertProjectPostRatingResponse.getDefaultInstance());
                }
                if (request.getProjectPostRating().hasId()) {
                  ProjectPostRating oldRating =
                      db.getProjectPostRatingRepository()
                          .findById(request.getProjectPostRating().getId())
                          .orElse(null);
                  if (oldRating != null) {
                    if (!Objects.equals(oldRating.getUserX().getId(), userX.getUserXIdOrNull())) {
                      return userX.returnForbidden(
                          UpsertProjectPostRatingResponse.getDefaultInstance());
                    }
                  }
                }
              }

              ProjectPostRating savedRating =
                  DaoUtils.removeTransientValues(
                      ProtoDaoUtils.toProjectPostRatingDao(request.getProjectPostRating()),
                      db.getProjectPostRatingRepository()::save);

              return UpsertProjectPostRatingResponse.newBuilder()
                  .setId(savedRating.getId())
                  .build();
            })
        .finish();
  }
}
