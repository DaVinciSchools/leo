package org.davincischools.leo.server.utils.task_queue;

import com.google.protobuf.Message;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true, access = AccessLevel.PACKAGE)
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
  @Nullable String lastFailure = null;
}
