package org.davincischools.leo.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

@Component
public class ApplicationExceptionConsoleLogger extends AbstractHandlerExceptionResolver {

  private static final Logger logger = LogManager.getLogger();

  @Override
  protected ModelAndView doResolveException(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    logger.atError().withThrowable(ex).log("Internal server error.");
    return null;
  }
}
