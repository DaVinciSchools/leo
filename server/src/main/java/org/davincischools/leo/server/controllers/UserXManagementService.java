package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserXUtils;
import org.davincischools.leo.protos.user_x_management.FullUserXDetails;
import org.davincischools.leo.protos.user_x_management.GetPagedUserXsDetailsRequest;
import org.davincischools.leo.protos.user_x_management.GetPagedUserXsDetailsResponse;
import org.davincischools.leo.protos.user_x_management.GetUserXDetailsRequest;
import org.davincischools.leo.protos.user_x_management.GetUserXDetailsResponse;
import org.davincischools.leo.protos.user_x_management.RegisterUserXRequest;
import org.davincischools.leo.protos.user_x_management.RegisterUserXResponse;
import org.davincischools.leo.protos.user_x_management.RemoveUserXRequest;
import org.davincischools.leo.protos.user_x_management.RemoveUserXResponse;
import org.davincischools.leo.protos.user_x_management.UpsertUserXRequest;
import org.davincischools.leo.protos.user_x_management.UpsertUserXResponse;
import org.davincischools.leo.server.utils.ProtoDaoUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.AdminX;
import org.davincischools.leo.server.utils.http_user_x.Anonymous;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserXManagementService {

  private static final Pattern PASSWORD_REQS =
      Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$");
  private static final Pattern EMAIL_REQS = Pattern.compile("^[^@]+@[^@]+[.][^@]+$");

  @Autowired Database db;
  @Autowired EntityManager entityManager;

  @PostMapping(value = "/api/protos/UserXManagementService/GetPagedUserXsDetails")
  @ResponseBody
  public GetPagedUserXsDetailsResponse getPagedUserXsDetails(
      @AdminX HttpUserX userX,
      @RequestBody Optional<GetPagedUserXsDetailsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetPagedUserXsDetailsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetPagedUserXsDetailsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var pagedUserXs =
                  db.getUserXRepository()
                      .findAllByDistrictId(
                          userX.get().orElseThrow().getDistrict().getId(),
                          "%" + request.getSearchText().toLowerCase() + "%",
                          PageRequest.of(
                              request.getPage(),
                              request.getPageSize(),
                              Sort.by("lastName", "firstName", "emailAddress")));

              GetPagedUserXsDetailsResponse.Builder response =
                  GetPagedUserXsDetailsResponse.newBuilder()
                      .addAllUserXs(
                          pagedUserXs.getContent().stream()
                              .map(e -> ProtoDaoUtils.toFullUserXDetailsProto(e, null).build())
                              .toList())
                      .setTotalUserXs((int) pagedUserXs.getTotalElements());
              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserXManagementService/GetUserXDetails")
  @ResponseBody
  public GetUserXDetailsResponse getUserXDetails(
      @Anonymous HttpUserX userX,
      @RequestBody Optional<GetUserXDetailsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(GetUserXDetailsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              GetUserXDetailsResponse.Builder response = GetUserXDetailsResponse.newBuilder();

              int userXId;
              if (request.hasUserXId()) {
                if (!(userX.isAdminX()
                    || Objects.equals(request.getUserXId(), userX.getUserXIdOrNull()))) {
                  return userX.returnForbidden(GetUserXDetailsResponse.getDefaultInstance());
                }
                userXId = request.getUserXId();
              } else if (userX.isAuthenticated()) {
                userXId = checkNotNull(userX.getUserXIdOrNull());
              } else {
                return userX.returnForbidden(GetUserXDetailsResponse.getDefaultInstance());
              }

              FullUserXDetails.Builder details =
                  getUserXDetails(
                          userXId,
                          request.getIncludeSchools(),
                          request.getIncludeClassXs(),
                          request.getIncludeAllAvailableClassXs(),
                          request.getIncludeKnowledgeAndSkills(),
                          null)
                      .orElse(null);
              if (details == null) {
                return userX.returnNotFound(GetUserXDetailsResponse.getDefaultInstance());
              }

              return response.setUserX(details).build();
            })
        .finish();
  }

  public Optional<FullUserXDetails.Builder> getUserXDetails(
      int userXId,
      boolean includeSchools,
      boolean includeClassXs,
      boolean includeAllAvailableClassXs,
      boolean includeKnowledgeAndSkills,
      @Nullable FullUserXDetails.Builder details) {

    var finalDetails = details != null ? details : FullUserXDetails.newBuilder();

    UserX dbUserX = db.getUserXRepository().findById(userXId).orElse(null);
    if (dbUserX == null) {
      return Optional.empty();
    }
    ProtoDaoUtils.toUserXProto(dbUserX, finalDetails.getUserXBuilder());

    if (dbUserX.getDistrict() != null && dbUserX.getDistrict().getId() != null) {
      ProtoDaoUtils.toDistrictProto(dbUserX.getDistrict(), finalDetails.getDistrictBuilder());
    }

    if (includeSchools) {
      db.getSchoolRepository()
          .findSchools(dbUserX.getTeacher(), dbUserX.getStudent())
          .forEach(school -> ProtoDaoUtils.toSchoolProto(school, finalDetails.addSchoolsBuilder()));
    }

    if (includeClassXs || includeAllAvailableClassXs) {
      db.getClassXRepository()
          .findFullClassXs(
              dbUserX.getTeacher(),
              dbUserX.getStudent(),
              /* schools= */ ImmutableList.of(),
              includeAllAvailableClassXs,
              includeKnowledgeAndSkills)
          .forEach(
              classX -> ProtoDaoUtils.toFullClassXProto(classX, finalDetails.addClassXsBuilder()));
    }

    return Optional.of(finalDetails);
  }

  @PostMapping(value = "/api/protos/UserXManagementService/UpsertUserX")
  @ResponseBody
  @Transactional
  public UpsertUserXResponse upsertUserX(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpsertUserXRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(UpsertUserXResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpsertUserXRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              UpsertUserXResponse.Builder response = UpsertUserXResponse.newBuilder();

              // Verify permissions.
              Integer userXId = null;
              if (request.getUserX().getUserX().hasUserXId()) {
                if (!(userX.isAdminX()
                    || Objects.equals(
                        request.getUserX().getUserX().getUserXId(), userX.getUserXIdOrNull()))) {
                  return userX.returnForbidden(UpsertUserXResponse.getDefaultInstance());
                }
                userXId = request.getUserX().getUserX().getUserXId();
              } else if (!userX.isAdminX()) {
                return userX.returnForbidden(UpsertUserXResponse.getDefaultInstance());
              }

              UserX oldUserX =
                  userXId != null ? db.getUserXRepository().findById(userXId).orElse(null) : null;
              if (oldUserX != null) {
                entityManager.detach(oldUserX);
              }
              // TODO: Make a copy rather than reread.
              UserX newUserX =
                  userXId != null ? db.getUserXRepository().findById(userXId).orElse(null) : null;
              newUserX =
                  newUserX != null
                      ? newUserX
                      : new UserX().setCreationTime(Instant.now()).setId(userXId);
              boolean isNewUserX = oldUserX == null;

              // Set first and last names.
              String newFirstName = request.getUserX().getUserX().getFirstName().trim();
              String newLastName = request.getUserX().getUserX().getLastName().trim();
              if (newFirstName.isEmpty() || newLastName.isEmpty()) {
                return response.setError("Error: Both first and last names are required.").build();
              }
              newUserX.setFirstName(newFirstName);
              newUserX.setLastName(newLastName);

              // Set the e-mail address.
              if (isNewUserX || userX.isAdminX()) {
                String newEmailAddress = request.getUserX().getUserX().getEmailAddress().trim();
                if (!EMAIL_REQS.matcher(newEmailAddress).matches()) {
                  return response.setError("Error: Email address is invalid.").build();
                }
                newUserX.setEmailAddress(newEmailAddress);
              }

              // Change / set the password.
              if (isNewUserX || !request.getNewPassword().isEmpty()) {
                // Check the existing password.
                if (oldUserX != null && !userX.isAdminX()) {
                  if (!UserXUtils.checkPassword(oldUserX, request.getCurrentPassword())) {
                    return response
                        .setError("Access Denied: The current password is incorrect.")
                        .build();
                  }
                }

                // Set the new password.
                if (!request.getNewPassword().equals(request.getVerifyPassword())) {
                  return response.setError("Error: The passwords do not match.").build();
                }
                if (!PASSWORD_REQS.matcher(request.getNewPassword()).matches()) {
                  return response.setError("Error: The password is invalid.").build();
                }

                UserXUtils.setPassword(newUserX, request.getNewPassword());
              }
              if (Strings.isNullOrEmpty(newUserX.getEncodedPassword())) {
                return response.setError("Error: A password is required.").build();
              }

              // Only admins can change the following properties.
              if (userX.isAdminX()) {
                // Update admin privileges.
                if (request.getUserX().getUserX().getIsAdminX()) {
                  if (oldUserX.getAdminX() == null) {
                    newUserX.setAdminX(
                        new org.davincischools.leo.database.daos.AdminX()
                            .setCreationTime(Instant.now()));
                    DaoUtils.removeTransientValues(
                        newUserX.getAdminX(), db.getAdminXRepository()::save);
                  }
                } else {
                  newUserX.setAdminX(null);
                }

                // Update teacher privileges.
                if (request.getUserX().getUserX().getIsTeacher()) {
                  if (oldUserX.getTeacher() == null) {
                    newUserX.setTeacher(
                        new org.davincischools.leo.database.daos.Teacher()
                            .setCreationTime(Instant.now()));
                    DaoUtils.removeTransientValues(
                        newUserX.getTeacher(), db.getTeacherRepository()::save);
                  }
                } else {
                  newUserX.setTeacher(null);
                }

                // Update student privileges.
                if (request.getUserX().getUserX().getIsStudent()) {
                  if (oldUserX.getStudent() == null) {
                    newUserX.setStudent(
                        new org.davincischools.leo.database.daos.Student()
                            .setCreationTime(Instant.now()));
                    DaoUtils.removeTransientValues(
                        newUserX.getStudent(), db.getStudentRepository()::save);
                  }
                } else {
                  newUserX.setStudent(null);
                }

                // Update district.
                Integer requestDistrictId =
                    request.getUserX().getDistrict().hasId()
                        ? request.getUserX().getDistrict().getId()
                        : null;
                Integer oldDistrictId =
                    newUserX.getDistrict() != null ? newUserX.getDistrict().getId() : null;
                if (!Objects.equals(requestDistrictId, oldDistrictId)) {
                  if (requestDistrictId != null) {
                    newUserX.setDistrict(new District().setId(requestDistrictId));
                  } else {
                    newUserX.setDistrict(null);
                  }
                }
              }

              // Update schools.
              // TODO: move this under admin requirements.
              db.getSchoolRepository()
                  .updateSchools(
                      db,
                      newUserX.getTeacher(),
                      newUserX.getStudent(),
                      Lists.transform(
                          request.getUserX().getSchoolsList(), ProtoDaoUtils::toSchoolDao));

              // Update classes.
              // TODO: move this under admin requirements.
              db.getClassXRepository()
                  .updateClassXs(
                      db,
                      newUserX.getTeacher(),
                      newUserX.getStudent(),
                      Lists.transform(
                          request.getUserX().getClassXsList(), ProtoDaoUtils::toClassXDao));

              // Save the updated user.
              DaoUtils.removeTransientValues(newUserX, db.getUserXRepository()::save);

              // Remove dropped permissions.
              if (oldUserX != null) {
                if (newUserX.getAdminX() == null && oldUserX.getAdminX() != null) {
                  DaoUtils.removeTransientValues(
                      oldUserX.getAdminX().setDeleted(Instant.now()),
                      db.getAdminXRepository()::save);
                }
                if (newUserX.getTeacher() == null && oldUserX.getTeacher() != null) {
                  DaoUtils.removeTransientValues(
                      oldUserX.getTeacher().setDeleted(Instant.now()),
                      db.getTeacherRepository()::save);
                }
                if (newUserX.getStudent() == null && oldUserX.getStudent() != null) {
                  DaoUtils.removeTransientValues(
                      oldUserX.getStudent().setDeleted(Instant.now()),
                      db.getStudentRepository()::save);
                }
              }

              final Integer finalUserXId = newUserX.getId();
              getUserXDetails(
                      finalUserXId,
                      /* includeHighSchools= */ true,
                      /* includeClassXs= */ true,
                      /* includeAllAvailableClassXs= */ true,
                      /* includeKnowledgeAndSkills= */ false,
                      response.getUserXBuilder())
                  .orElseThrow(
                      () ->
                          new IllegalStateException(
                              "The user just saved should exist: " + finalUserXId));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserXManagementService/RemoveUserX")
  @ResponseBody
  @Transactional
  public RemoveUserXResponse removeUserX(
      @AdminX HttpUserX userX,
      @RequestBody Optional<RemoveUserXRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(RemoveUserXResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(RemoveUserXRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              checkArgument(request.hasUserXId());

              UserX existingUserX =
                  db.getUserXRepository().findById(request.getUserXId()).orElse(null);

              if (existingUserX != null) {
                if (existingUserX.getAdminX() != null) {
                  db.getAdminXRepository().deleteById(existingUserX.getAdminX().getId());
                }

                if (existingUserX.getTeacher() != null) {
                  db.getTeacherSchoolRepository()
                      .setTeacherSchools(existingUserX.getTeacher(), ImmutableList.of());
                  db.getTeacherClassXRepository()
                      .setTeacherClassXs(existingUserX.getTeacher(), ImmutableList.of());
                  db.getTeacherRepository().delete(existingUserX.getTeacher());
                }

                if (existingUserX.getStudent() != null) {
                  db.getStudentSchoolRepository()
                      .setStudentSchools(existingUserX.getStudent(), ImmutableList.of());
                  db.getStudentClassXRepository()
                      .setStudentClassXs(existingUserX.getStudent(), ImmutableList.of());
                  db.getStudentRepository().delete(existingUserX.getStudent());
                }

                db.getUserXRepository().delete(existingUserX);
              }

              return RemoveUserXResponse.getDefaultInstance();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserXManagementService/RegisterUserX")
  @ResponseBody
  public RegisterUserXResponse registerUserX(
      @RequestBody Optional<RegisterUserXRequest> optionalRequest, HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(RegisterUserXRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = RegisterUserXResponse.newBuilder();

              Optional<UserX> userX =
                  db.getUserXRepository().findByEmailAddress(request.getEmailAddress());
              if (userX.isPresent()) {
                return response.setAccountAlreadyExists(true).build();
              }

              // TODO: Remove when clever is incorporated.
              Optional<District> district = Optional.empty();
              boolean isStudent = false;
              boolean isTeacher = false;
              Student student = null;
              Teacher teacher = null;
              if (request.getEmailAddress().endsWith("davincischools.org")) {
                district =
                    db.getDistrictRepository().findByName("Wiseburn Unified School District");
                isStudent = request.getEmailAddress().endsWith("student.davincischools.org");
                isTeacher = !isStudent;
              }

              Interest interest =
                  db.getInterestRepository()
                      .save(
                          new Interest()
                              .setCreationTime(Instant.now())
                              .setFirstName(
                                  valueOrNull(
                                      request, RegisterUserXRequest.FIRST_NAME_FIELD_NUMBER))
                              .setLastName(
                                  valueOrNull(request, RegisterUserXRequest.LAST_NAME_FIELD_NUMBER))
                              .setEmailAddress(
                                  valueOrNull(
                                      request, RegisterUserXRequest.EMAIL_ADDRESS_FIELD_NUMBER))
                              .setProfession(
                                  valueOrNull(
                                      request, RegisterUserXRequest.PROFESSION_FIELD_NUMBER))
                              .setReasonForInterest(
                                  valueOrNull(
                                      request,
                                      RegisterUserXRequest.REASON_FOR_INTEREST_FIELD_NUMBER))
                              .setDistrictName(
                                  valueOrNull(
                                      request, RegisterUserXRequest.DISTRICT_NAME_FIELD_NUMBER))
                              .setSchoolName(
                                  valueOrNull(
                                      request, RegisterUserXRequest.SCHOOL_NAME_FIELD_NUMBER))
                              .setAddressLine1(
                                  valueOrNull(
                                      request, RegisterUserXRequest.ADDRESS_LINE_1_FIELD_NUMBER))
                              .setAddressLine2(
                                  valueOrNull(
                                      request, RegisterUserXRequest.ADDRESS_LINE_2_FIELD_NUMBER))
                              .setCity(valueOrNull(request, RegisterUserXRequest.CITY_FIELD_NUMBER))
                              .setState(
                                  valueOrNull(request, RegisterUserXRequest.STATE_FIELD_NUMBER))
                              .setZipCode(
                                  valueOrNull(request, RegisterUserXRequest.ZIP_CODE_FIELD_NUMBER))
                              .setNumTeachers(
                                  valueOrNull(
                                      request, RegisterUserXRequest.NUM_TEACHERS_FIELD_NUMBER))
                              .setNumStudents(
                                  valueOrNull(
                                      request, RegisterUserXRequest.NUM_STUDENTS_FIELD_NUMBER)));

              if (isStudent) {
                student =
                    db.getStudentRepository().save(new Student().setCreationTime(Instant.now()));
              }

              if (isTeacher) {
                teacher =
                    db.getTeacherRepository().save(new Teacher().setCreationTime(Instant.now()));
              }

              db.getUserXRepository()
                  .save(
                      UserXUtils.setPassword(
                          new UserX()
                              .setCreationTime(Instant.now())
                              .setDistrict(district.orElse(null))
                              .setStudent(student)
                              .setTeacher(teacher)
                              .setFirstName(
                                  valueOrNull(
                                      request, RegisterUserXRequest.FIRST_NAME_FIELD_NUMBER))
                              .setLastName(
                                  valueOrNull(request, RegisterUserXRequest.LAST_NAME_FIELD_NUMBER))
                              .setEmailAddress(
                                  valueOrNull(
                                      request, RegisterUserXRequest.EMAIL_ADDRESS_FIELD_NUMBER))
                              .setInterest(interest),
                          request.getPassword()));

              return response.build();
            })
        .finish();
  }

  @SuppressWarnings("unchecked")
  private <T> T valueOrNull(Message request, int fieldNumber) {
    FieldDescriptor descriptor = request.getDescriptorForType().findFieldByNumber(fieldNumber);
    if (request.hasField(descriptor)) {
      return (T) request.getField(descriptor);
    }
    return null;
  }
}
