package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
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
import org.davincischools.leo.server.utils.ProtoDaoConverter;
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
                              .map(e -> ProtoDaoConverter.toFullUserXDetailsProto(e, null).build())
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

              // No user id is the equivalent to a whoami request.
              if (!request.hasUserXId()) {
                if (userX.isAuthenticated()) {
                  getFullUserXDetails(userX.get().get().getId(), response.getUserXBuilder());
                }
                return response.build();
              }

              // If not an admin, make sure the user is getting their own profile.
              int userXId = request.getUserXId();
              if (!userX.isAdminX()) {
                if (userXId != userX.get().orElseThrow().getId()) {
                  return userX.returnForbidden(GetUserXDetailsResponse.getDefaultInstance());
                }
              }

              getFullUserXDetails(userXId, response.getUserXBuilder());

              return response.build();
            })
        .finish();
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

              // Make sure a user is only updating their own profile.
              if (!userX.isAdminX()) {
                if (!request.getUserX().hasUserXId()
                    || request.getUserX().getUserXId() != userX.get().orElseThrow().getId()) {
                  return userX.returnForbidden(UpsertUserXResponse.getDefaultInstance());
                }
              }

              // Lookup existing data and detach.
              Optional<UserX> existingEmailUserX =
                  db.getUserXRepository().findByEmailAddress(request.getUserX().getEmailAddress());
              existingEmailUserX.ifPresent(entityManager::detach);

              // Prepare for modifications.
              var response = UpsertUserXResponse.newBuilder();

              // Possibly load the existing user.
              UserX existingUserX =
                  request.getUserX().hasUserXId()
                      ? db.getUserXRepository()
                          .findById(request.getUserX().getUserXId())
                          .orElse(null)
                      : null;

              // Only an admin can create a user.
              if (existingUserX == null) {
                if (!userX.isAdminX()) {
                  return userX.returnForbidden(UpsertUserXResponse.getDefaultInstance());
                }
                existingUserX = new UserX().setCreationTime(Instant.now());
              }

              // Change / set the password.
              if (request.hasNewPassword()) {
                // Check the existing password.
                if (!userX.isAdminX()) {
                  if (!UserXUtils.checkPassword(existingUserX, request.getCurrentPassword())) {
                    return response
                        .setError("Access Denied: The current password is incorrect.")
                        .build();
                  }
                }

                // Set the new password.
                if (!request.getNewPassword().equals(request.getVerifyPassword())) {
                  return response.setError("Error: The passwords do not match.").build();
                }
                if (!request.getNewPassword().isEmpty()) {
                  if (!PASSWORD_REQS.matcher(request.getNewPassword()).matches()) {
                    return response.setError("Error: The password is invalid.").build();
                  }
                  UserXUtils.setPassword(existingUserX, request.getNewPassword());
                }
              }
              if (Strings.isNullOrEmpty(existingUserX.getEncodedPassword())) {
                return response.setError("Error: New accounts must have a password.").build();
              }

              // Set first and last names.
              String firstName = request.getUserX().getFirstName().trim();
              String lastName = request.getUserX().getLastName().trim();
              if (firstName.isEmpty() || lastName.isEmpty()) {
                return response.setError("Error: Both first and last names are required.").build();
              }
              existingUserX.setFirstName(firstName).setLastName(lastName);

              // Only admins can change the following properties.
              if (userX.isAdminX()) {
                // Set the e-mail address.
                String emailAddress = request.getUserX().getEmailAddress().trim();
                if (!EMAIL_REQS.matcher(emailAddress).matches()) {
                  return response.setError("Error: The e-mail address is invalid.").build();
                }
                if (existingEmailUserX.isPresent()) {
                  if (!existingEmailUserX.get().getId().equals(existingUserX.getId())) {
                    return response
                        .setError("Error: That e-mail address is already in use.")
                        .build();
                  }
                }
                existingUserX.setEmailAddress(emailAddress);

                // Update admin privileges.
                if ((existingUserX.getAdminX() != null) ^ request.getUserX().getIsAdminX()) {
                  if (request.getUserX().getIsAdminX()) {
                    existingUserX.setAdminX(
                        db.getAdminXRepository()
                            .save(
                                new org.davincischools.leo.database.daos.AdminX()
                                    .setCreationTime(Instant.now())));
                  } else {
                    existingUserX.setAdminX(null);
                  }
                }

                //// Update teacher privileges.
                // if ((existingUserX.getTeacher() != null) ^ request.getUserX().getIsTeacher()) {
                // if (request.getUserX().getIsTeacher()) {
                // saveTeacher =
                // Optional.of(
                // Optional.ofNullable(existingUserX.getTeacher())
                //    .orElse(new Teacher().setCreationTime(Instant.now())));
                // existingUserX.setTeacher(saveTeacher.get());
                // } else {
                //// Remove all existing schools.
                // if (existingUserX.getTeacher() != null && existingUserX.getTeacher().getId() !=
                // null) {
                // db.getTeacherSchoolRepository()
                // .keepSchoolsForTeacher(existingUserX.getTeacher().getId(), ImmutableList.of());
                // }
                // removeTeacher = Optional.ofNullable(existingUserX.getTeacher());
                // existingUserX.setTeacher(null);
                // }
                // }
                //// Remove any extraneous schools.
                // if (existingUserX.getTeacher() != null && request.hasSchoolIds()) {
                // db.getTeacherSchoolRepository()
                // .keepSchoolsForTeacher(
                // existingUserX.getTeacher().getId(), request.getSchoolIds().getSchoolIdsList());
                // }

                // Update student privileges.
                if ((existingUserX.getStudent() != null) ^ request.getUserX().getIsStudent()) {
                  if (request.getUserX().getIsStudent()) {
                    existingUserX.setStudent(
                        db.getStudentRepository()
                            .save(new Student().setCreationTime(Instant.now())));
                  } else {
                    existingUserX.setStudent(null);
                  }
                }
                if (request.getUserX().getIsStudent()) {
                  existingUserX
                      .getStudent()
                      .setDistrictStudentId(
                          request.hasDistrictStudentId() ? request.getDistrictStudentId() : null)
                      .setGrade(request.hasStudentGrade() ? request.getStudentGrade() : null);
                }
              }

              //// Add any missing schools.
              // if (userX.getTeacher() != null) {
              // for (int schoolId : request.getSchoolIds().getSchoolIdsList()) {
              // db.getTeacherSchoolRepository()
              // .saveTeacherSchool(
              // userX.getTeacher(),
              // new School().setCreationTime(Instant.now()).setId(schoolId));
              // }
              // }

              if (existingUserX.getAdminX() != null) {
                db.getAdminXRepository().save(existingUserX.getAdminX());
              }
              if (existingUserX.getTeacher() != null) {
                db.getTeacherRepository().save(existingUserX.getTeacher());
              }
              if (existingUserX.getStudent() != null) {
                db.getStudentRepository().save(existingUserX.getStudent());
              }
              db.getUserXRepository().save(existingUserX);

              if (!response.hasError()) {
                response.setUserX(ProtoDaoConverter.toFullUserXDetailsProto(existingUserX, null));
              }
              return response.build();
            })
        .onError(
            (error, log) ->
                Optional.of(
                    UpsertUserXResponse.newBuilder()
                        .setError(error.throwables().get(0).getMessage())
                        .build()))
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
                      .keepSchoolsForTeacher(
                          existingUserX.getTeacher().getId(), ImmutableList.of());
                  db.getTeacherRepository().deleteById(existingUserX.getTeacher().getId());
                }

                if (existingUserX.getStudent() != null) {
                  db.getStudentRepository().deleteById(existingUserX.getStudent().getId());
                }

                db.getUserXRepository().deleteById(existingUserX.getId());
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

  private void getFullUserXDetails(int userXId, FullUserXDetails.Builder details) {
    // If the user doesn't exist, return an empty result.
    UserX userX = db.getUserXRepository().findById(userXId).orElse(null);
    if (userX == null) {
      return;
    }

    // Set user details.
    details.setUserX(ProtoDaoConverter.toUserXProto(userX, null));

    // Set teacher details.
    Optional.ofNullable(userX.getTeacher())
        .map(Teacher::getId)
        .map(db.getTeacherSchoolRepository()::findSchoolsByTeacherId)
        .map(schools -> Lists.transform(schools, School::getId))
        .ifPresent(details::addAllSchoolIds);

    // Set student details.
    Optional.ofNullable(userX.getStudent())
        .map(Student::getDistrictStudentId)
        .ifPresent(details::setDistrictStudentId);
    Optional.ofNullable(userX.getStudent())
        .map(Student::getGrade)
        .ifPresent(details::setStudentGrade);
  }
}
