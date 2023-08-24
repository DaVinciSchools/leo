package org.davincischools.leo.server.controllers;

import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ClassXRepository.FullClassX;
import org.davincischools.leo.protos.class_x_management_service.GetClassXsRequest;
import org.davincischools.leo.protos.class_x_management_service.GetClassXsRequest.IdCase;
import org.davincischools.leo.protos.class_x_management_service.GetClassXsResponse;
import org.davincischools.leo.protos.class_x_management_service.UpsertClassXRequest;
import org.davincischools.leo.protos.class_x_management_service.UpsertClassXResponse;
import org.davincischools.leo.server.utils.ProtoDaoUtils;
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
public class ClassXManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/ClassXManagementService/GetClassXs")
  @ResponseBody
  public GetClassXsResponse getClassXs(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetClassXsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = GetClassXsResponse.newBuilder();

    return httpExecutors
        .start(optionalRequest.orElse(GetClassXsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (!switch (request.getIdCase()) {
                case TEACHER_ID -> userX.isAdminX()
                    || Objects.equals(userX.teacherId(), request.getTeacherId());
                case STUDENT_ID -> userX.isAdminX()
                    || Objects.equals(userX.studentId(), request.getStudentId());
                case ID_NOT_SET -> false;
              }) {
                return userX.returnForbidden(response.build());
              }

              db.getClassXRepository()
                  .findFullClassXsByUserXId(
                      request.getIdCase() == IdCase.TEACHER_ID ? request.getTeacherId() : null,
                      request.getIdCase() == IdCase.STUDENT_ID ? request.getStudentId() : null)
                  .forEach(e -> ProtoDaoUtils.toFullClassXProto(e, response.addClassXsBuilder()));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ClassXManagementService/UpsertClassX")
  @ResponseBody
  public UpsertClassXResponse upsertClassX(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpsertClassXRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = UpsertClassXResponse.newBuilder();

    return httpExecutors
        .start(optionalRequest.orElse(UpsertClassXRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (!userX.isAdminX() && !userX.isTeacher()) {
                throw new UnauthorizedUserX();
              }

              FullClassX fullClassX = ProtoDaoUtils.toFullClassXRecord(request.getClassX());
              db.getClassXRepository()
                  .guardedUpsert(db, fullClassX, userX.isAdminX() ? null : userX.teacherId());
              ProtoDaoUtils.toFullClassXProto(fullClassX, response.getClassXBuilder());

              if (userX.teacherId() != null) {
                db.getTeacherClassXRepository()
                    .upsert(new Teacher().setId(userX.teacherId()), fullClassX.classX());
              }

              return response.build();
            })
        .finish();
  }
}
