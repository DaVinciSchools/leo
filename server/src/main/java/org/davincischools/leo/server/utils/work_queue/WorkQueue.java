package org.davincischools.leo.server.utils.work_queue;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.protobuf.Message;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class WorkQueue {

  private static final Logger logger = LogManager.getLogger();

  private record Work<T extends Message>(T payload, int retriesLeft) {}

  private final Multimap<Worker<?>, Work<?>> workQueue =
      TreeMultimap.create(
          Comparator.comparing(k -> k.getClass().getName()),
          Comparator.comparing(w -> w.payload().toString()));

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

  private WorkQueue() {
    new Thread(
            () -> {
              while (true) {
                try {
                  doWork();
                  synchronized (workQueue) {
                    if (workQueue.isEmpty()) {
                      workQueue.wait();
                    }
                  }
                } catch (InterruptedException e) {
                  // More work is probably available.
                }
              }
            })
        .start();
  }

  public <T extends Message> void submitWork(Worker<T> worker, T payload, int retries) {
    logger.atInfo().log("Submitting work: {}: \"{}\"", worker.getClass(), payload);
    internalSubmitWork(worker, payload, retries);
  }

  private <T extends Message> void internalSubmitWork(
      Worker<T> worker, T payload, int retriesLeft) {
    synchronized (workQueue) {
      workQueue.put(worker, new Work<T>(payload, retriesLeft));
      workQueue.notifyAll();
    }
  }

  @SuppressWarnings("unchecked")
  private void doWork() {
    while (true) {
      Worker<Message> worker;
      Work<Message> work;

      synchronized (workQueue) {
        var i = workQueue.entries().iterator();
        if (!i.hasNext()) {
          break;
        }
        var entry = i.next();
        i.remove();

        worker = (Worker<Message>) entry.getKey();
        work = (Work<Message>) entry.getValue();
      }

      executorService.submit(
          () -> {
            try {
              worker.process(work.payload());
            } catch (Exception e) {
              try {
                Thread.sleep(1000);
              } catch (Exception e2) {
                // Ignore.
              }
              if (work.retriesLeft() <= 0) {
                logger
                    .atError()
                    .withThrowable(e)
                    .log("Work processing failure: {}: \"{}\"", worker.getClass(), work.payload());
                try {
                  worker.failed(work.payload());
                } catch (Exception e2) {
                  logger
                      .atError()
                      .withThrowable(e2)
                      .log(
                          "Work processing failure exception: {}: \"{}\"",
                          worker.getClass(),
                          work.payload());
                }
                return;
              }
              internalSubmitWork(worker, work.payload(), work.retriesLeft() - 1);
            }
          });
    }

    synchronized (workQueue) {
      if (!workQueue.isEmpty()) {
        workQueue.notifyAll();
      }
    }
  }
}
