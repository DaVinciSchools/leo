package org.davincischools.leo.server.utils.http_executor;

import javax.annotation.CheckReturnValue;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.utils.http_user.Anonymous;
import org.davincischools.leo.server.utils.http_user.HttpUser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpExecutors {

  Database db;
  HttpUser user;

  public HttpExecutors(Database db, @Anonymous HttpUser user) {
    this.db = db;
    this.user = user;
  }

  @CheckReturnValue
  public <T> HttpExecutor<T, T> start(T input) {
    StackWalker.StackFrame callerFrame =
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(fnStream -> fnStream.skip(1).findFirst().orElse(null));

    return new LoggingHttpExecutor<>(db, user, callerFrame.toStackTraceElement().toString(), input);
  }
}
