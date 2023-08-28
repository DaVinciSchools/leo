package org.davincischools.leo.server.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.tag_service.GetAllPreviousTagsRequest;
import org.davincischools.leo.protos.tag_service.GetAllPreviousTagsResponse;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TagService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/TagService/GetAllPreviousTags")
  @ResponseBody
  public GetAllPreviousTagsResponse getAllPreviousTags(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetAllPreviousTagsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = GetAllPreviousTagsResponse.newBuilder();
    return httpExecutors
        .start(optionalRequest.orElse(GetAllPreviousTagsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              int userXId = request.getUserXId();
              if (!userX.isAdminX()) {
                if (!Objects.equals(request.getUserXId(), userX.getUserXIdOrNull())) {
                  return userX.returnForbidden(response.build());
                }
              }

              List<String> tags =
                  db.getTagRepository()
                      .findAllTagTextsByUserX(new UserX().setId(request.getUserXId()));
              tags.forEach(text -> response.addTagsBuilder().setUserXId(userXId).setText(text));

              return response.build();
            })
        .finish();
  }
}
