package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.database.utils.DaoUtils.createJoinTableRows;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.listOrNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toAssignmentProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toClassXProto;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.TeacherClassXId;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.AssignmentKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.GetAssignmentsParams;
import org.davincischools.leo.database.utils.repos.GetClassXsParams;
import org.davincischools.leo.database.utils.repos.GetProjectDefinitionsParams;
import org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest;
import org.davincischools.leo.protos.assignment_management.CreateAssignmentResponse;
import org.davincischools.leo.protos.assignment_management.DeleteAssignmentRequest;
import org.davincischools.leo.protos.assignment_management.DeleteAssignmentResponse;
import org.davincischools.leo.protos.assignment_management.GetAssignmentsRequest;
import org.davincischools.leo.protos.assignment_management.GetAssignmentsResponse;
import org.davincischools.leo.protos.assignment_management.SaveAssignmentRequest;
import org.davincischools.leo.protos.assignment_management.SaveAssignmentResponse;
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
public class AssignmentManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/AssignmentManagementService/GetAssignments")
  @ResponseBody
  public GetAssignmentsResponse getAssignments(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetAssignmentsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(GetAssignmentsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetAssignmentsResponse.newBuilder();

              if (userX.isNotAuthorized()) {
                return userX.returnForbidden(response.build());
              }

              if (!userX.isAdminX()) {
                if (userX.isTeacher()) {
                  if (request.hasTeacherId()
                      && !Objects.equals(userX.getTeacherIdOrNull(), request.getTeacherId())) {
                    return userX.returnForbidden(response.build());
                  }
                } else if (userX.isStudent()) {
                  if (request.hasStudentId()
                      && !Objects.equals(userX.getStudentIdOrNull(), request.getStudentId())) {
                    return userX.returnForbidden(response.build());
                  }
                  if (request.hasTeacherId()) {
                    return userX.returnForbidden(response.build());
                  }
                }
              }

              db.getClassXRepository()
                  .getClassXs(
                      new GetClassXsParams()
                          .setIncludeAssignments(
                              new GetAssignmentsParams()
                                  .setAssignmentIds(
                                      listOrNull(
                                          request,
                                          GetAssignmentsRequest.ASSIGNMENT_IDS_FIELD_NUMBER))
                                  .setIncludeKnowledgeAndSkills(true)
                                  .setIncludeProjectDefinitions(
                                      request.getIncludeProjectDefinitions()
                                          ? new GetProjectDefinitionsParams()
                                          : null))
                          .setSchoolIds(
                              listOrNull(request, GetAssignmentsRequest.SCHOOL_IDS_FIELD_NUMBER))
                          .setClassXIds(
                              listOrNull(request, GetAssignmentsRequest.CLASS_X_IDS_FIELD_NUMBER))
                          .setTeacherIds(
                              listOrNull(request, GetAssignmentsRequest.TEACHER_ID_FIELD_NUMBER))
                          .setStudentIds(
                              listOrNull(request, GetAssignmentsRequest.STUDENT_ID_FIELD_NUMBER)))
                  .forEach(
                      classX -> {
                        toClassXProto(classX, true, response::addClassXsBuilder);
                        classX
                            .getAssignments()
                            .forEach(
                                assignment ->
                                    toAssignmentProto(
                                        assignment, true, response::addAssignmentsBuilder));
                      });

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/AssignmentManagementService/CreateAssignment")
  @ResponseBody
  public CreateAssignmentResponse createAssignment(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<CreateAssignmentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(CreateAssignmentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = CreateAssignmentResponse.newBuilder();
              if (userX.isNotAuthorized()) {
                return userX.returnForbidden(response.build());
              }

              if (!userX.isAdminX()
                  && !(userX.isTeacher()
                      && db.getTeacherClassXRepository()
                          .findById(
                              new TeacherClassXId()
                                  .setTeacherId(userX.get().orElseThrow().getTeacher().getId())
                                  .setClassXId(request.getClassXId()))
                          .isPresent())) {
                return userX.returnForbidden(response.build());
              }

              ClassX classX =
                  db.getClassXRepository().findById(request.getClassXId()).orElseThrow();

              toAssignmentProto(
                  db.getAssignmentRepository()
                      .save(
                          new Assignment()
                              .setCreationTime(Instant.now())
                              .setName("New Assignment")
                              .setClassX(classX)),
                  true,
                  response::getAssignmentBuilder);

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/AssignmentManagementService/SaveAssignment")
  @ResponseBody
  public SaveAssignmentResponse saveAssignment(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<SaveAssignmentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(SaveAssignmentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = SaveAssignmentResponse.newBuilder();
              if (userX.isNotAuthorized()) {
                return userX.returnForbidden(response.build());
              }

              Assignment assignment =
                  db.getAssignmentRepository()
                      .findById(request.getAssignment().getId())
                      .orElseThrow();

              if (!userX.isAdminX()
                  && !(userX.isTeacher()
                      && db.getTeacherClassXRepository()
                          .findById(
                              new TeacherClassXId()
                                  .setTeacherId(userX.get().orElseThrow().getTeacher().getId())
                                  .setClassXId(assignment.getClassX().getId()))
                          .isPresent())) {
                return userX.returnForbidden(response.build());
              }

              db.getAssignmentRepository()
                  .upsert(
                      db,
                      assignment
                          .setName(request.getAssignment().getName())
                          .setShortDescr(request.getAssignment().getShortDescr())
                          .setLongDescrHtml(request.getAssignment().getLongDescrHtml())
                          .setAssignmentKnowledgeAndSkills(
                              createJoinTableRows(
                                  assignment,
                                  request.getAssignment().getKnowledgeAndSkillsList().stream()
                                      .map(ProtoDaoUtils::toKnowledgeAndSkillDao)
                                      .filter(Optional::isPresent)
                                      .map(Optional::get)
                                      .toList(),
                                  AssignmentKnowledgeAndSkillRepository::create)));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/AssignmentManagementService/DeleteAssignment")
  @ResponseBody
  public DeleteAssignmentResponse deleteAssignment(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<DeleteAssignmentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(DeleteAssignmentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = DeleteAssignmentResponse.newBuilder();
              if (userX.isNotAuthorized()) {
                return userX.returnForbidden(response.build());
              }

              Assignment assignment =
                  db.getAssignmentRepository().findById(request.getAssignmentId()).orElseThrow();

              if (!userX.isAdminX()
                  && !(userX.isTeacher()
                      && db.getTeacherClassXRepository()
                          .findById(
                              new TeacherClassXId()
                                  .setTeacherId(userX.get().orElseThrow().getTeacher().getId())
                                  .setClassXId(assignment.getClassX().getId()))
                          .isPresent())) {
                return userX.returnForbidden(response.build());
              }

              db.getAssignmentRepository().delete(assignment);

              return response.build();
            })
        .finish();
  }
}
