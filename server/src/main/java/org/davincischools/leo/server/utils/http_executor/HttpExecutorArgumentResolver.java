package org.davincischools.leo.server.utils.http_executor;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.utils.http_user.HttpUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HttpExecutorArgumentResolver implements HandlerMethodArgumentResolver {

  private final Database db;
  private final EntityManager entityManager;

  public HttpExecutorArgumentResolver(Database db, EntityManager entityManager) {
    this.db = db;
    this.entityManager = entityManager;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType() == HttpExecutors.class;
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {

    return new HttpExecutors(
        db,
        HttpUserService.getAnonymousHttpUser(
            db,
            webRequest.getNativeRequest(HttpServletRequest.class),
            webRequest.getNativeResponse(HttpServletResponse.class),
            entityManager));
  }
}
