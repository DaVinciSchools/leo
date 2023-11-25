package org.davincischools.leo.server.utils.task_queue;

public class TaskMetadata<M extends TaskMetadata<M>> {
  int retries;

  @SuppressWarnings("unchecked")
  public M setRetries(int retries) {
    this.retries = retries;
    return (M) this;
  }
}
