package org.davincischools.leo.server.controllers;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.TeacherClassXId;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.AssignmentRepository.FullClassXAssignment;
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

              List<FullClassXAssignment> classXAssignments;
              switch (request.getUserXIdCase()) {
                case TEACHER_ID -> {
                  if (!userX.isAdminX()
                      && (!userX.isTeacher()
                          || !Objects.equals(
                              userX.get().orElseThrow().getTeacher().getId(),
                              request.getTeacherId()))) {
                    return userX.returnForbidden(response.build());
                  }
                  classXAssignments =
                      db.getAssignmentRepository().findAllByTeacherId(request.getTeacherId());
                }
                case STUDENT_ID -> {
                  if (!userX.isAdminX()
                      && (!userX.isStudent()
                          || !Objects.equals(
                              userX.get().orElseThrow().getStudent().getId(),
                              request.getStudentId()))) {
                    return userX.returnForbidden(response.build());
                  }
                  classXAssignments =
                      db.getAssignmentRepository().findAllByStudentId(request.getStudentId());
                }
                default -> throw new IllegalArgumentException("Unsupported user type.");
              }

              classXAssignments.forEach(
                  classXAssignment -> {
                    classXAssignment
                        .assignments()
                        .forEach(
                            assignment -> {
                              assignment.assignment().setClassX(classXAssignment.classX());
                              ProtoDaoUtils.toAssignmentProto(
                                  assignment.assignment(), response.addAssignmentsBuilder());
                            });
                    ProtoDaoUtils.toClassXProto(
                        classXAssignment.classX(), response.addClassXsBuilder());
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

              response.setAssignment(
                  ProtoDaoUtils.toAssignmentProto(
                      db.getAssignmentRepository()
                          .save(
                              new Assignment()
                                  .setCreationTime(Instant.now())
                                  .setName("New Assignment")
                                  .setClassX(classX)),
                      null));

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
                  .save(
                      assignment
                          .setName(request.getAssignment().getName())
                          .setShortDescr(request.getAssignment().getShortDescr())
                          .setLongDescrHtml(request.getAssignment().getLongDescrHtml()));

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
