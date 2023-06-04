package org.davincischools.leo.server.utils.http_executor;

import java.io.IOException;
import java.io.Serial;
import javax.annotation.Nullable;
import org.davincischools.leo.server.utils.http_executor.HttpExecutor.Error;

public class HttpExecutorException extends IOException {

  @Serial private static final long serialVersionUID = 1164992147965935071L;

  private final transient Error<?> error;

  public HttpExecutorException(Error<?> error) {
    super(error.throwables().isEmpty() ? null : error.throwables().get(0));
    this.error = error;
  }

  @Nullable
  Error<?> getError() {
    return error;
  }
}
