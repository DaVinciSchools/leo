package org.davincischools.leo.server.utils.http_user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HttpUserService {

  private static Optional<UserX> getAuthenticatedUserX(Database db) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      if (auth.getPrincipal() instanceof UserXDetails details) {
        Optional<UserX> optionalUserX =
            db.getUserXRepository().findById(details.getUserX().getId());
        optionalUserX.ifPresent(userX -> userX.setEncodedPassword(null));
        return optionalUserX;
      }
    }
    return Optional.empty();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUser getAnonymousHttpUser(
      Database db, HttpServletRequest request, HttpServletResponse response) {
    return new HttpUser(getAuthenticatedUserX(db), request, response, false);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUser getAuthenticatedHttpUser(
      Database db, HttpServletRequest request, HttpServletResponse response) {
    return new HttpUser(getAuthenticatedUserX(db), request, response, true);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUser getAdminHttpUser(
      Database db, HttpServletRequest request, HttpServletResponse response) {
    Optional<UserX> userX = getAuthenticatedUserX(db);
    if (userX.isPresent() && HttpUser.isAdmin(userX.get())) {
      return new HttpUser(userX, request, response, true);
    }
    return new HttpUser(Optional.empty(), request, response, true);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUser getTeacherHttpUser(
      Database db, HttpServletRequest request, HttpServletResponse response) {
    Optional<UserX> userX = getAuthenticatedUserX(db);
    if (userX.isPresent() && HttpUser.isTeacher(userX.get())) {
      return new HttpUser(userX, request, response, true);
    }
    return new HttpUser(Optional.empty(), request, response, true);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUser getStudentHttpUser(
      Database db, HttpServletRequest request, HttpServletResponse response) {
    Optional<UserX> userX = getAuthenticatedUserX(db);
    if (userX.isPresent() && HttpUser.isStudent(userX.get())) {
      return new HttpUser(userX, request, response, true);
    }
    return new HttpUser(Optional.empty(), request, response, true);
  }
}
