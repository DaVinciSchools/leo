package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.district_management.AddDistrictRequest;
import org.davincischools.leo.protos.district_management.DistrictInformationResponse;
import org.davincischools.leo.protos.district_management.GetDistrictsRequest;
import org.davincischools.leo.protos.district_management.RemoveDistrictRequest;
import org.davincischools.leo.protos.district_management.UpdateDistrictRequest;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DistrictManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/DistrictManagementService/GetDistricts")
  @ResponseBody
  public DistrictInformationResponse getDistricts(
      @RequestBody Optional<GetDistrictsRequest> optionalRequest) throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            Optional.empty(),
            optionalRequest.orElse(GetDistrictsRequest.getDefaultInstance()),
            (request, logEntry) -> {
              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/AddDistrict")
  @ResponseBody
  public DistrictInformationResponse addDistrict(
      @RequestBody Optional<AddDistrictRequest> optionalRequest) throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            Optional.empty(),
            optionalRequest.orElse(AddDistrictRequest.getDefaultInstance()),
            (request, logEntry) -> {
              if (request.hasDistrict()) {
                District district =
                    new District()
                        .setCreationTime(Instant.now())
                        .setName(request.getDistrict().getName());
                db.getDistrictRepository().save(district);
                return getAllDistricts(district.getId());
              }

              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/UpdateDistrict")
  @ResponseBody
  public DistrictInformationResponse updateDistrict(
      @RequestBody Optional<UpdateDistrictRequest> optionalRequest) throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            Optional.empty(),
            optionalRequest.orElse(UpdateDistrictRequest.getDefaultInstance()),
            (request, logEntry) -> {
              if (request.getDistrict().hasId()) {
                db.getDistrictRepository()
                    .save(
                        new District()
                            .setCreationTime(Instant.now())
                            .setId(request.getDistrict().getId())
                            .setName(request.getDistrict().getName()));
                return getAllDistricts(request.getDistrict().getId());
              }

              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/RemoveDistrict")
  @ResponseBody
  public DistrictInformationResponse removeDistrict(
      @RequestBody Optional<RemoveDistrictRequest> optionalRequest) throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            Optional.empty(),
            optionalRequest.orElse(RemoveDistrictRequest.getDefaultInstance()),
            (request, logEntry) -> {
              checkArgument(request.hasDistrictId());

              db.getDistrictRepository().deleteById(request.getDistrictId());

              return getAllDistricts(-1);
            })
        .finish();
  }

  private DistrictInformationResponse getAllDistricts(int modifiedDistrictId) {
    DistrictInformationResponse.Builder response = DistrictInformationResponse.newBuilder();

    response.setModifiedDistrictId(modifiedDistrictId);
    response.addAllDistricts(
        StreamSupport.stream(db.getDistrictRepository().findAll().spliterator(), false)
            .map(
                district ->
                    org.davincischools.leo.protos.district_management.District.newBuilder()
                        .setId(district.getId())
                        .setName(district.getName())
                        .build())
            .collect(Collectors.toList()));

    return response.build();
  }
}
