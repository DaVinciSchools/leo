package org.davincischools.leo.server.controllers;

import java.time.Instant;
import java.util.Optional;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.school_management.GetSchoolsRequest;
import org.davincischools.leo.protos.school_management.RemoveSchoolRequest;
import org.davincischools.leo.protos.school_management.SchoolInformationResponse;
import org.davincischools.leo.protos.school_management.UpsertSchoolRequest;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.HttpUserProvider.Admin;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
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
      @Admin UserX userX, @RequestBody Optional<GetSchoolsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(GetSchoolsRequest.getDefaultInstance()),
            (request, log) -> {
              return getAllSchools(request.getDistrictId(), -1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/SchoolManagementService/UpsertSchool")
  @ResponseBody
  public SchoolInformationResponse upsertSchool(
      @Admin UserX userX, @RequestBody Optional<UpsertSchoolRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(UpsertSchoolRequest.getDefaultInstance()),
            (request, log) -> {
              School school =
                  new School()
                      .setCreationTime(Instant.now())
                      .setDistrict(
                          db.getDistrictRepository()
                              .findById(request.getSchool().getDistrictId())
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
      @Admin UserX userX, @RequestBody Optional<RemoveSchoolRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(RemoveSchoolRequest.getDefaultInstance()),
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
    response.addAllSchools(DataAccess.getProtoSchoolsByDistrictId(db, districtId));
    return response.build();
  }
}
