package org.davincischools.leo.server.utils.task_queue.workers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.task_service.ReplyToPostTask;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ReplyToPostsWorker extends TaskQueue<ReplyToPostTask, DefaultTaskMetadata> {

  private static final Logger logger = LogManager.getLogger();

  private final Database db;

  public ReplyToPostsWorker(@Autowired Database db) {
    super(10);
    this.db = db;
  }

  @Override
  protected DefaultTaskMetadata createDefaultMetadata() {
    return new DefaultTaskMetadata().setRetries(2);
  }

  @Override
  protected void scanForTasks() {
    db.getProjectPostRepository()
        .getAllIds()
        .forEach(id -> ReplyToPostTask.newBuilder().setProjectPostId(id).build());
  }

  @Override
  protected boolean processTask(ReplyToPostTask task, DefaultTaskMetadata metadata) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // Do nothing.
    }
    return false;
  }
}
