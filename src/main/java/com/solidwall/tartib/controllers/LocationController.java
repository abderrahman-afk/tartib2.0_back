package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.entities.DeanshipEntity;
import com.solidwall.tartib.entities.DelegationEntity;
import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.services.LocationService;
@RestController
@RequestMapping("locations")
public class LocationController {
      @Autowired
    private LocationService locationService;

    @GetMapping("/districts")
    public ResponseEntity<CustomResponseHelper<List<DistrictEntity>>> getAllDistricts() {
    CustomResponseHelper<List<DistrictEntity>> response = CustomResponseHelper.<List<DistrictEntity>>builder()
        .body(locationService.getAllDistricts())
        .message("governorate list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);

        
    }

    @GetMapping("/governorates")
    public ResponseEntity<CustomResponseHelper<List<GovernorateEntity>>> getGovernoratesByDistricts(@RequestParam List<Long> districtIds) {
        CustomResponseHelper<List<GovernorateEntity>> response = CustomResponseHelper.<List<GovernorateEntity>>builder()
        .body(locationService.getGovernoratesByDistricts(districtIds))
        .message("governorate list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);

    }

    @GetMapping("/delegations")
    public ResponseEntity<CustomResponseHelper<List<DelegationEntity>>> getDelegationsByGovernorates(@RequestParam List<Long> governorateIds) {
        CustomResponseHelper<List<DelegationEntity>> response = CustomResponseHelper.<List<DelegationEntity>>builder()

        .body(locationService.getDelegationsByGovernorates(governorateIds))
        .message("governorate list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/deanships")
    public ResponseEntity<CustomResponseHelper<List<DeanshipEntity>>> getDeanshipsByDelegations(@RequestParam List<Long> delegationIds) {

        CustomResponseHelper<List<DeanshipEntity>> response = CustomResponseHelper.<List<DeanshipEntity>>builder()
        .body(locationService.getDeanshipsByDelegations(delegationIds))
        .message("governorate list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
    }
}
