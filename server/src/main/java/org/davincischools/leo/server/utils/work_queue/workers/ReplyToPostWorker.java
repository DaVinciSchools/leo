package org.davincischools.leo.server.utils.work_queue.workers;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectPostsParams;
import org.davincischools.leo.protos.worker_queue.ReplyToPostPayload;
import org.davincischools.leo.server.utils.work_queue.WorkQueue;
import org.davincischools.leo.server.utils.work_queue.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReplyToPostWorker implements Worker<ReplyToPostPayload> {

  private static final Logger logger = LogManager.getLogger();

  @Autowired private Database db;
  @Autowired private WorkQueue workQueue;

  @PostConstruct
  public void scanForWork() {
    db.getProjectPostRepository()
        .getProjectPosts(new GetProjectPostsParams().setBeingEdited(false))
        .forEach(
            projectPost ->
                submitWork(
                    ReplyToPostPayload.newBuilder().setProjectPostId(projectPost.getId()).build()));
  }

  @Override
  public void submitWork(ReplyToPostPayload payload) {
    workQueue.submitWork(this, payload, 2);
  }

  @Override
  public void process(ReplyToPostPayload payload) throws Exception {
    logger.atInfo().log("Processing project post {}", payload.getProjectPostId());
    throw new IllegalArgumentException("Not implemented");
  }

  @Override
  public void failed(ReplyToPostPayload payload) {
    logger.atError().log("Failed to process project post {}", payload.getProjectPostId());
  }
}
