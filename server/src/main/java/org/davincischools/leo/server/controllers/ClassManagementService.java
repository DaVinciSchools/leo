package org.davincischools.leo.server.controllers;

import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.class_management_service.GetClassesRequest;
import org.davincischools.leo.protos.class_management_service.GetClassesRequest.IdCase;
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
              if (!switch (request.getIdCase()) {
                case TEACHER_ID -> user.isAdmin()
                    || Objects.equals(user.teacherId(), request.getTeacherId());
                case STUDENT_ID -> user.isAdmin()
                    || Objects.equals(user.studentId(), request.getStudentId());
                case ID_NOT_SET -> false;
              }) {
                return user.returnForbidden(response.build());
              }

              db.getClassXRepository()
                  .findFullClassXsByUserId(
                      request.getIdCase() == IdCase.TEACHER_ID ? request.getTeacherId() : null,
                      request.getIdCase() == IdCase.STUDENT_ID ? request.getStudentId() : null)
                  .forEach(
                      e -> ProtoDaoConverter.toFullClassXProto(e, response.addClassesBuilder()));

              return response.build();
            })
        .finish();
  }
}
