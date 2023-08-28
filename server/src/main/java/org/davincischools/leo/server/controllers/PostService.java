package org.davincischools.leo.server.controllers;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ProjectPostRepository.FullProjectPost;
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

  @Autowired Database db;

  @PostMapping(value = "/api/protos/PostService/GetProjectPosts")
  @ResponseBody
  public GetProjectPostsResponse getProjectPosts(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetProjectPostsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = GetProjectPostsResponse.newBuilder();
    return httpExecutors
        .start(optionalRequest.orElse(GetProjectPostsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              // TODO: Determine what sort of security to have on project posts.

              List<FullProjectPost> fullProjectPosts =
                  db.getProjectPostRepository()
                      .findProjectPostsByProject(
                          new Project().setId(request.getProjectId()),
                          request.hasBeingEdited() ? request.getBeingEdited() : null);

              for (var fullProjectPost : fullProjectPosts) {
                ProtoDaoUtils.toProjectPostProto(
                    fullProjectPost, response.addProjectPostsBuilder());
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
              if (!userX.isAdminX()) {
                if (!Objects.equals(
                    request.getProjectPost().getUserX().getUserXId(), userX.getUserXIdOrNull())) {
                  return userX.returnForbidden(response.build());
                }
              }

              var fullProjectPost = ProtoDaoUtils.toFullProjectPostRecord(request.getProjectPost());
              fullProjectPost.projectPost().setPostTime(Instant.now());

              if (!userX.isAdminX() || fullProjectPost.projectPost().getUserX() == null) {
                fullProjectPost.projectPost().setUserX(userX.getUserXOrNull());
              }
              fullProjectPost
                  .tags()
                  .forEach(
                      tag -> {
                        UserX tagUserX = tag.getUserX();
                        if (!userX.isAdminX() || tagUserX == null) {
                          tagUserX = userX.getUserXOrNull();
                        }
                        tag.setUserX(tagUserX);
                      });

              db.getProjectPostRepository().upsert(db, userX.getUserXOrNull(), fullProjectPost);
              response.setProjectPostId(fullProjectPost.projectPost().getId());

              return response.build();
            })
        .finish();
  }
}
