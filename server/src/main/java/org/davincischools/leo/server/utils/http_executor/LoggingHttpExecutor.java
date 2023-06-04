package org.davincischools.leo.server.utils.http_executor;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.davincischools.leo.database.daos.Log;
import org.davincischools.leo.database.daos.LogReference;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.LogRepository;
import org.davincischools.leo.server.utils.http_user.HttpUser;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional(TxType.NOT_SUPPORTED)
public class LoggingHttpExecutor<R, I> implements HttpExecutor<R, I>, HttpExecutorLog {

  private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
  private static final String ADDITIONAL_ERROR_MESSAGE =
      "While processing that error an additional error occurred:\n";
  private static final Joiner ADDITIONAL_ERROR_JOINER =
      Joiner.on("\n\n" + ADDITIONAL_ERROR_MESSAGE);
  private static final Joiner EOL_JOINER = Joiner.on("\n");

  final Database db;
  final Log log;
  final List<LogReference> logReferences = new ArrayList<>();
  final R originalRequest;

  boolean onlyLogOnFailure = false;

  int retries = 0;
  int retriesWithinMilliseconds = 0;

  Object lastSuccessfulInput;
  Instant lastSuccessfulInputTime;

  /**
   * True if we've handled the error with an onError(). It causes any future functionality to be
   * skipped until the final writes the log entry before it returns from finish().
   */
  boolean skipToFinish = false;

  final List<Throwable> throwables = new ArrayList<>();

  LoggingHttpExecutor(@Autowired Database db, @Autowired HttpUser user, String caller, R input) {
    this.db = checkNotNull(db);
    this.log = new Log().setCreationTime(Instant.now());
    if (user.isPresent()) {
      log.setUserX(user.get());
    }

    originalRequest = input;
    lastSuccessfulInput = input;
    lastSuccessfulInputTime = Instant.now();

    log.setCaller(caller);
    log.setRequestTime(Instant.now());
    log.setRequestType(input.getClass().getName());
    log.setRequest(ioToString(input));
  }

  @Override
  public HttpExecutorLog addNote(String pattern, Object... args) {
    log.setNotes(
        Optional.ofNullable(log.getNotes()).orElse("") + String.format(pattern, args) + "\n");
    return this;
  }

  @Override
  public HttpExecutorLog addProjectInput(ProjectInput projectInput) {
    logReferences.add(
        new LogReference()
            .setCreationTime(Instant.now())
            .setLog(log)
            .setProjectInput(projectInput));
    return this;
  }

  @Override
  public HttpExecutorLog addProject(Project project) {
    logReferences.add(
        new LogReference().setCreationTime(Instant.now()).setLog(log).setProject(project));
    return this;
  }

  @Override
  public HttpExecutorLog setStatus(LogRepository.Status status) {
    log.setStatus(status.name());
    return this;
  }

  @Override
  @CheckReturnValue
  public HttpExecutor<R, I> setOnlyLogOnFailure(boolean onlyLogOnFailure) {
    this.onlyLogOnFailure = onlyLogOnFailure;
    return this;
  }

  @Override
  @CheckReturnValue
  public HttpExecutor<R, I> retryNextStep(int times, int withinMilliseconds) {
    retries = times;
    retriesWithinMilliseconds = withinMilliseconds;
    return this;
  }

  @Override
  @CheckReturnValue
  public HttpExecutor<R, I> logInitialResponse() {
    if (lastSuccessfulInput != null) {
      log.setInitialResponseType(lastSuccessfulInput.getClass().getName());
      log.setInitialResponse(ioToString(lastSuccessfulInput));
    } else {
      log.setInitialResponseType("null");
      log.setInitialResponse(null);
    }
    log.setInitialResponseTime(lastSuccessfulInputTime);

    return this;
  }

  @Override
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public <O> HttpExecutor<R, O> andThen(InputConsumer<I, O> inputConsumer) {
    if (!skipToFinish && throwables.isEmpty()) {
      Instant startTime = Instant.now();
      for (int retry = 0; retry <= retries; ++retry) {
        try {
          lastSuccessfulInput = inputConsumer.accept((I) lastSuccessfulInput, this);
          lastSuccessfulInputTime = Instant.now();
          break;
        } catch (Throwable t) {
          if (retry > retries
              || Instant.now().isAfter(startTime.plusMillis(retriesWithinMilliseconds))) {
            throwables.add(t);
          } else {
            logger.atWarn().withThrowable(t).log("Retrying after error: {}", retry);
            try {
              Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }
          }
        }
      }
    }

    retries = 0;
    retriesWithinMilliseconds = 0;
    return (LoggingHttpExecutor<R, O>) this;
  }

  @Override
  @CheckReturnValue
  public HttpExecutor<R, I> onError(ErrorConsumer<R, I> errorConsumer) {
    if (!skipToFinish && !throwables.isEmpty()) {
      try {
        Optional<I> errorConsumerResult =
            errorConsumer.accept(
                new Error<>(originalRequest, lastSuccessfulInput, ImmutableList.copyOf(throwables)),
                this);
        // If there's a result then continue the chain with it as the last successful input.
        if (errorConsumerResult.isPresent()) {
          lastSuccessfulInput = errorConsumerResult.get();
          lastSuccessfulInputTime = Instant.now();
          throwables.clear();
          return this;
        }
      } catch (Throwable t) {
        throwables.add(t);
      }
    }
    skipToFinish = true;
    return this;
  }

  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public I finish(ErrorConsumer<R, I> errorConsumer) throws HttpExecutorException {
    if (!throwables.isEmpty()) {
      try {
        if (lastSuccessfulInput != null) {
          log.setLastInputType(lastSuccessfulInput.getClass().getName());
        } else {
          log.setLastInputType("null");
        }
        log.setLastInput(ioToString(lastSuccessfulInput));
        log.setLastInputTime(lastSuccessfulInputTime);

        log.setStackTrace(
            ADDITIONAL_ERROR_JOINER.join(
                Lists.transform(throwables, Throwables::getStackTraceAsString)));

        Optional<I> errorConsumerResponse =
            errorConsumer.accept(
                new Error<>(originalRequest, lastSuccessfulInput, ImmutableList.copyOf(throwables)),
                this);

        if (errorConsumerResponse.isPresent()) {
          lastSuccessfulInput = errorConsumerResponse.get();
          lastSuccessfulInputTime = Instant.now();
        } else {
          log.setStatus(LogRepository.Status.ERROR.name());

          db.getLogRepository().save(log);
          db.getLogReferenceRepository().saveAll(logReferences);

          throw new HttpExecutorException(
              new Error<>(originalRequest, lastSuccessfulInput, ImmutableList.copyOf(throwables)));
        }
      } catch (HttpExecutorException e) {
        throw e;
      } catch (Throwable t) {
        throwables.add(t);
      }
    }

    try {
      if (throwables.isEmpty() && onlyLogOnFailure) {
        return (I) lastSuccessfulInput;
      }

      if (lastSuccessfulInput != null) {
        log.setFinalResponseType(lastSuccessfulInput.getClass().getName());
      } else {
        log.setFinalResponseType("null");
      }
      log.setFinalResponse(ioToString(lastSuccessfulInput));
      log.setFinalResponseTime(lastSuccessfulInputTime);

      if (Strings.isNullOrEmpty(log.getStatus())) {
        log.setStatus(LogRepository.Status.SUCCESS.name());
      }

      db.getLogRepository().save(log);
      db.getLogReferenceRepository().saveAll(logReferences);

      return (I) lastSuccessfulInput;
    } catch (Throwable t) {
      throwables.add(t);
    }

    logger
        .atError()
        .withThrowable(throwables.get(0))
        .log("An error occurred while finishing the executor: {}", ioToString(originalRequest));
    throw new HttpExecutorException(
        new Error<>(originalRequest, lastSuccessfulInput, ImmutableList.copyOf(throwables)));
  }

  public I finish() throws HttpExecutorException {
    return finish((error, log) -> Optional.empty());
  }

  @Nullable
  private static String ioToString(@Nullable Object o) {
    // TODO: Make this Annotation based so that we can add more types.
    if (o == null) {
      return null;
    } else if (o instanceof Message) {
      return TextFormat.printer().printToString((Message) o);
    } else if (o instanceof HttpServletRequest) {
      HttpServletRequest r = (HttpServletRequest) o;
      Iterator<String> e1 = r.getHeaderNames().asIterator();
      return EOL_JOINER.join(
          ImmutableList.builder()
              .add("URI: " + r.getRequestURI())
              .addAll(
                  Streams.stream(e1)
                      .sorted()
                      .limit(150)
                      .map(
                          name ->
                              "HEADER: "
                                  + name
                                  + ": "
                                  + Streams.stream(r.getHeaders(name).asIterator())
                                      .sorted()
                                      .toList())
                      .toList())
              .build());
    } else if (o instanceof HttpServletResponse) {
      HttpServletResponse r = (HttpServletResponse) o;
      return EOL_JOINER.join(
          ImmutableList.builder()
              .add("STATUS: " + r.getStatus())
              .add("CONTENT_TYPE: " + r.getContentType())
              .addAll(
                  r.getHeaderNames().stream()
                      .sorted()
                      .limit(150)
                      .map(
                          name ->
                              "HEADER: "
                                  + name
                                  + ": "
                                  + Streams.stream(r.getHeaders(name)).sorted().toList())
                      .toList())
              .build());
    } else if (o instanceof byte[]) {
      String converted = new String((byte[]) o, StandardCharsets.UTF_8);
      if (Arrays.compare((byte[]) o, converted.getBytes(StandardCharsets.UTF_8)) == 0) {
        return converted;
      } else {
        return new String(Base64.getMimeEncoder().encode((byte[]) o), StandardCharsets.US_ASCII);
      }
    } else {
      return o.toString();
    }
  }
}
