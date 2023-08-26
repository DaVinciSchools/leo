package org.davincischools.leo.server.controllers;

import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.ClassXRepository.FullClassX;
import org.davincischools.leo.protos.class_x_management_service.GetClassXsRequest;
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
              if (!userX.isAdminX()) {
                if (request.hasTeacherId()
                    && !Objects.equals(userX.getTeacherIdOrNull(), request.getTeacherId())) {
                  return userX.returnForbidden(response.build());
                }
                if (request.hasStudentId()
                    && !Objects.equals(userX.getStudentIdOrNull(), request.getStudentId())) {
                  return userX.returnForbidden(response.build());
                }
              }

              db.getClassXRepository()
                  .findFullClassXs(
                      request.hasTeacherId() ? new Teacher().setId(request.getTeacherId()) : null,
                      request.hasStudentId() ? new Student().setId(request.getStudentId()) : null,
                      request.getSchoolIdsList().stream().map(e -> new School().setId(e)).toList(),
                      request.getIncludeAllAvailableClassXs(),
                      request.getIncludeKnowledgeAndSkills())
                  .forEach(
                      fullClassX -> {
                        ProtoDaoUtils.toFullClassXProto(fullClassX, response.addClassXsBuilder());
                      });

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
                  .guardedUpsert(
                      db, fullClassX, userX.isAdminX() ? null : userX.getTeacherIdOrNull());
              ProtoDaoUtils.toFullClassXProto(fullClassX, response.getClassXBuilder());

              if (userX.getTeacherOrNull() != null) {
                db.getTeacherClassXRepository()
                    .upsert(userX.getTeacherOrNull(), fullClassX.classX());
              }

              return response.build();
            })
        .finish();
  }
}
