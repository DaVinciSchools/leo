package org.davincischools.leo.server.utils.http_executor;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import javax.annotation.CheckReturnValue;

public interface HttpExecutor<R, I> {

  interface InputConsumer<I, O> {

    @CheckReturnValue
    O accept(I input, HttpExecutorLog log) throws Throwable;
  }

  interface ErrorConsumer<R, O> {

    // If it returns a value, the chain is continued with it.
    @CheckReturnValue
    Optional<O> accept(Error<R> error, HttpExecutorLog log) throws Throwable;
  }

  record Error<R>(R originalRequest, Object lastInput, ImmutableList<Throwable> throwables) {}

  @CheckReturnValue
  HttpExecutor<R, I> setOnlyLogOnFailure(boolean onlyLogOnFailure);

  @CheckReturnValue
  HttpExecutor<R, I> logInitialResponse();

  @CheckReturnValue
  HttpExecutor<R, I> retryNextStep(int times, int withinMilliseconds);

  @CheckReturnValue
  <T> HttpExecutor<R, T> andThen(InputConsumer<I, T> inputConsumer);

  @CheckReturnValue
  HttpExecutor<R, I> onError(ErrorConsumer<R, I> errorConsumer);

  @CheckReturnValue
  I finish(ErrorConsumer<R, I> errorConsumer) throws HttpExecutorException;

  @CheckReturnValue
  I finish() throws HttpExecutorException;
}
