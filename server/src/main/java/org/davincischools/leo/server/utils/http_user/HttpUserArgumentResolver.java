package org.davincischools.leo.server.utils.http_user;

import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import org.apache.commons.lang3.function.TriFunction;
import org.davincischools.leo.database.utils.Database;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HttpUserArgumentResolver implements HandlerMethodArgumentResolver {

  private static final ImmutableMap<
          Class<? extends Annotation>,
          TriFunction<Database, HttpServletRequest, HttpServletResponse, HttpUser>>
      HTTP_USER_ANNOTATIONS_MAP =
          ImmutableMap
              .<Class<? extends Annotation>,
                  TriFunction<Database, HttpServletRequest, HttpServletResponse, HttpUser>>
                  builderWithExpectedSize(5)
              .put(Admin.class, HttpUserService::getAdminHttpUser)
              .put(Teacher.class, HttpUserService::getTeacherHttpUser)
              .put(Student.class, HttpUserService::getStudentHttpUser)
              .put(Authenticated.class, HttpUserService::getAuthenticatedHttpUser)
              .put(Anonymous.class, HttpUserService::getAnonymousHttpUser)
              .build();

  private final Database db;

  public HttpUserArgumentResolver(Database db) {
    this.db = db;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    if (parameter.getParameterType() != HttpUser.class) {
      return false;
    }

    int count = 0;
    for (Annotation annotation : parameter.getParameterAnnotations()) {
      if (HTTP_USER_ANNOTATIONS_MAP.containsKey(annotation.annotationType())) {
        if (++count > 1) {
          throw new IllegalArgumentException("Only one HttpUser annotation may be used.");
        }
      }
    }
    if (count == 0) {
      throw new IllegalArgumentException("HttpUser must have an annotation.");
    }

    return true;
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    TriFunction<Database, HttpServletRequest, HttpServletResponse, HttpUser> fn;
    for (Annotation annotation : List.of(parameter.getParameterAnnotations())) {
      if ((fn = HTTP_USER_ANNOTATIONS_MAP.get(annotation.annotationType())) != null) {
        return fn.apply(
            db,
            webRequest.getNativeRequest(HttpServletRequest.class),
            webRequest.getNativeResponse(HttpServletResponse.class));
      }
    }
    throw new UnsupportedOperationException("Could not find HttpUser for " + parameter);
  }
}
