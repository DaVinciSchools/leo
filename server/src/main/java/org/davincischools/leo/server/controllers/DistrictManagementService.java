package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

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
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user.Admin;
import org.davincischools.leo.server.utils.http_user.HttpUser;
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
      @Admin HttpUser user,
      @RequestBody Optional<GetDistrictsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(DistrictInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetDistrictsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/AddDistrict")
  @ResponseBody
  public DistrictInformationResponse addDistrict(
      @Admin HttpUser user,
      @RequestBody Optional<AddDistrictRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(DistrictInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(AddDistrictRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (request.hasDistrict()) {
                District district = db.createDistrict(request.getDistrict().getName());
                return getAllDistricts(district.getId());
              }

              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/UpdateDistrict")
  @ResponseBody
  public DistrictInformationResponse updateDistrict(
      @Admin HttpUser user,
      @RequestBody Optional<UpdateDistrictRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(DistrictInformationResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpdateDistrictRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              db.createDistrict(request.getDistrict().getName());
              return getAllDistricts(-1);
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/RemoveDistrict")
  @ResponseBody
  public DistrictInformationResponse removeDistrict(
      @Admin HttpUser user,
      @RequestBody Optional<RemoveDistrictRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(DistrictInformationResponse.getDefaultInstance());
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
        StreamSupport.stream(db.getDistrictRepository().findAll().spliterator(), false)
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
