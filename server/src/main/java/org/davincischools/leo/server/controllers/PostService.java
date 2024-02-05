package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.listOrNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostCommentProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostRatingDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toUserXDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.valueOrNull;

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
import org.davincischools.leo.database.utils.repos.GetProjectsParams;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository.FullProjectPostComment;
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
import org.davincischools.leo.protos.task_service.ReplyToPostTask;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.davincischools.leo.server.utils.task_queue.workers.ReplyToPostsWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostService {

  @Autowired Database db;
  @Autowired ReplyToPostsWorker replyToPostsWorker;

  @PostMapping(value = "/api/protos/PostService/GetProjectPosts")
  @ResponseBody
  public GetProjectPostsResponse getProjectPosts(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetProjectPostsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(GetProjectPostsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              // TODO: Determine what sort of security to have on project posts.

              var response = GetProjectPostsResponse.newBuilder();

              Page<ProjectPost> projectPosts =
                  db.getProjectPostRepository()
                      .getProjectPosts(
                          new GetProjectPostsParams()
                              .setIncludeTags(
                                  valueOrNull(
                                      request, GetProjectPostsRequest.INCLUDE_TAGS_FIELD_NUMBER))
                              .setIncludeComments(
                                  valueOrNull(
                                      request,
                                      GetProjectPostsRequest.INCLUDE_COMMENTS_FIELD_NUMBER))
                              .setIncludeProjects(
                                  request.getIncludeProjects()
                                      ? new GetProjectsParams()
                                          .setIncludeFulfillments(request.getIncludeRatings())
                                      : null)
                              .setIncludeRatings(
                                  valueOrNull(
                                      request,
                                      GetProjectPostsRequest.INCLUDE_RATINGS_FIELD_NUMBER,
                                      userX.isAdminX() || userX.isTeacher()))
                              .setIncludeAssignments(
                                  valueOrNull(
                                      request,
                                      GetProjectPostsRequest.INCLUDE_ASSIGNMENTS_FIELD_NUMBER))
                              .setProjectIds(
                                  listOrNull(
                                      request, GetProjectPostsRequest.PROJECT_IDS_FIELD_NUMBER))
                              .setProjectPostIds(
                                  listOrNull(
                                      request,
                                      GetProjectPostsRequest.PROJECT_POST_IDS_FIELD_NUMBER))
                              .setAssignmentIds(
                                  listOrNull(
                                      request, GetProjectPostsRequest.ASSIGNMENT_IDS_FIELD_NUMBER))
                              .setClassXIds(
                                  listOrNull(
                                      request, GetProjectPostsRequest.CLASS_X_IDS_FIELD_NUMBER))
                              .setSchoolIds(
                                  listOrNull(
                                      request, GetProjectPostsRequest.SCHOOL_IDS_FIELD_NUMBER))
                              .setUserXIds(
                                  listOrNull(
                                      request, GetProjectPostsRequest.USER_X_IDS_FIELD_NUMBER))
                              .setBeingEdited(
                                  valueOrNull(
                                      request, GetProjectPostsRequest.BEING_EDITED_FIELD_NUMBER))
                              .setPage(
                                  valueOrNull(request, GetProjectPostsRequest.PAGE_FIELD_NUMBER))
                              .setPageSize(
                                  valueOrNull(
                                      request, GetProjectPostsRequest.PAGE_SIZE_FIELD_NUMBER)));
              projectPosts.forEach(
                  fullProjectPost ->
                      toProjectPostProto(fullProjectPost, true, response::addProjectPostsBuilder));

              if (request.hasPage()) {
                response
                    .setPage(request.getPage())
                    .setPageSize(request.getPageSize())
                    .setTotalProjectPosts(projectPosts.getTotalElements());
              }

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
                      ? toUserXDao(request.getProjectPost().getUserX()).orElseThrow()
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

              var projectPost = toProjectPostDao(request.getProjectPost()).orElseThrow();
              projectPost.setUserX(postUserX);
              projectPost.setPostTime(Instant.now());

              db.getProjectPostRepository().upsert(db, postUserX, projectPost);
              if (!projectPost.getBeingEdited()) {
                replyToPostsWorker.submitTask(
                    ReplyToPostTask.newBuilder()
                        .setProjectId(projectPost.getProject().getId())
                        .build());
              }
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
                      ? toUserXDao(request.getProjectPostComment().getUserX()).orElseThrow()
                      : checkNotNull(userX.getUserXOrNull());

              ProjectPostComment comment =
                  db.getProjectPostCommentRepository()
                      .findById(request.getProjectPostComment().getId())
                      .orElseGet(
                          () ->
                              new ProjectPostComment()
                                  .setCreationTime(Instant.now())
                                  .setUserX(postUserX)
                                  .setProjectPost(
                                      new ProjectPost()
                                          .setId(
                                              request
                                                  .getProjectPostComment()
                                                  .getProjectPost()
                                                  .getId()))
                                  .setPostTime(Instant.now()));

              if (!userX.isAdminX()
                  && !userX.isTeacher()
                  && !Objects.equals(comment.getUserX().getId(), userX.getUserXIdOrNull())) {
                return userX.returnForbidden(response.build());
              }

              comment.setLongDescrHtml(request.getProjectPostComment().getLongDescrHtml());

              DaoUtils.removeTransientValues(comment, db.getProjectPostCommentRepository()::save);

              toProjectPostCommentProto(comment, response::getProjectPostCommentBuilder);

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
                return DeleteProjectPostCommentResponse.getDefaultInstance();
              }

              if (!userX.isAdminX()
                  && !userX.isTeacher()
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
                      toProjectPostRatingDao(request.getProjectPostRating()).orElseThrow(),
                      db.getProjectPostRatingRepository()::save);

              return UpsertProjectPostRatingResponse.newBuilder()
                  .setId(savedRating.getId())
                  .build();
            })
        .finish();
  }
}
