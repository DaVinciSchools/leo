package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.LogRepository.Status;
import org.davincischools.leo.protos.error_service.ReportErrorRequest;
import org.davincischools.leo.protos.error_service.ReportErrorResponse;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/ErrorService/ReportError")
  @ResponseBody
  public ReportErrorResponse ReportError(
      @RequestBody Optional<ReportErrorRequest> optionalRequest, HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(ReportErrorRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = ReportErrorResponse.newBuilder();

              log.setStatus(Status.ERROR);

              return response.build();
            })
        .finish();
  }
}
