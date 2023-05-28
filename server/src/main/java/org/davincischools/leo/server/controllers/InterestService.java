package org.davincischools.leo.server.controllers;

import java.time.Instant;
import java.util.Optional;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.interest_service.RegisterInterestRequest;
import org.davincischools.leo.protos.interest_service.RegisterInterestResponse;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InterestService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/InterestService/RegisterInterest")
  @ResponseBody
  public RegisterInterestResponse registerInterest(
      @RequestBody Optional<RegisterInterestRequest> optionalRequest) throws LogExecutionError {
    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(RegisterInterestRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = RegisterInterestResponse.newBuilder();

              db.getInterestRepository()
                  .save(
                      new Interest()
                          .setCreationTime(Instant.now())
                          .setFirstName(request.getFirstName())
                          .setLastName(request.getLastName())
                          .setEmailAddress(request.getEmailAddress())
                          .setProfession(request.getProfession())
                          .setReasonForInterest(request.getReasonForInterest())
                          .setDistrictName(request.getDistrictName())
                          .setSchoolName(request.getSchoolName())
                          .setAddressLine1(request.getAddressLine1())
                          .setAddressLine2(request.getAddressLine2())
                          .setCity(request.getCity())
                          .setState(request.getState())
                          .setZipCode(request.getZipCode())
                          .setNumTeachers(request.getNumTeachers())
                          .setNumStudents(request.getNumStudents()));

              return response.build();
            })
        .finish();
  }
}
