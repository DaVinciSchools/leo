package org.davincischools.leo.server.utils.http_user_x;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nullable;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class HttpUserX {

  private final Optional<UserX> userX;
  private final HttpServletRequest request;
  private final HttpServletResponse response;
  private final boolean authorized;

  public HttpUserX(
      Optional<UserX> userX,
      HttpServletRequest request,
      HttpServletResponse response,
      boolean requireAuthenticated) {
    checkArgument(userX.isEmpty() || userX.get().getId() != null);
    this.userX = checkNotNull(userX);
    this.request = checkNotNull(request);
    this.response = checkNotNull(response);
    this.authorized = !requireAuthenticated || userX.isPresent();
  }

  public boolean isNotAuthorized() {
    return !authorized;
  }

  public <T> T returnForbidden(T response) {
    this.response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    return response;
  }

  public <T> T returnNotFound(T response) {
    this.response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return response;
  }

  public <T> T forwardToLogin(T response) throws IOException {
    this.response.sendRedirect("/users/login");
    return response;
  }

  public HttpUserX logout() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    SecurityContextHolder.getContext().setAuthentication(null);
    return this;
  }

  public boolean isAdminX() {
    return userX.isPresent() && UserXRepository.isAdminX(userX.get());
  }

  @Nullable
  public Integer getAdminXIdOrNull() {
    return isAdminX() ? userX.orElseThrow().getAdminX().getId() : null;
  }

  @Nullable
  public AdminX getAdminXOrNull() {
    return isAdminX() ? userX.map(UserX::getAdminX).orElse(null) : null;
  }

  public boolean isTeacher() {
    return userX.isPresent() && UserXRepository.isTeacher(userX.get());
  }

  @Nullable
  public Integer getTeacherIdOrNull() {
    return isTeacher() ? userX.orElseThrow().getTeacher().getId() : null;
  }

  @Nullable
  public Teacher getTeacherOrNull() {
    return isTeacher() ? userX.map(UserX::getTeacher).orElse(null) : null;
  }

  public boolean isStudent() {
    return userX.isPresent() && UserXRepository.isStudent(userX.get());
  }

  @Nullable
  public Integer getStudentIdOrNull() {
    return isStudent() ? userX.orElseThrow().getStudent().getId() : null;
  }

  @Nullable
  public Student getStudentOrNull() {
    return isStudent() ? userX.map(UserX::getStudent).orElse(null) : null;
  }

  public boolean isAuthenticated() {
    return userX.isPresent();
  }

  @Nullable
  public Integer getUserXIdOrNull() {
    return isAuthenticated() ? userX.orElseThrow().getId() : null;
  }

  @Nullable
  public UserX getUserXOrNull() {
    return isAuthenticated() ? userX.orElse(null) : null;
  }

  public boolean isDemo() {
    return userX.isPresent() && UserXRepository.isDemo(userX.get());
  }

  public Optional<UserX> get() {
    return userX;
  }
}
