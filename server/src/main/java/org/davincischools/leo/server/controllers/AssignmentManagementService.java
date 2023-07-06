package org.davincischools.leo.server.controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.TeacherClassXId;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.AssignmentRepository.ClassXAssignment;
import org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest;
import org.davincischools.leo.protos.assignment_management.CreateAssignmentResponse;
import org.davincischools.leo.protos.assignment_management.DeleteAssignmentRequest;
import org.davincischools.leo.protos.assignment_management.DeleteAssignmentResponse;
import org.davincischools.leo.protos.assignment_management.GetAssignmentsRequest;
import org.davincischools.leo.protos.assignment_management.GetAssignmentsResponse;
import org.davincischools.leo.protos.assignment_management.SaveAssignmentRequest;
import org.davincischools.leo.protos.assignment_management.SaveAssignmentResponse;
import org.davincischools.leo.server.utils.DataAccess;
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
public class AssignmentManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/AssignmentManagementService/GetAssignments")
  @ResponseBody
  public GetAssignmentsResponse getAssignments(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetAssignmentsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(GetAssignmentsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetAssignmentsResponse.newBuilder();
              if (user.isNotAuthorized()) {
                return user.returnForbidden(response.build());
              }

              List<ClassXAssignment> assignments;
              switch (request.getUserIdCase()) {
                case TEACHER_ID -> {
                  if (!user.isAdmin()
                      && (!user.isTeacher()
                          || !Objects.equals(
                              user.get().getTeacher().getId(), request.getTeacherId()))) {
                    return user.returnForbidden(response.build());
                  }
                  assignments =
                      Lists.newArrayList(
                          db.getAssignmentRepository().findAllByTeacherId(request.getTeacherId()));
                }
                case STUDENT_ID -> {
                  if (!user.isAdmin()
                      && (!user.isStudent()
                          || !Objects.equals(
                              user.get().getStudent().getId(), request.getStudentId()))) {
                    return user.returnForbidden(response.build());
                  }
                  assignments =
                      Lists.newArrayList(
                          db.getAssignmentRepository().findAllByStudentId(request.getStudentId()));
                }
                default -> throw new IllegalArgumentException("Unsupported user type.");
              }

              var assignmentsByClassX =
                  Multimaps.filterValues(
                      Multimaps.transformValues(
                          Multimaps.index(assignments, ClassXAssignment::getClassX),
                          ClassXAssignment::getAssignment),
                      Objects::nonNull);
              assignmentsByClassX
                  .asMap()
                  .forEach(
                      (key, value) -> {
                        var classProto = DataAccess.convertClassToProto(key);
                        response.addClassXs(classProto);
                        value.forEach(
                            assignment ->
                                response.addAssignments(
                                    DataAccess.convertAssignmentToProto(key, assignment)));
                      });

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/AssignmentManagementService/CreateAssignment")
  @ResponseBody
  public CreateAssignmentResponse createAssignment(
      @Authenticated HttpUser user,
      @RequestBody Optional<CreateAssignmentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(CreateAssignmentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = CreateAssignmentResponse.newBuilder();
              if (user.isNotAuthorized()) {
                return user.returnForbidden(response.build());
              }

              if (!user.isAdmin()
                  && !(user.isTeacher()
                      && db.getTeacherClassXRepository()
                          .findById(
                              new TeacherClassXId()
                                  .setTeacherId(user.get().getTeacher().getId())
                                  .setClassXId(request.getClassXId()))
                          .isPresent())) {
                return user.returnForbidden(response.build());
              }

              ClassX classX =
                  db.getClassXRepository().findById(request.getClassXId()).orElseThrow();

              response.setAssignment(
                  DataAccess.convertAssignmentToProto(
                      classX,
                      db.getAssignmentRepository()
                          .save(
                              new Assignment()
                                  .setCreationTime(Instant.now())
                                  .setName("New Assignment")
                                  .setClassX(classX))));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/AssignmentManagementService/SaveAssignment")
  @ResponseBody
  public SaveAssignmentResponse saveAssignment(
      @Authenticated HttpUser user,
      @RequestBody Optional<SaveAssignmentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(SaveAssignmentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = SaveAssignmentResponse.newBuilder();
              if (user.isNotAuthorized()) {
                return user.returnForbidden(response.build());
              }

              Assignment assignment =
                  db.getAssignmentRepository()
                      .findById(request.getAssignment().getId())
                      .orElseThrow();

              if (!user.isAdmin()
                  && !(user.isTeacher()
                      && db.getTeacherClassXRepository()
                          .findById(
                              new TeacherClassXId()
                                  .setTeacherId(user.get().getTeacher().getId())
                                  .setClassXId(assignment.getClassX().getId()))
                          .isPresent())) {
                return user.returnForbidden(response.build());
              }

              db.getAssignmentRepository()
                  .save(
                      assignment
                          .setName(request.getAssignment().getName())
                          .setShortDescr(request.getAssignment().getShortDescr())
                          .setLongDescr(request.getAssignment().getLongDescr()));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/AssignmentManagementService/DeleteAssignment")
  @ResponseBody
  public DeleteAssignmentResponse deleteAssignment(
      @Authenticated HttpUser user,
      @RequestBody Optional<DeleteAssignmentRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(DeleteAssignmentRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = DeleteAssignmentResponse.newBuilder();
              if (user.isNotAuthorized()) {
                return user.returnForbidden(response.build());
              }

              Assignment assignment =
                  db.getAssignmentRepository()
                      .findById(request.getAssignmentId())
                      .orElseThrow();

              if (!user.isAdmin()
                  && !(user.isTeacher()
                  && db.getTeacherClassXRepository()
                  .findById(
                      new TeacherClassXId()
                          .setTeacherId(user.get().getTeacher().getId())
                          .setClassXId(assignment.getClassX().getId()))
                  .isPresent())) {
                return user.returnForbidden(response.build());
              }

              db.getAssignmentRepository().delete(assignment);

              return response.build();
            })
        .finish();
  }
}
