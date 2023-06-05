package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.protos.user_management.FullUserDetails;
import org.davincischools.leo.protos.user_management.GetUserDetailsRequest;
import org.davincischools.leo.protos.user_management.GetUserDetailsResponse;
import org.davincischools.leo.protos.user_management.GetUsersDetailsRequest;
import org.davincischools.leo.protos.user_management.GetUsersDetailsResponse;
import org.davincischools.leo.protos.user_management.RemoveUserRequest;
import org.davincischools.leo.protos.user_management.RemoveUserResponse;
import org.davincischools.leo.protos.user_management.UpsertUserRequest;
import org.davincischools.leo.protos.user_management.UpsertUserResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user.Admin;
import org.davincischools.leo.server.utils.http_user.Authenticated;
import org.davincischools.leo.server.utils.http_user.HttpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserManagementService {

  private Pattern PASSWORD_REQS = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$");
  private Pattern EMAIL_REQS = Pattern.compile("^[^@]+@[^@]+[.][^@]+$");

  @Autowired Database db;
  @Autowired EntityManager entityManager;

  @PostMapping(value = "/api/protos/UserManagementService/GetUsersDetails")
  @ResponseBody
  public GetUsersDetailsResponse getUsersDetails(
      @Admin HttpUser user,
      @RequestBody Optional<GetUsersDetailsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetUsersDetailsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetUsersDetailsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var pagedUsers =
                  db.getUserXRepository()
                      .findAllByDistrictId(
                          user.get().getDistrict().getId(),
                          "%" + request.getSearchText().toLowerCase() + "%",
                          PageRequest.of(
                              request.getPage(),
                              request.getPageSize(),
                              Sort.by("lastName", "firstName", "emailAddress")));

              GetUsersDetailsResponse.Builder response =
                  GetUsersDetailsResponse.newBuilder()
                      .addAllUsers(
                          pagedUsers.getContent().stream()
                              .map(DataAccess::convertFullUserXToDetailsProto)
                              .toList())
                      .setTotalUsers((int) pagedUsers.getTotalElements());
              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserManagementService/GetUserDetails")
  @ResponseBody
  public GetUserDetailsResponse getUserDetails(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetUserDetailsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetUserDetailsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetUserDetailsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {

              // If not an admin, make sure the user is getting their own profile.
              int userId = request.getUserXId();
              if (!user.isAdmin()) {
                if (userId != user.get().getId()) {
                  return user.returnForbidden(GetUserDetailsResponse.getDefaultInstance());
                }
              }

              GetUserDetailsResponse.Builder response = GetUserDetailsResponse.newBuilder();

              getFullUserDetails(userId, response.getUserBuilder());

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserManagementService/UpsertUser")
  @ResponseBody
  @Transactional
  public UpsertUserResponse upsertUser(
      @Authenticated HttpUser user,
      @RequestBody Optional<UpsertUserRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(UpsertUserResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpsertUserRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {

              // Make sure a user is only updating their own profile.
              if (!user.isAdmin()) {
                if (!request.getUser().hasId() || request.getUser().getId() != user.get().getId()) {
                  return user.returnForbidden(UpsertUserResponse.getDefaultInstance());
                }
              }

              // Lookup existing data and detach.
              Optional<UserX> existingEmailUser =
                  db.getUserXRepository().findByEmailAddress(request.getUser().getEmailAddress());
              existingEmailUser.ifPresent(entityManager::detach);

              // Prepare for modifications.
              var response = UpsertUserResponse.newBuilder();
              Optional<AdminX> saveAdminX = Optional.empty();
              Optional<Teacher> saveTeacher = Optional.empty();
              Optional<Student> saveStudent = Optional.empty();
              Optional<AdminX> removeAdminX = Optional.empty();
              Optional<Teacher> removeTeacher = Optional.empty();
              Optional<Student> removeStudent = Optional.empty();

              // Possibly load the existing user.
              UserX userX =
                  request.getUser().hasId()
                      ? db.getUserXRepository().findById(request.getUser().getId()).orElse(null)
                      : null;

              // Only an admin can create a user.
              if (userX == null) {
                if (!user.isAdmin()) {
                  return user.returnForbidden(UpsertUserResponse.getDefaultInstance());
                }
                userX =
                    new UserX()
                        .setCreationTime(Instant.now())
                        .setDistrict(new District().setId(user.get().getDistrict().getId()))
                        .setStudent(
                            db.getStudentRepository()
                                .save(
                                    new Student()
                                        .setCreationTime(Instant.now())
                                        .setStudentId(-1)
                                        .setGrade(-1)));
              }

              // If it's a new account, a password is required.
              if (userX.getId() == null
                  && !PASSWORD_REQS.matcher(request.getNewPassword()).matches()) {
                return response.setError("Error: New accounts must have a password.").build();
              }

              // Set first and last names.
              String firstName = request.getUser().getFirstName().trim();
              String lastName = request.getUser().getLastName().trim();
              if (firstName.isEmpty() || lastName.isEmpty()) {
                return response.setError("Error: Both first and last names are required.").build();
              }
              userX.setFirstName(firstName).setLastName(lastName);

              // Change / set the password.
              if (request.hasNewPassword()) {
                // Check the existing password.
                if (!user.isAdmin()) {
                  if (!UserUtils.checkPassword(userX, request.getCurrentPassword())) {
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
                  UserUtils.setPassword(userX, request.getNewPassword());
                }
              }

              // Only admins can change the following properties.
              if (user.isAdmin()) {
                // Set the e-mail address.
                String emailAddress = request.getUser().getEmailAddress().trim();
                if (!EMAIL_REQS.matcher(emailAddress).matches()) {
                  return response.setError("Error: The e-mail address is invalid.").build();
                }
                if (existingEmailUser.isPresent()) {
                  if (!existingEmailUser.get().getId().equals(userX.getId())) {
                    return response
                        .setError("Error: That e-mail address is already in use.")
                        .build();
                  }
                }
                userX.setEmailAddress(emailAddress);

                // These need to be fixed in that they break foreign-key constraints.

                //// Update admin privileges.
                // if ((userX.getAdminX() != null) ^ request.getUser().getIsAdmin()) {
                // if (request.getUser().getIsAdmin()) {
                // saveAdminX =
                // Optional.of(
                // Optional.ofNullable(userX.getAdminX())
                //    .orElse(new AdminX().setCreationTime(Instant.now())));
                // userX.setAdminX(saveAdminX.get());
                // } else {
                // removeAdminX = Optional.ofNullable(userX.getAdminX());
                // userX.setAdminX(null);
                // }
                // }
                //
                //// Update teacher privileges.
                // if ((userX.getTeacher() != null) ^ request.getUser().getIsTeacher()) {
                // if (request.getUser().getIsTeacher()) {
                // saveTeacher =
                // Optional.of(
                // Optional.ofNullable(userX.getTeacher())
                //    .orElse(new Teacher().setCreationTime(Instant.now())));
                // userX.setTeacher(saveTeacher.get());
                // } else {
                //// Remove all existing schools.
                // if (userX.getTeacher() != null && userX.getTeacher().getId() != null) {
                // db.getTeacherSchoolRepository()
                // .keepSchoolsForTeacher(userX.getTeacher().getId(), ImmutableList.of());
                // }
                // removeTeacher = Optional.ofNullable(userX.getTeacher());
                // userX.setTeacher(null);
                // }
                // }
                //// Remove any extraneous schools.
                // if (userX.getTeacher() != null && request.hasSchoolIds()) {
                // db.getTeacherSchoolRepository()
                // .keepSchoolsForTeacher(
                // userX.getTeacher().getId(), request.getSchoolIds().getSchoolIdsList());
                // }
                //
                //// Update student privileges.
                // if ((userX.getStudent() != null) ^ request.getUser().getIsStudent()) {
                // if (request.getUser().getIsStudent()) {
                // saveStudent =
                // Optional.of(
                // Optional.ofNullable(userX.getStudent())
                //    .orElse(new Student().setCreationTime(Instant.now())));
                // userX.setStudent(
                // saveStudent
                // .get()
                // .setStudentId(request.hasStudentId() ? request.getStudentId() : null)
                // .setGrade(
                //    request.hasStudentGrade() ? request.getStudentGrade() : null));
                // } else {
                // removeStudent = Optional.ofNullable(userX.getStudent());
                // userX.setStudent(null);
                // }
                // }
              }

              // Save updates.
              saveAdminX.ifPresent(db.getAdminXRepository()::save);
              saveTeacher.ifPresent(db.getTeacherRepository()::save);
              saveStudent.ifPresent(db.getStudentRepository()::save);

              // This needs to be fixed in that it breaks foreign key constraints.

              //// Add any missing schools.
              // if (userX.getTeacher() != null) {
              // for (int schoolId : request.getSchoolIds().getSchoolIdsList()) {
              // db.getTeacherSchoolRepository()
              // .saveTeacherSchool(
              // userX.getTeacher(),
              // new School().setCreationTime(Instant.now()).setId(schoolId));
              // }
              // }

              db.getUserXRepository().save(userX);
              removeAdminX.ifPresent(db.getAdminXRepository()::delete);
              removeTeacher.ifPresent(db.getTeacherRepository()::delete);
              removeStudent.ifPresent(db.getStudentRepository()::delete);

              return response.build();
            })
        .onError(
            (error, log) -> {
              return Optional.of(
                  UpsertUserResponse.newBuilder()
                      .setError(error.throwables().get(0).getMessage())
                      .build());
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserManagementService/RemoveUser")
  @ResponseBody
  @Transactional
  public RemoveUserResponse removeUser(
      @Admin HttpUser user,
      @RequestBody Optional<RemoveUserRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(RemoveUserResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(RemoveUserRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              checkArgument(request.hasUserXId());

              UserX userX = db.getUserXRepository().findById(request.getUserXId()).orElse(null);

              if (userX != null) {
                if (userX.getAdminX() != null) {
                  db.getAdminXRepository().deleteById(userX.getAdminX().getId());
                }

                if (userX.getTeacher() != null) {
                  db.getTeacherSchoolRepository()
                      .keepSchoolsForTeacher(userX.getTeacher().getId(), ImmutableList.of());
                  db.getTeacherRepository().deleteById(userX.getTeacher().getId());
                }

                if (userX.getStudent() != null) {
                  db.getStudentRepository().deleteById(userX.getStudent().getId());
                }

                db.getUserXRepository().deleteById(userX.getId());
              }

              return RemoveUserResponse.getDefaultInstance();
            })
        .finish();
  }

  private void getFullUserDetails(int userId, FullUserDetails.Builder details) {
    // If the user doesn't exist, return an empty result.
    UserX userX = db.getUserXRepository().findById(userId).orElse(null);
    if (userX == null) {
      return;
    }

    // Set user details.
    details.setUser(DataAccess.convertFullUserXToProto(userX));

    // Set teacher details.
    Optional.ofNullable(userX.getTeacher())
        .map(Teacher::getId)
        .map(db.getTeacherSchoolRepository()::findSchoolsByTeacherId)
        .map(schools -> Streams.stream(schools).map(School::getId).toList())
        .ifPresent(details::addAllSchoolIds);

    // Set student details.
    Optional.ofNullable(userX.getStudent())
        .map(Student::getStudentId)
        .ifPresent(details::setStudentId);
    Optional.ofNullable(userX.getStudent())
        .map(Student::getGrade)
        .ifPresent(details::setStudentGrade);
  }
}
