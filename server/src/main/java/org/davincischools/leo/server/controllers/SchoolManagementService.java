package org.davincischools.leo.server.controllers;

import java.time.Instant;
import java.util.Optional;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.school_management.GetSchoolsRequest;
import org.davincischools.leo.protos.school_management.RemoveSchoolRequest;
import org.davincischools.leo.protos.school_management.SchoolInformationResponse;
import org.davincischools.leo.protos.school_management.UpsertSchoolRequest;
import org.davincischools.leo.server.utils.ProtoDaoUtils;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.AdminX;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SchoolManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/SchoolManagementService/GetSchools")
  @ResponseBody
  public SchoolInformationResponse getSchools(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetSchoolsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(SchoolInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetSchoolsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              return getAllSchools(request.getDistrictId(), -1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/SchoolManagementService/UpsertSchool")
  @ResponseBody
  public SchoolInformationResponse upsertSchool(
      @AdminX HttpUserX userX,
      @RequestBody Optional<UpsertSchoolRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(SchoolInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpsertSchoolRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              School school =
                  new School()
                      .setCreationTime(Instant.now())
                      .setDistrict(
                          db.getDistrictRepository()
                              .findById(request.getSchool().getDistrict().getId())
                              .orElseThrow())
                      .setId(request.getSchool().hasId() ? request.getSchool().getId() : null)
                      .setName(request.getSchool().getName())
                      .setAddress(request.getSchool().getAddress());
              db.getSchoolRepository().save(school);

              return getAllSchools(school.getDistrict().getId(), school.getId());
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/SchoolManagementService/RemoveSchool")
  @ResponseBody
  public SchoolInformationResponse removeSchool(
      @AdminX HttpUserX userX,
      @RequestBody Optional<RemoveSchoolRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(SchoolInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(RemoveSchoolRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              db.getSchoolRepository().deleteById(request.getSchoolId());
              return getAllSchools(request.getDistrictId(), -1);
            })
        .finish();
  }

  private SchoolInformationResponse getAllSchools(int districtId, int nextSchoolId) {
    SchoolInformationResponse.Builder response = SchoolInformationResponse.newBuilder();
    response.setDistrictId(districtId);
    response.setNextSchoolId(nextSchoolId);
    response.addAllSchools(
        db.getSchoolRepository().findAllByDistrictId(districtId).stream()
            .map(s -> ProtoDaoUtils.toSchoolProto(s, null).build())
            .toList());
    return response.build();
  }
}
