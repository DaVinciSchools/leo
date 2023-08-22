package org.davincischools.leo.server.utils.http_user_x;

import com.google.common.collect.ImmutableMap;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import org.davincischools.leo.database.utils.Database;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HttpUserXArgumentResolver implements HandlerMethodArgumentResolver {

  private interface HttpUserXCreator {
    HttpUserX createHttpUserX(
        Database db,
        HttpServletRequest request,
        HttpServletResponse response,
        EntityManager entityManager);
  }

  private static final ImmutableMap<Class<? extends Annotation>, HttpUserXCreator>
      HTTP_USER_ANNOTATIONS_MAP =
          ImmutableMap.<Class<? extends Annotation>, HttpUserXCreator>builderWithExpectedSize(5)
              .put(AdminX.class, HttpUserXService::getAdminXHttpUserX)
              .put(Teacher.class, HttpUserXService::getTeacherHttpUserX)
              .put(Student.class, HttpUserXService::getStudentHttpUserX)
              .put(Authenticated.class, HttpUserXService::getAuthenticatedHttpUserX)
              .put(Anonymous.class, HttpUserXService::getAnonymousHttpUserX)
              .build();

  private final Database db;
  private final EntityManager entityManager;

  public HttpUserXArgumentResolver(Database db, EntityManager entityManager) {
    this.db = db;
    this.entityManager = entityManager;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    if (parameter.getParameterType() != HttpUserX.class) {
      return false;
    }

    int count = 0;
    for (Annotation annotation : parameter.getParameterAnnotations()) {
      if (HTTP_USER_ANNOTATIONS_MAP.containsKey(annotation.annotationType())) {
        if (++count > 1) {
          throw new IllegalArgumentException("Only one HttpUserX annotation may be used.");
        }
      }
    }
    if (count == 0) {
      throw new IllegalArgumentException("HttpUserX must have an annotation.");
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
    HttpUserXCreator fn;
    for (Annotation annotation : List.of(parameter.getParameterAnnotations())) {
      if ((fn = HTTP_USER_ANNOTATIONS_MAP.get(annotation.annotationType())) != null) {
        return fn.createHttpUserX(
            db,
            webRequest.getNativeRequest(HttpServletRequest.class),
            webRequest.getNativeResponse(HttpServletResponse.class),
            entityManager);
      }
    }
    throw new UnsupportedOperationException("Could not find HttpUserX for " + parameter);
  }
}
