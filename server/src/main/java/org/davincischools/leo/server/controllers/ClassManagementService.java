package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsRequest;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.HttpUserProvider.Student;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.davincischools.leo.server.utils.OpenAiUtils;
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
      @Student UserX userX, @RequestBody Optional<GetStudentAssignmentsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(GetStudentAssignmentsRequest.getDefaultInstance()),
            (request, log) -> {
              var response = GetStudentAssignmentsResponse.newBuilder();

              for (Assignment assignment :
                  db.getAssignmentRepository().findAllByStudentId(userX.getStudent().getId())) {
                response.addAssignments(
                    DataAccess.convertAssignmentToProto(assignment.getClassX(), assignment));
              }

              return response.build();
            })
        .finish();
  }
}
