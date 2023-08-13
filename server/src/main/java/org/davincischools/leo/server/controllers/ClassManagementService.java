package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.class_management_service.GetClassesRequest;
import org.davincischools.leo.protos.class_management_service.GetClassesResponse;
import org.davincischools.leo.server.utils.ProtoDaoConverter;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user.Authenticated;
import org.davincischools.leo.server.utils.http_user.HttpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/ClassManagementService/GetClasses")
  @ResponseBody
  public GetClassesResponse getClasses(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetClassesRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = GetClassesResponse.newBuilder();

    return httpExecutors
        .start(optionalRequest.orElse(GetClassesRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              boolean accessDenied =
                  switch (request.getIdCase()) {
                    case STUDENT_ID -> !user.isStudent();
                    case TEACHER_ID -> !user.isTeacher();
                    case ID_NOT_SET -> true;
                  };
              if (accessDenied) {
                return user.returnForbidden(response.build());
              }

              response.addAllClasses(
                  db
                      .getClassXRepository()
                      .findFullClassXsByUserId(user.teacherId(), user.studentId())
                      .stream()
                      .map(ProtoDaoConverter::toClassXProto)
                      .toList());

              return response.build();
            })
        .finish();
  }
}
