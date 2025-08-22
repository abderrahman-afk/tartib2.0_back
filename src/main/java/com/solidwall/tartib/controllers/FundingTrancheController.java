package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.funding.tranche.CreateDto;
import com.solidwall.tartib.dto.funding.tranche.UpdateDto;
import com.solidwall.tartib.entities.FundingTrancheEntity;
import com.solidwall.tartib.implementations.FundingTrancheImplementation;

@RestController
@RequestMapping("funding-tranche")
public class FundingTrancheController {

  @Autowired
  FundingTrancheImplementation fundingTrancheImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<FundingTrancheEntity>>> findAll() {
    CustomResponseHelper<List<FundingTrancheEntity>> response = CustomResponseHelper
        .<List<FundingTrancheEntity>>builder()
        .body(fundingTrancheImplementation.findAll())
        .message("funding tranche list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<FundingTrancheEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<FundingTrancheEntity> response = CustomResponseHelper.<FundingTrancheEntity>builder()
        .body(fundingTrancheImplementation.getOne(id))
        .message("funding tranche data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<FundingTrancheEntity>> findOne(
      @RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<FundingTrancheEntity> response = CustomResponseHelper
        .<FundingTrancheEntity>builder()
        .body(fundingTrancheImplementation.findOne(reqParam))
        .message("funding tranche found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<FundingTrancheEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<FundingTrancheEntity> response = CustomResponseHelper.<FundingTrancheEntity>builder()
        .body(fundingTrancheImplementation.create(data))
        .message("funding tranche created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<FundingTrancheEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<FundingTrancheEntity> response = CustomResponseHelper.<FundingTrancheEntity>builder()
        .body(fundingTrancheImplementation.update(id, data))
        .message("funding tranche updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    fundingTrancheImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("funding tranche deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
