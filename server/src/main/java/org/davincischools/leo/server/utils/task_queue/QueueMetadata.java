package org.davincischools.leo.server.utils.task_queue;

import com.google.protobuf.Message;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "set", toBuilder = true, access = AccessLevel.PACKAGE)
public final class QueueMetadata<T extends Message, M extends TaskMetadata<M>> {
  String name;
  TaskQueue<T, M> taskQueue;
  int processingTasks; // Updated on request.
  int pendingTasks; // Updated on request.
  int processedTasks;
  int skippedTasks;
  int submittedTasks;
  int retries;
  int failures;
  int errors;
  long totalProcessingTimeMs;
  long totalProcessingTimeCount;
  long totalFailedProcessingTimeMs;
  long totalFailedProcessingTimeCount;
  @Nullable String lastFailure = null;
}
