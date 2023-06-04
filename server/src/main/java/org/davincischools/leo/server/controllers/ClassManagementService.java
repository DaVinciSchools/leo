package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsRequest;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user.HttpUser;
import org.davincischools.leo.server.utils.http_user.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassManagementService {

  @Autowired Database db;
  @Autowired OpenAiUtils openAiUtils;

  @PostMapping(value = "/api/protos/ClassManagementService/GetStudentAssignments")
  @ResponseBody
  public GetStudentAssignmentsResponse getStudentAssignments(
      @Student HttpUser user,
      @RequestBody Optional<GetStudentAssignmentsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetStudentAssignmentsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetStudentAssignmentsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetStudentAssignmentsResponse.newBuilder();

              for (Assignment assignment :
                  db.getAssignmentRepository()
                      .findAllByStudentId(user.get().getStudent().getId())) {
                response.addAssignments(
                    DataAccess.convertAssignmentToProto(assignment.getClassX(), assignment));
              }

              return response.build();
            })
        .finish();
  }
}
