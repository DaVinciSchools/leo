package org.davincischools.leo.server.utils.http_executor;

import jakarta.servlet.http.HttpServletResponse;
import javax.annotation.CheckReturnValue;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.utils.http_user_x.Anonymous;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpExecutors {

  Database db;
  HttpUserX userX;
  HttpServletResponse response;

  public HttpExecutors(Database db, @Anonymous HttpUserX userX, HttpServletResponse response) {
    this.db = db;
    this.userX = userX;
    this.response = response;
  }

  @CheckReturnValue
  public <T> HttpExecutor<T, T> start(T input) {
    StackWalker.StackFrame callerFrame =
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(fnStream -> fnStream.skip(1).findFirst().orElse(null));

    return new LoggingHttpExecutor<>(
        db, userX, response, callerFrame.toStackTraceElement().toString(), input);
  }
}
