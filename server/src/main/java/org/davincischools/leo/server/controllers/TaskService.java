package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.protos.task_service.GetTaskQueuesStatusRequest;
import org.davincischools.leo.protos.task_service.GetTaskQueuesStatusResponse;
import org.davincischools.leo.protos.task_service.ResetTaskQueuesRequest;
import org.davincischools.leo.protos.task_service.ResetTaskQueuesResponse;
import org.davincischools.leo.protos.task_service.ScanForTasksRequest;
import org.davincischools.leo.protos.task_service.ScanForTasksResponse;
import org.davincischools.leo.protos.task_service.TaskQueueStatus;
import org.davincischools.leo.server.utils.ProtoDaoUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaskService {

  @PostMapping(value = "/api/protos/TaskService/GetTaskQueuesStatus")
  @ResponseBody
  public GetTaskQueuesStatusResponse getTaskQueuesStatus(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetTaskQueuesStatusRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = GetTaskQueuesStatusResponse.newBuilder();
    return httpExecutors
        .start(optionalRequest.orElse(GetTaskQueuesStatusRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (!userX.isAdminX()) {
                return userX.returnForbidden(GetTaskQueuesStatusResponse.getDefaultInstance());
              }

              TaskQueue.getTaskQueueMetadata()
                  .forEach(
                      m ->
                          ProtoDaoUtils.translateToProto(
                              m,
                              response::addTaskQueueStatusesBuilder,
                              s -> {
                                s.setProcessingTimeMs(
                                    m.getTotalProcessingTimeCount() == 0
                                        ? 0
                                        : m.getTotalProcessingTimeMs()
                                            / m.getTotalProcessingTimeCount());
                                s.setFailedProcessingTimeMs(
                                    m.getTotalFailedProcessingTimeCount() == 0
                                        ? 0
                                        : m.getTotalFailedProcessingTimeMs()
                                            / m.getTotalFailedProcessingTimeCount());
                              },
                              TaskQueueStatus.PROCESSING_TIME_MS_FIELD_NUMBER,
                              TaskQueueStatus.FAILED_PROCESSING_TIME_MS_FIELD_NUMBER));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/TaskService/ScanForTasks")
  @ResponseBody
  public ScanForTasksResponse scanForTasks(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<ScanForTasksRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(ScanForTasksRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (!userX.isAdminX()) {
                return userX.returnForbidden(ScanForTasksResponse.getDefaultInstance());
              }

              TaskQueue.getTaskQueueMetadata()
                  .forEach(
                      q -> {
                        if (request.hasName()) {
                          if (request.getName().equals(q.getName())) {
                            q.getTaskQueue().rescanForTasks(false);
                          }
                        } else {
                          q.getTaskQueue().rescanForTasks(false);
                        }
                      });

              return ScanForTasksResponse.getDefaultInstance();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/TaskService/ResetTaskQueues")
  @ResponseBody
  public ResetTaskQueuesResponse resetTaskQueues(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<ResetTaskQueuesRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(ResetTaskQueuesRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (!userX.isAdminX()) {
                return userX.returnForbidden(ResetTaskQueuesResponse.getDefaultInstance());
              }

              TaskQueue.getTaskQueueMetadata()
                  .forEach(
                      q -> {
                        if (request.hasName()) {
                          if (request.getName().equals(q.getName())) {
                            q.getTaskQueue().resetTaskQueues();
                            ;
                          }
                        } else {
                          q.getTaskQueue().resetTaskQueues();
                        }
                      });

              return ResetTaskQueuesResponse.getDefaultInstance();
            })
        .finish();
  }
}
