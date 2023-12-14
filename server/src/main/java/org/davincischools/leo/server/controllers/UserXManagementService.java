package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.listOrNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toFullUserXDetailsProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.valueOrNull;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserXUtils;
import org.davincischools.leo.database.utils.repos.GetClassXsParams;
import org.davincischools.leo.database.utils.repos.GetUserXsParams;
import org.davincischools.leo.protos.user_x_management.GetUserXsRequest;
import org.davincischools.leo.protos.user_x_management.GetUserXsResponse;
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
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
              if (request.getUserX().getUserX().hasId()) {
                if (!(userX.isAdminX()
                    || Objects.equals(
                        request.getUserX().getUserX().getId(), userX.getUserXIdOrNull()))) {
                  return userX.returnForbidden(UpsertUserXResponse.getDefaultInstance());
                }
                userXId = request.getUserX().getUserX().getId();
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
                Optional<UserX> oldEmailAccount =
                    db.getUserXRepository().findByEmailAddress(newEmailAddress);
                if (oldEmailAccount.isPresent()
                    && !isNewUserX
                    && !Objects.equals(oldEmailAccount.get().getId(), oldUserX.getId())) {
                  return response.setError("Error: Email address is already in use.").build();
                }
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
                  if (oldUserX == null || oldUserX.getAdminX() == null) {
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
                  if (oldUserX == null || oldUserX.getTeacher() == null) {
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
                  if (oldUserX == null || oldUserX.getStudent() == null) {
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
                      request.getUserX().getSchoolsList().stream()
                          .map(ProtoDaoUtils::toSchoolDao)
                          .filter(Optional::isPresent)
                          .map(Optional::get)
                          .toList());

              // Update classes.
              // TODO: move this under admin requirements.
              List<ClassX> classXs =
                  request.getUserX().getClassXsList().stream()
                      .map(ProtoDaoUtils::toClassXDao)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList();
              if (newUserX.getTeacher() != null) {
                db.getTeacherClassXRepository().setTeacherClassXs(newUserX.getTeacher(), classXs);
              }
              if (newUserX.getStudent() != null) {
                db.getStudentClassXRepository().setStudentClassXs(newUserX.getStudent(), classXs);
              }

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

              // TODO: Restore knowledge and skill.
              final Integer finalUserXId = newUserX.getId();
              db
                  .getUserXRepository()
                  .getUserXs(
                      new GetUserXsParams()
                          .setInUserXIds(List.of(finalUserXId))
                          .setIncludeClassXs(new GetClassXsParams()))
                  .stream()
                  .findFirst()
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
              } else if (request.getEmailAddress().endsWith("summitps.org")) {
                district = db.getDistrictRepository().findByName("Summit Public Schools");
                isStudent = request.getEmailAddress().endsWith("mysummitps.org");
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

  @PostMapping(value = "/api/protos/UserXManagementService/GetUserXs")
  @ResponseBody
  public GetUserXsResponse getUserX(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetUserXsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetUserXsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetUserXsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (request.getOfSelf()) {
                request =
                    request.toBuilder()
                        .clearInUserXIds()
                        .addInUserXIds(checkNotNull(userX.getUserXIdOrNull()))
                        .build();
              }

              if (!userX.isAdminX() && !userX.isTeacher()) {
                if (!request
                    .getInUserXIdsList()
                    .equals(List.of(checkNotNull(userX.getUserXIdOrNull())))) {
                  return userX.returnForbidden(GetUserXsResponse.getDefaultInstance());
                }
              }

              var response = GetUserXsResponse.newBuilder();

              Page<UserX> userXs =
                  db.getUserXRepository()
                      .getUserXs(
                          new GetUserXsParams()
                              .setIncludeSchools(
                                  valueOrNull(
                                      request, GetUserXsRequest.INCLUDE_SCHOOLS_FIELD_NUMBER))
                              .setIncludeClassXs(
                                  request.getIncludeClassXs()
                                      ? new GetClassXsParams().setIncludeSchool(true)
                                      : null)
                              .setInDistrictIds(
                                  listOrNull(
                                      request, GetUserXsRequest.IN_DISTRICT_IDS_FIELD_NUMBER))
                              .setInUserXIds(
                                  listOrNull(request, GetUserXsRequest.IN_USER_X_IDS_FIELD_NUMBER))
                              .setInSchoolIds(
                                  listOrNull(request, GetUserXsRequest.IN_SCHOOL_IDS_FIELD_NUMBER))
                              .setInClassXIds(
                                  listOrNull(request, GetUserXsRequest.IN_CLASS_X_IDS_FIELD_NUMBER))
                              .setHasEmailAddress(
                                  valueOrNull(
                                      request, GetUserXsRequest.HAS_EMAIL_ADDRESS_FIELD_NUMBER))
                              .setAdminXsOnly(
                                  valueOrNull(request, GetUserXsRequest.ADMIN_XS_ONLY_FIELD_NUMBER))
                              .setTeachersOnly(
                                  valueOrNull(request, GetUserXsRequest.TEACHERS_ONLY_FIELD_NUMBER))
                              .setStudentsOnly(
                                  valueOrNull(request, GetUserXsRequest.STUDENTS_ONLY_FIELD_NUMBER))
                              .setFirstLastEmailSearchText(
                                  valueOrNull(
                                      request,
                                      GetUserXsRequest.FIRST_LAST_EMAIL_SEARCH_TEXT_FIELD_NUMBER))
                              .setPage(valueOrNull(request, GetUserXsRequest.PAGE_FIELD_NUMBER))
                              .setPageSize(
                                  valueOrNull(request, GetUserXsRequest.PAGE_SIZE_FIELD_NUMBER)));
              userXs.forEach(e -> toFullUserXDetailsProto(e, response::addUserXsBuilder));
              response.setTotalUserXs(userXs.getTotalElements());

              return response.build();
            })
        .finish();
  }
}
