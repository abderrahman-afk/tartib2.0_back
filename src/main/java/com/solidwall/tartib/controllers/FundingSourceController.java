package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.entities.FundingSourceEntity;
import com.solidwall.tartib.implementations.FundingSourceImplementation;

@RestController
@RequestMapping("funding-source")
public class FundingSourceController {

  @Autowired
  FundingSourceImplementation fundingSourceImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<FundingSourceEntity>>> findAll() {
    CustomResponseHelper<List<FundingSourceEntity>> response = CustomResponseHelper
        .<List<FundingSourceEntity>>builder()
        .body(fundingSourceImplementation.findAll())
        .message("funding source list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<FundingSourceEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<FundingSourceEntity> response = CustomResponseHelper.<FundingSourceEntity>builder()
        .body(fundingSourceImplementation.getOne(id))
        .message("funding source data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<FundingSourceEntity>> create(@RequestBody FundingSourceEntity data) {
    CustomResponseHelper<FundingSourceEntity> response = CustomResponseHelper.<FundingSourceEntity>builder()
        .body(fundingSourceImplementation.create(data))
        .message("funding source created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<FundingSourceEntity>> update(@PathVariable("id") Long id,
      @RequestBody FundingSourceEntity user) {
    CustomResponseHelper<FundingSourceEntity> response = CustomResponseHelper.<FundingSourceEntity>builder()
        .body(fundingSourceImplementation.update(id, user))
        .message("funding source updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    fundingSourceImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("funding source deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
