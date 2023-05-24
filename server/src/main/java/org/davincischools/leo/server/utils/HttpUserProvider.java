package org.davincischools.leo.server.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class HttpUserProvider {

  @Target({ElementType.PARAMETER, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  @Documented
  public @interface Anonymous {}

  @Target({ElementType.PARAMETER, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  @Documented
  public @interface Admin {}

  @Target({ElementType.PARAMETER, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  @Documented
  public @interface Teacher {}

  @Target({ElementType.PARAMETER, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  @Documented
  public @interface Student {}

  @Target({ElementType.PARAMETER, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  @Documented
  public @interface Authenticated {}

  public static boolean isAdmin(Optional<UserX> userX) {
    return userX.isPresent() && isAdmin(userX.get());
  }

  public static boolean isAdmin(UserX userX) {
    return userX.getAdminX() != null && userX.getAdminX().getId() != null;
  }

  public static boolean isTeacher(Optional<UserX> userX) {
    return userX.isPresent() && isTeacher(userX.get());
  }

  public static boolean isTeacher(UserX userX) {
    return userX.getTeacher() != null && userX.getTeacher().getId() != null;
  }

  public static boolean isStudent(Optional<UserX> userX) {
    return userX.isPresent() && isStudent(userX.get());
  }

  public static boolean isStudent(UserX userX) {
    return userX.getStudent() != null && userX.getStudent().getId() != null;
  }

  @Bean
  @Anonymous
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static Optional<UserX> getUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      if (auth.getPrincipal() instanceof UserXDetails details) {
        return Optional.of(details.getUserX());
      }
    }
    return Optional.empty();
  }

  @Bean
  @Admin
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static UserX getAdminUser(@Authenticated UserX userX) {
    if (isAdmin(userX)) {
      return userX;
    }
    throw new IllegalArgumentException("access denied: admin required");
  }

  @Bean
  @Teacher
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static UserX getTeacherUser(@Authenticated UserX userX) {
    if (isTeacher(userX)) {
      return userX;
    }
    throw new IllegalArgumentException("access denied: teacher required");
  }

  @Bean
  @Student
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static UserX getStudentUser(@Authenticated UserX userX) {
    if (isStudent(userX)) {
      return userX;
    }
    throw new IllegalArgumentException("access denied: student required");
  }

  @Bean
  @Authenticated
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public static UserX getAuthenticatedUser(@Anonymous Optional<UserX> user) {
    if (user.isPresent()) {
      return user.get();
    }
    throw new IllegalArgumentException("access denied: authenticated user required");
  }

  private HttpUserProvider() {}
}
