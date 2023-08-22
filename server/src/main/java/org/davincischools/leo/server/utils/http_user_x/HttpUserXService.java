package org.davincischools.leo.server.utils.http_user_x;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HttpUserXService {

  private static Optional<UserX> getAuthenticatedUserX(Database db, EntityManager entityManager) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      if (auth.getPrincipal() instanceof UserXDetails details) {
        Optional<UserX> optionalUserX =
            db.getUserXRepository().findById(details.getUserX().getId());
        optionalUserX.ifPresent(
            userX -> {
              entityManager.detach(userX);
              userX.setEncodedPassword(null);
            });
        return optionalUserX;
      }
    }
    return Optional.empty();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUserX getAnonymousHttpUserX(
      Database db,
      HttpServletRequest request,
      HttpServletResponse response,
      EntityManager entityManager) {
    return new HttpUserX(getAuthenticatedUserX(db, entityManager), request, response, false);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUserX getAuthenticatedHttpUserX(
      Database db,
      HttpServletRequest request,
      HttpServletResponse response,
      EntityManager entityManager) {
    return new HttpUserX(getAuthenticatedUserX(db, entityManager), request, response, true);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUserX getAdminXHttpUserX(
      Database db,
      HttpServletRequest request,
      HttpServletResponse response,
      EntityManager entityManager) {
    Optional<UserX> userX = getAuthenticatedUserX(db, entityManager);
    if (userX.isPresent() && UserXRepository.isAdminX(userX.get())) {
      return new HttpUserX(userX, request, response, true);
    }
    return new HttpUserX(Optional.empty(), request, response, true);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUserX getTeacherHttpUserX(
      Database db,
      HttpServletRequest request,
      HttpServletResponse response,
      EntityManager entityManager) {
    Optional<UserX> userX = getAuthenticatedUserX(db, entityManager);
    if (userX.isPresent() && UserXRepository.isTeacher(userX.get())) {
      return new HttpUserX(userX, request, response, true);
    }
    return new HttpUserX(Optional.empty(), request, response, true);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static HttpUserX getStudentHttpUserX(
      Database db,
      HttpServletRequest request,
      HttpServletResponse response,
      EntityManager entityManager) {
    Optional<UserX> userX = getAuthenticatedUserX(db, entityManager);
    if (userX.isPresent() && UserXRepository.isStudent(userX.get())) {
      return new HttpUserX(userX, request, response, true);
    }
    return new HttpUserX(Optional.empty(), request, response, true);
  }
}
