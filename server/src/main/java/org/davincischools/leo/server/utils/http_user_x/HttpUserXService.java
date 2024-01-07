package org.davincischools.leo.server.utils.http_user_x;

import com.google.common.base.Strings;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetUserXsParams;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

@Service
public class HttpUserXService {

  private static final Logger logger = LogManager.getLogger();

  private static Optional<UserX> getAuthenticatedUserX(Database db, EntityManager entityManager) {
    Optional<UserX> optionalUserX = Optional.empty();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      if (auth.getPrincipal() instanceof UserXDetails details) {
        optionalUserX = db.getUserXRepository().findById(details.getUserX().getId());
      } else if (auth.getPrincipal() instanceof OAuth2AuthenticatedPrincipal oauth2
          && !Strings.isNullOrEmpty(oauth2.getAttribute("email"))) {
        optionalUserX =
            db
                .getUserXRepository()
                .getUserXs(new GetUserXsParams().setHasEmailAddress(oauth2.getAttribute("email")))
                .stream()
                .findFirst();
      }
    }
    optionalUserX.ifPresent(
        userX -> {
          entityManager.detach(userX);
          userX.setEncodedPassword(null);
        });
    return optionalUserX;
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
