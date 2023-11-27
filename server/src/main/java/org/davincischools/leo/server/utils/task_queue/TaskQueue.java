package org.davincischools.leo.server.utils.task_queue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.Message;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;
import javax.annotation.concurrent.GuardedBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.internal.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class TaskQueue<T extends Message, M extends TaskMetadata<M>> {

  public static final String AUTO_SCAN_FOR_TASKS_PROP_NAME =
      "project_leo.tasks.auto_scan_for_tasks";

  private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+", Pattern.DOTALL);

  private static final Object LOCK = new Object();
  private static final Logger logger = LogManager.getLogger();

  @GuardedBy("LOCK")
  private static final List<TaskQueue<?, ?>> taskQueues = new ArrayList<>();

  @GuardedBy("LOCK")
  private final QueueMetadata<T, M> queueMetadata = QueueMetadata.<T, M>builder().build();

  @GuardedBy("LOCK")
  private final LinkedHashMap<T, M> pendingTasks = new LinkedHashMap<>();

  @GuardedBy("LOCK")
  private final Set<T> processingTasks = new HashSet<>();

  private final int numThreads;
  private final Semaphore semaphore;
  private boolean autoScanForTasks = false;
  private final ExecutorService executorService;

  protected TaskQueue(int numThreads) {
    this.numThreads = numThreads;
    semaphore = new Semaphore(numThreads);
    executorService =
        Executors.newFixedThreadPool(
            numThreads,
            new ThreadFactoryBuilder()
                .setNameFormat(getClass().getSimpleName() + "Thread-%d")
                .setDaemon(true)
                .build());

    queueMetadata.name = this.getClass().getSimpleName();
    queueMetadata.taskQueue = this;

    synchronized (LOCK) {
      taskQueues.add(this);
    }

    new ThreadFactoryBuilder()
        .setNameFormat(getClass().getSimpleName() + "ProcessThread-%d")
        .setDaemon(true)
        .build()
        .newThread(this::processTasks)
        .start();
  }

  @Autowired
  public final void initializeAutowiredProperties(Environment environment) {
    autoScanForTasks = environment.getProperty(AUTO_SCAN_FOR_TASKS_PROP_NAME, Boolean.class, false);
  }

  @PostConstruct
  public void autoRescan() {
    rescanForTasks(true);
  }

  public void rescanForTasks(boolean isAutoRescan) {
    if (isAutoRescan && !autoScanForTasks) {
      logger.atInfo().log("Skipping rescan for tasks: {}", getClass().getSimpleName());
      return;
    }

    new ThreadFactoryBuilder()
        .setNameFormat(getClass().getSimpleName() + "ScanThread-%d")
        .setDaemon(true)
        .build()
        .newThread(this::scanForTasks)
        .start();
  }

  protected void scanForTasks() {}

  protected abstract M createDefaultMetadata();

  protected abstract void processTask(T task, M metadata) throws Throwable;

  protected void taskFailed(T task, M metadata, Throwable t) throws Throwable {}

  public final void submitTask(T task, M metadata) {
    checkNotNull(task);
    checkNotNull(metadata);

    synchronized (LOCK) {
      queueMetadata.submittedTasks++;
      M oldMetadata = pendingTasks.put(task, metadata);
      if (oldMetadata != null) {
        queueMetadata.skippedTasks++;
      }
      LOCK.notifyAll();
    }
  }

  public final void submitTask(T task) {
    checkNotNull(task);

    submitTask(task, createDefaultMetadata());
  }

  @SuppressWarnings("InfiniteLoopStatement")
  private void processTasks() {
    NEXT_TASK:
    while (true) {
      try {
        // Get next task.
        T task;
        M taskMetadata;
        synchronized (LOCK) {
          // Pull out a task that is not being processed already.
          Map.Entry<T, M> entry;
          var i = pendingTasks.entrySet().iterator();
          do {
            if (!i.hasNext()) {
              try {
                LOCK.wait();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              continue NEXT_TASK;
            }
            entry = i.next();
          } while (processingTasks.contains(entry.getKey()));

          task = entry.getKey();
          taskMetadata = entry.getValue();

          i.remove();
          processingTasks.add(task);
        }

        // Wait for an available thread.
        while (true) {
          try {
            semaphore.acquire();
            break;
          } catch (InterruptedException ignored) {
          }
        }

        // Process task.
        executorService.submit(
            () -> {
              long startTimeMs = System.currentTimeMillis();
              try {
                processTask(task, taskMetadata);
                synchronized (LOCK) {
                  queueMetadata.totalProcessingTimeMs += System.currentTimeMillis() - startTimeMs;
                  queueMetadata.totalProcessingTimeCount++;
                }
              } catch (Throwable t) {
                synchronized (LOCK) {
                  queueMetadata.totalFailedProcessingTimeMs +=
                      System.currentTimeMillis() - startTimeMs;
                  queueMetadata.totalFailedProcessingTimeCount++;
                  queueMetadata.failures++;
                  queueMetadata.lastFailure =
                      Throwables.getStacktrace(t)
                          + System.lineSeparator()
                          + System.lineSeparator()
                          + TaskQueue.toCompressedString(task);
                  if (taskMetadata.retries-- > 0) {
                    queueMetadata.retries++;
                    submitTask(task, taskMetadata);
                    return;
                  }
                }
                try {
                  taskFailed(task, taskMetadata, t);
                } catch (Throwable t2) {
                  synchronized (LOCK) {
                    queueMetadata.errors++;
                    queueMetadata.lastFailure =
                        Throwables.getStacktrace(t).trim()
                            + System.lineSeparator()
                            + System.lineSeparator()
                            + TaskQueue.toCompressedString(task)
                            + System.lineSeparator()
                            + System.lineSeparator()
                            + Throwables.getStacktrace(t2).trim();
                  }
                }
              } finally {
                semaphore.release();
                synchronized (LOCK) {
                  queueMetadata.processedTasks++;
                  processingTasks.remove(task);
                  LOCK.notifyAll();
                }
              }
            });
      } catch (Throwable t) {
        logger.atError().withThrowable(t).log("Processing tasks failed.");
        synchronized (LOCK) {
          queueMetadata.errors++;
          queueMetadata.lastFailure = Throwables.getStacktrace(t).trim();
          LOCK.notifyAll();
        }
      }
    }
  }

  public static List<? extends QueueMetadata<?, ?>> getTaskQueueMetadata() {
    synchronized (LOCK) {
      return taskQueues.stream()
          .map(
              t -> {
                // Account for tasks that are waiting for a thread.
                int waiting = Math.max(0, t.processingTasks.size() - t.numThreads);
                return t.queueMetadata.toBuilder()
                    .processingTasks(t.processingTasks.size() - waiting)
                    .pendingTasks(t.pendingTasks.size() + waiting)
                    .build();
              })
          .toList();
    }
  }

  protected static String toCompressedString(Message m) {
    return WHITESPACE_PATTERN.matcher(m.toString()).replaceAll(" ").trim();
  }
}
