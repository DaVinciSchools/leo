package org.davincischools.leo.server.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.davincischools.leo.database.daos.Log;
import org.davincischools.leo.database.daos.LogReference;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.utils.Database;

public class LogUtils {

  private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
  private static final String ADDITIONAL_ERROR_MESSAGE =
      "While processing that error an additional error occurred:\n";
  private static final Joiner ADDITIONAL_ERROR_JOINER =
      Joiner.on("\n\n" + ADDITIONAL_ERROR_MESSAGE);
  private static final Joiner EOL_JOINER = Joiner.on("\n");

  public enum Status {
    ERROR,
    SUCCESS
  }

  public record Error<R>(
      R originalRequest, Object lastInput, ImmutableList<Throwable> throwables) {}

  public static class LogExecutionError extends IOException {

    @Serial private static final long serialVersionUID = 1164992147965935071L;

    private final transient Error<?> error;

    public LogExecutionError(Error<?> error) {
      super(error.throwables.get(0));
      this.error = error;
    }

    @Nullable
    Error<?> getError() {
      return error;
    }
  }

  public interface LogOperations {

    Log getLogEntry();

    void setOnlyLogOnFailure(boolean onlyLogOnFailure);

    void addNote(String pattern, Object... args);

    void addProjectInput(ProjectInput projectInput);

    void addProject(Project project);

    void setStatus(Status status);
  }

  public interface InputConsumer<I, O> {

    O accept(I input, LogOperations log) throws Throwable;
  }

  public interface ErrorConsumer<R> {

    void accept(Error<R> error, LogOperations log) throws Throwable;
  }

  public interface SaveErrorConsumer<R> {

    void accept(Error<R> error, LogOperations log);
  }

  public static class Logger<R, I> implements LogOperations {
    final Database db;
    final R originalRequest;
    final Log logEntry;
    final List<LogReference> logReferenceEntries;
    boolean onlyLogOnFailure;
    Object lastSuccessfulInput;
    Instant lastSuccessfulInputTime;
    Object input;
    int retries = 0;
    int retriesWithinMilliseconds = 0;
    boolean firstTime = true;

    /**
     * True if we've handled the error with an onError(). It causes any future functionality to be
     * skipped until the final writes the log entry before it returns from finish().
     */
    boolean skipToFinish = false;

    final List<Throwable> throwables = new ArrayList<>();

    public Logger(Database db, R originalRequest, Log logEntry) {
      this.db = checkNotNull(db);
      this.originalRequest = checkNotNull(originalRequest);
      this.logEntry = checkNotNull(logEntry);
      this.logReferenceEntries = new ArrayList<>();
      this.onlyLogOnFailure = false;
      this.lastSuccessfulInput = originalRequest;
      this.lastSuccessfulInputTime = Instant.now();
      this.input = originalRequest;
    }

    @Override
    public Log getLogEntry() {
      return logEntry;
    }

    @Override
    public void setOnlyLogOnFailure(boolean onlyLogOnFailure) {
      this.onlyLogOnFailure = onlyLogOnFailure;
    }

    @Override
    public void addNote(String pattern, Object... args) {
      logEntry.setNotes(
          Optional.ofNullable(logEntry.getNotes()).orElse("")
              + String.format(pattern, args)
              + "\n");
    }

    @Override
    public void addProjectInput(ProjectInput projectInput) {
      logReferenceEntries.add(
          new LogReference()
              .setCreationTime(Instant.now())
              .setLog(logEntry)
              .setProjectInput(projectInput));
    }

    @Override
    public void addProject(Project project) {
      logReferenceEntries.add(
          new LogReference().setCreationTime(Instant.now()).setLog(logEntry).setProject(project));
    }

    @Override
    public void setStatus(Status status) {
      logEntry.setStatus(status.name());
    }

    @CheckReturnValue
    public Logger<R, I> retryNextStep(int times, int withinMilliseconds) {
      retries = times;
      retriesWithinMilliseconds = withinMilliseconds;
      return this;
    }

    @CheckReturnValue
    @SuppressWarnings("unchecked")
    public <O> Logger<R, O> andThen(InputConsumer<I, O> inputConsumer) {
      if (!skipToFinish && throwables.isEmpty()) {
        lastSuccessfulInput = input;
        lastSuccessfulInputTime = Instant.now();
        Instant startTime = Instant.now();
        for (int retry = 0; retry <= retries; ++retry) {
          try {
            input = inputConsumer.accept((I) lastSuccessfulInput, this);

            if (firstTime) {
              firstTime = false;
              if (logEntry.getInitialResponseTime() == null) {
                logEntry.setInitialResponseTime(Instant.now());
              }
              if (input != null) {
                if (logEntry.getInitialResponseType() == null) {
                  logEntry.setInitialResponseType(input.getClass().getName());
                }
                if (logEntry.getInitialResponse() == null) {
                  logEntry.setInitialResponse(ioToString(input));
                }
              }
            }
            break;
          } catch (Throwable t) {
            if (retry > retries
                || Instant.now().isAfter(startTime.plusMillis(retriesWithinMilliseconds))) {
              input = null;
              throwables.add(t);
            } else {
              logger.atWarn().withThrowable(t).log("Retrying after error: {}", retry);
              try {
                Thread.sleep(250);
              } catch (Exception ignored) {
              }
            }
          }
        }
      }

      retries = 0;
      retriesWithinMilliseconds = 0;
      return (Logger<R, O>) this;
    }

    @CheckReturnValue
    public Logger<R, I> onError(ErrorConsumer<R> errorConsumer) {
      if (!skipToFinish && !throwables.isEmpty()) {
        try {
          skipToFinish = true;
          errorConsumer.accept(
              new Error<>(originalRequest, lastSuccessfulInput, ImmutableList.copyOf(throwables)),
              this);
        } catch (Throwable t) {
          throwables.add(t);
        }
      }
      return this;
    }

    @CheckReturnValue
    @SuppressWarnings("unchecked")
    public I finish(ErrorConsumer<R> errorConsumer) throws LogExecutionError {
      try {
        if (logEntry.getFinalResponseTime() == null) {
          logEntry.setFinalResponseTime(Instant.now());
        }
        if (throwables.isEmpty()) {
          if (input != null) {
            if (logEntry.getFinalResponseType() == null) {
              logEntry.setFinalResponseType(input.getClass().getName());
            }
            if (logEntry.getFinalResponse() == null) {
              logEntry.setFinalResponse(ioToString(input));
            }
          }

          if (Strings.isNullOrEmpty(logEntry.getStatus())) {
            logEntry.setStatus(Status.SUCCESS.name());
          }

          if (!onlyLogOnFailure) {
            db.getLogRepository().save(logEntry);
            db.getLogReferenceRepository().saveAll(logReferenceEntries);
          }

          return (I) input;
        } else {
          logEntry.setLastInputTime(lastSuccessfulInputTime);
          if (lastSuccessfulInput != null) {
            logEntry.setLastInputType(lastSuccessfulInput.getClass().getName());
            logEntry.setLastInput(ioToString(lastSuccessfulInput));
          }
          logEntry.setStatus(Status.ERROR.name());

          errorConsumer.accept(
              new Error<>(originalRequest, lastSuccessfulInput, ImmutableList.copyOf(throwables)),
              this);
        }
      } catch (Throwable t) {
        throwables.add(t);
      }

      logEntry.setStackTrace(
          ADDITIONAL_ERROR_JOINER.join(
              Lists.transform(throwables, Throwables::getStackTraceAsString)));
      try {
        db.getLogRepository().save(logEntry);
        db.getLogReferenceRepository().saveAll(logReferenceEntries);
      } catch (Throwable t) {
        throwables.add(t);
        logEntry.setStackTrace(
            ADDITIONAL_ERROR_JOINER.join(
                Lists.transform(throwables, Throwables::getStackTraceAsString)));
      }

      logger
          .atError()
          .withThrowable(throwables.get(0))
          .log("An error occurred while processing request: {}", ioToString(originalRequest));
      if (throwables.size() >= 2) {
        logger
            .atError()
            .log(
                "{}{}",
                ADDITIONAL_ERROR_MESSAGE,
                ADDITIONAL_ERROR_JOINER.join(
                    Lists.transform(
                        throwables.subList(1, throwables.size()),
                        Throwables::getStackTraceAsString)));
      }
      throw new LogExecutionError(
          new Error<>(originalRequest, lastSuccessfulInput, ImmutableList.copyOf(throwables)));
    }

    public I finish() throws LogExecutionError {
      return finish((error, throwable) -> {});
    }
  }

  @CheckReturnValue
  public static <I> Logger<I, I> executeAndLog(Database db, I request) {
    checkNotNull(db);
    checkNotNull(request);

    Log logEntry = new Log().setCreationTime(Instant.now());

    HttpUserProvider.getUser().ifPresent(logEntry::setUserX);

    StackWalker.StackFrame callerFrame =
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(fnStream -> fnStream.skip(1).findFirst().orElse(null));
    if (callerFrame != null) {
      logEntry.setCaller(callerFrame.toStackTraceElement().toString());
    }
    logEntry
        .setRequest(ioToString(request))
        .setRequestType(request.getClass().getName())
        .setRequestTime(Instant.now());

    return new Logger<>(db, request, logEntry);
  }

  @CheckReturnValue
  public static <I, O> Logger<I, O> executeAndLog(
      Database db, I request, InputConsumer<I, O> inputConsumer) {
    return executeAndLog(db, request).andThen(inputConsumer);
  }

  @Nullable
  private static String ioToString(@Nullable Object o) {
    // TODO: Make this Annotation based so that we can add more types.
    if (o == null) {
      return "null";
    } else if (o instanceof Message) {
      return TextFormat.printer().printToString((Message) o);
    } else if (o instanceof HttpServletRequest) {
      HttpServletRequest r = (HttpServletRequest) o;
      Enumeration<String> e1 = r.getHeaderNames();
      return EOL_JOINER.join(
          ImmutableList.builder()
              .add("URI: " + r.getRequestURI())
              .addAll(
                  stream(e1)
                      .limit(150)
                      .sorted()
                      .map(
                          name -> {
                            Enumeration<String> e = r.getHeaders(name);
                            return "HEADER: " + name + ": " + stream(e).sorted().toList();
                          })
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
                      .map(name -> "HEADER: " + name + ": " + r.getHeaders(name))
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

  private static <T> Stream<T> stream(Enumeration<T> enumeration) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(enumeration.asIterator(), 0), false);
  }
}
