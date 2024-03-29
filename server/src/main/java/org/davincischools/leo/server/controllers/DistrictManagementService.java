package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;
import java.util.stream.Collectors;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.district_management.AddDistrictRequest;
import org.davincischools.leo.protos.district_management.DistrictInformationResponse;
import org.davincischools.leo.protos.district_management.GetDistrictsRequest;
import org.davincischools.leo.protos.district_management.RemoveDistrictRequest;
import org.davincischools.leo.protos.district_management.UpdateDistrictRequest;
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
public class DistrictManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/DistrictManagementService/GetDistricts")
  @ResponseBody
  public DistrictInformationResponse getDistricts(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetDistrictsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(DistrictInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetDistrictsRequest.getDefaultInstance()))
        .andThen((request, log) -> getAllDistricts(-1))
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/AddDistrict")
  @ResponseBody
  public DistrictInformationResponse addDistrict(
      @AdminX HttpUserX userX,
      @RequestBody Optional<AddDistrictRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(DistrictInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(AddDistrictRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (request.hasDistrict()) {
                District district =
                    db.getDistrictRepository().upsert(request.getDistrict().getName());
                return getAllDistricts(district.getId());
              }

              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/UpdateDistrict")
  @ResponseBody
  public DistrictInformationResponse updateDistrict(
      @AdminX HttpUserX userX,
      @RequestBody Optional<UpdateDistrictRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(DistrictInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpdateDistrictRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              db.getDistrictRepository().upsert(request.getDistrict().getName());
              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/RemoveDistrict")
  @ResponseBody
  public DistrictInformationResponse removeDistrict(
      @AdminX HttpUserX userX,
      @RequestBody Optional<RemoveDistrictRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(DistrictInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(RemoveDistrictRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
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
        db.getDistrictRepository().findAll().stream()
            .map(
                district ->
                    org.davincischools.leo.protos.pl_types.District.newBuilder()
                        .setId(district.getId())
                        .setName(district.getName())
                        .build())
            .collect(Collectors.toList()));

    return response.build();
  }
}
