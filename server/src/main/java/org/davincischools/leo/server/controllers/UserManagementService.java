package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static org.davincischools.leo.database.utils.EntityUtils.checkRequired;
import static org.davincischools.leo.database.utils.EntityUtils.checkRequiredMaxLength;
import static org.davincischools.leo.database.utils.EntityUtils.checkThat;
import static org.davincischools.leo.server.utils.http_user.HttpUser.isTeacher;

import com.google.common.collect.Iterables;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.protos.user_management.GetUserDetailsRequest;
import org.davincischools.leo.protos.user_management.GetUserDetailsResponse;
import org.davincischools.leo.protos.user_management.GetUsersRequest;
import org.davincischools.leo.protos.user_management.RemoveUserRequest;
import org.davincischools.leo.protos.user_management.UpsertUserRequest;
import org.davincischools.leo.protos.user_management.UserInformationResponse;
import org.davincischools.leo.protos.user_management.UserInformationResponse.Builder;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.davincischools.leo.server.utils.http_user.Admin;
import org.davincischools.leo.server.utils.http_user.Authenticated;
import org.davincischools.leo.server.utils.http_user.HttpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/UserManagementService/GetUsers")
  @ResponseBody
  @Transactional
  public UserInformationResponse getUserXs(
      @Admin HttpUser user, @RequestBody Optional<GetUsersRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(UserInformationResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(db, optionalRequest.orElse(GetUsersRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              checkArgument(request.hasDistrictId());
              var response = UserInformationResponse.newBuilder();

              getAllFullUserXs(request.getDistrictId(), -1, response);
              response.setSuccess(true);
              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserManagementService/GetUserDetails")
  @ResponseBody
  @Transactional
  public GetUserDetailsResponse getUserXDetails(
      @Authenticated HttpUser user, @RequestBody Optional<GetUserDetailsRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetUserDetailsResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GetUserDetailsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              int userId = request.getUserXId();
              if (user.isAdmin()) {
                // Do nothing.
              } else {
                // Make sure the user is only getting their own details.
                if (userId != user.get().getId()) {
                  return user.returnForbidden(GetUserDetailsResponse.getDefaultInstance());
                }
              }

              UserX details = db.getUserXRepository().findById(userId).orElseThrow();
              var response = GetUserDetailsResponse.newBuilder();

              response.setUser(DataAccess.convertFullUserXToProto(details));
              if (isTeacher(details)) {
                response.addAllSchoolIds(
                    Iterables.transform(
                        db.getSchoolRepository().findAllByTeacherId(details.getTeacher().getId()),
                        School::getId));
              }

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/UserManagementService/UpsertUser")
  @ResponseBody
  @Transactional
  public UserInformationResponse upsertUserX(
      @Authenticated HttpUser user,
      @RequestBody Optional<UpsertUserRequest> optionalRequest,
      HttpServletRequest httpRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(UserInformationResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(UpsertUserRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              int userId = request.getUser().getId();
              if (user.isAdmin()) {
                // Do nothing.
              } else {
                // Make sure the user is only getting their own details.
                if (userId != user.get().getId()) {
                  return user.returnForbidden(UserInformationResponse.getDefaultInstance());
                }
              }

              var response = UserInformationResponse.newBuilder();

              Optional<UserX> existingUserX = Optional.empty();
              if (request.getUser().hasId()) {
                existingUserX = db.getUserXRepository().findById(request.getUser().getId());
              }

              if (setFieldErrors(request, response, existingUserX)) {
                response.setSuccess(false);
                return response.build();
              }

              UserX userX =
                  new UserX()
                      .setCreationTime(Instant.now())
                      .setDistrict(
                          new District()
                              .setCreationTime(Instant.now())
                              .setId(request.getUser().getDistrictId()))
                      .setFirstName(request.getUser().getFirstName())
                      .setLastName(request.getUser().getLastName())
                      .setEmailAddress(request.getUser().getEmailAddress());
              existingUserX.ifPresent(
                  e -> {
                    userX.setId(e.getId());
                    userX.setAdminX(e.getAdminX());
                    userX.setTeacher(e.getTeacher());
                    userX.setStudent(e.getStudent());
                    userX.setEncodedPassword(e.getEncodedPassword());
                  });

              if (!request.getUser().getPassword().isEmpty()) {
                UserUtils.setPassword(userX, request.getUser().getPassword());
              }

              if ((userX.getAdminX() != null) ^ request.getUser().getIsAdmin()) {
                if (request.getUser().getIsAdmin()) {
                  userX.setAdminX(
                      db.getAdminXRepository().save(new AdminX().setCreationTime(Instant.now())));
                } else {
                  userX.setAdminX(null);
                }
              }

              if ((userX.getTeacher() != null) ^ request.getUser().getIsTeacher()) {
                if (request.getUser().getIsTeacher()) {
                  userX.setTeacher(
                      db.getTeacherRepository().save(new Teacher().setCreationTime(Instant.now())));
                } else {
                  userX.setTeacher(null);
                }
              }
              if (userX.getTeacher() != null) {
                if (existingUserX.isPresent()) {
                  // Remove any extraneous schools.
                  db.getTeacherSchoolRepository()
                      .keepSchoolsForTeacher(
                          userX.getTeacher().getId(), request.getSchoolIdsList());
                }

                // Add missing schools.
                for (int schoolId : request.getSchoolIdsList()) {
                  db.getTeacherSchoolRepository()
                      .saveTeacherSchool(
                          userX.getTeacher(),
                          new School().setCreationTime(Instant.now()).setId(schoolId));
                }
              }

              if ((userX.getStudent() != null) ^ request.getUser().getIsStudent()) {
                if (request.getUser().getIsStudent()) {
                  // TODO: Set the student ID.
                  userX.setStudent(
                      db.getStudentRepository()
                          .save(new Student().setCreationTime(Instant.now()).setStudentId(-1)));
                } else {
                  userX.setStudent(null);
                }
              }

              db.getUserXRepository().save(userX);

              existingUserX.ifPresent(
                  e -> {
                    if (e.getAdminX() != null && userX.getAdminX() == null) {
                      db.getAdminXRepository().delete(e.getAdminX());
                    }
                    if (e.getTeacher() != null && userX.getTeacher() == null) {
                      db.getTeacherRepository().delete(e.getTeacher());
                    }
                    if (e.getStudent() != null && userX.getStudent() == null) {
                      db.getStudentRepository().delete(e.getStudent());
                    }
                  });

              getAllFullUserXs(request.getUser().getDistrictId(), userX.getId(), response);
              response.setSuccess(true);
              return response.build();
            })
        .finish();
  }

  /** Checks fields for errors. Sets error messages and returns true if there are any errors. */
  private boolean setFieldErrors(
      UpsertUserRequest request, Builder response, Optional<UserX> existingUser) {
    boolean inputValid = true;

    inputValid &=
        checkRequiredMaxLength(
            request.getUser().getFirstName(),
            "First name",
            Database.USER_MAX_FIRST_NAME_LENGTH,
            response::setFirstNameError);
    inputValid &=
        checkRequiredMaxLength(
            request.getUser().getLastName(),
            "Last name",
            Database.USER_MAX_LAST_NAME_LENGTH,
            response::setLastNameError);
    inputValid &=
        checkThat(
            UserUtils.isEmailAddressValid(request.getUser().getEmailAddress()),
            response::setEmailAddressError,
            "Invalid email address.");
    inputValid &=
        checkRequiredMaxLength(
            request.getUser().getEmailAddress(),
            "Email address",
            Database.USER_MAX_EMAIL_ADDRESS_LENGTH,
            response::setEmailAddressError);

    inputValid &=
        checkThat(
            request.getUser().getPassword().equals(request.getUser().getVerifyPassword()),
            error -> {
              response.setPasswordError(error);
              response.setVerifyPasswordError(error);
            },
            "Passwords do not match.");

    if (existingUser.isPresent()) {
      if (!request.getUser().getPassword().isEmpty()
          || !request.getUser().getVerifyPassword().isEmpty()) {
        inputValid &=
            checkThat(
                request.getUser().getPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
                response::setPasswordError,
                "Password must be at least %s characters long.",
                Database.USER_MIN_PASSWORD_LENGTH);
        inputValid &=
            checkThat(
                request.getUser().getVerifyPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
                response::setVerifyPasswordError,
                "Password must be at least %s characters long.",
                Database.USER_MIN_PASSWORD_LENGTH);
      }
    } else {
      inputValid &=
          checkRequired(request.getUser().getPassword(), "Password", response::setPasswordError);
      inputValid &=
          checkThat(
              request.getUser().getPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
              response::setPasswordError,
              "Password must be at least %s characters long.",
              Database.USER_MIN_PASSWORD_LENGTH);

      inputValid &=
          checkRequired(
              request.getUser().getVerifyPassword(), "Password", response::setVerifyPasswordError);
      inputValid &=
          checkThat(
              request.getUser().getVerifyPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
              response::setVerifyPasswordError,
              "Password must be at least %s characters long.",
              Database.USER_MIN_PASSWORD_LENGTH);
    }

    Optional<UserX> emailUser =
        db.getUserXRepository().findByEmailAddress(request.getUser().getEmailAddress());
    if (emailUser.isPresent()) {
      inputValid &=
          checkThat(
              existingUser.isPresent()
                  && emailUser.get().getId().equals(existingUser.get().getId()),
              response::setEmailAddressError,
              "Email address is already in use.");
    }

    return !inputValid;
  }

  @PostMapping(value = "/api/protos/UserManagementService/RemoveUser")
  @ResponseBody
  @Transactional
  public UserInformationResponse removeUser(
      @Admin HttpUser user,
      @RequestBody Optional<RemoveUserRequest> optionalRequest,
      HttpServletRequest httpRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(UserInformationResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(RemoveUserRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              checkArgument(request.hasUserXId());
              var response = UserInformationResponse.newBuilder();

              db.getUserXRepository().deleteById(request.getUserXId());
              getAllFullUserXs(request.getDistrictId(), -1, response);
              response.setSuccess(true);
              return response.build();
            })
        .finish();
  }

  private void getAllFullUserXs(int districtId, int nextUserXId, Builder response) {
    response.setDistrictId(districtId);
    response.setNextUserXId(nextUserXId);
    response.addAllUsers(DataAccess.getProtoFullUserXsByDistrictId(db, districtId));
  }
}
