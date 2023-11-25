package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.server.utils.ProtoDaoUtils.listOrNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toClassXDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toClassXProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.valueOrNull;

import com.google.common.collect.Iterables;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.exceptions.UnauthorizedUserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetAssignmentsParams;
import org.davincischools.leo.database.utils.repos.GetClassXsParams;
import org.davincischools.leo.protos.class_x_management_service.GetClassXsRequest;
import org.davincischools.leo.protos.class_x_management_service.GetClassXsResponse;
import org.davincischools.leo.protos.class_x_management_service.UpsertClassXRequest;
import org.davincischools.leo.protos.class_x_management_service.UpsertClassXResponse;
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
                if (request.getTeacherIdsCount() > 0
                    && !Objects.equals(
                        userX.getTeacherIdOrNull(),
                        Iterables.getOnlyElement(request.getTeacherIdsList()))) {
                  return userX.returnForbidden(response.build());
                }
                if (request.getStudentIdsCount() > 0
                    && !Objects.equals(
                        userX.getStudentIdOrNull(),
                        Iterables.getOnlyElement(request.getStudentIdsList()))) {
                  return userX.returnForbidden(response.build());
                }
              }

              db.getClassXRepository()
                  .getClassXs(
                      new GetClassXsParams()
                          .setSchoolIds(
                              listOrNull(request, GetClassXsRequest.SCHOOL_IDS_FIELD_NUMBER))
                          .setClassXIds(
                              listOrNull(request, GetClassXsRequest.CLASS_X_IDS_FIELD_NUMBER))
                          .setTeacherIds(
                              listOrNull(request, GetClassXsRequest.TEACHER_IDS_FIELD_NUMBER))
                          .setStudentIds(
                              listOrNull(request, GetClassXsRequest.STUDENT_IDS_FIELD_NUMBER))
                          .setIncludeSchool(
                              valueOrNull(request, GetClassXsRequest.INCLUDE_SCHOOL_FIELD_NUMBER))
                          .setIncludeAssignments(
                              request.getIncludeAssignments()
                                  ? new GetAssignmentsParams()
                                      .setIncludeKnowledgeAndSkills(
                                          valueOrNull(
                                              request,
                                              GetClassXsRequest
                                                  .INCLUDE_KNOWLEDGE_AND_SKILLS_FIELD_NUMBER))
                                  : null)
                          .setIncludeKnowledgeAndSkills(
                              valueOrNull(
                                  request,
                                  GetClassXsRequest.INCLUDE_KNOWLEDGE_AND_SKILLS_FIELD_NUMBER)))
                  .forEach(classX -> toClassXProto(classX, true, response::addClassXsBuilder));

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

              ClassX classX = toClassXDao(request.getClassX()).orElseThrow();
              db.getClassXRepository()
                  .guardedUpsert(db, classX, userX.isAdminX() ? null : userX.getTeacherIdOrNull());
              toClassXProto(classX, true, response::getClassXBuilder);

              if (userX.getTeacherOrNull() != null) {
                db.getTeacherClassXRepository().upsert(userX.getTeacherOrNull(), classX);
              }

              return response.build();
            })
        .finish();
  }
}
