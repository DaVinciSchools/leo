package org.davincischools.leo.server.controllers;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ProjectPostRepository.GetProjectPostsParams;
import org.davincischools.leo.protos.post_service.GetProjectPostsRequest;
import org.davincischools.leo.protos.post_service.GetProjectPostsResponse;
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

              db.getProjectPostRepository()
                  .getProjectPosts(
                      entityManager,
                      new GetProjectPostsParams()
                          .setProjectIds(
                              request.getProjectIdsList().isEmpty()
                                  ? null
                                  : request.getProjectIdsList())
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
                      fullProjectPost -> {
                        ProtoDaoUtils.toProjectPostProto(
                            fullProjectPost, response.addProjectPostsBuilder());
                      });

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
                  request.getProjectPost().getUserX().hasUserXId()
                      ? ProtoDaoUtils.toUserXDao(request.getProjectPost().getUserX())
                      : userX.getUserXOrNull();

              if (!userX.isAdminX() && request.getProjectPost().getUserX().hasUserXId()) {
                Optional<ProjectPost> existingPost =
                    db.getProjectPostRepository().findById(request.getProjectPost().getId());
                if (existingPost.isPresent()) {
                  if (!Objects.equals(existingPost.get().getUserX().getId(), postUserX.getId())) {
                    return userX.returnForbidden(response.build());
                  }
                }
              }

              var fullProjectPost = ProtoDaoUtils.toFullProjectPostRecord(request.getProjectPost());
              fullProjectPost.getProjectPost().setUserX(postUserX);
              fullProjectPost.getProjectPost().setPostTime(Instant.now());

              fullProjectPost
                  .getTags()
                  .forEach(
                      tag -> {
                        if (!userX.isAdminX()
                            || tag.getUserX() == null
                            || tag.getUserX().getId() == null) {
                          tag.setUserX(postUserX);
                        }
                      });

              db.getProjectPostRepository().upsert(db, userX.getUserXOrNull(), fullProjectPost);
              response.setProjectPostId(fullProjectPost.getProjectPost().getId());

              return response.build();
            })
        .finish();
  }
}
