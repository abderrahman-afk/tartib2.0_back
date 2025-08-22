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
import com.solidwall.tartib.entities.FundingExtensionEntity;
import com.solidwall.tartib.implementations.FundingExtensionImplementation;

@RestController
@RequestMapping("funding-extension")
public class FundingExtensionController {

  @Autowired
  FundingExtensionImplementation fundingExtensionImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<FundingExtensionEntity>>> findAll() {
    CustomResponseHelper<List<FundingExtensionEntity>> response = CustomResponseHelper
        .<List<FundingExtensionEntity>>builder()
        .body(fundingExtensionImplementation.findAll())
        .message("funding extension list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<FundingExtensionEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<FundingExtensionEntity> response = CustomResponseHelper.<FundingExtensionEntity>builder()
        .body(fundingExtensionImplementation.getOne(id))
        .message("funding extension data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<FundingExtensionEntity>> create(
      @RequestBody FundingExtensionEntity data) {
    CustomResponseHelper<FundingExtensionEntity> response = CustomResponseHelper.<FundingExtensionEntity>builder()
        .body(fundingExtensionImplementation.create(data))
        .message("funding extension created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<FundingExtensionEntity>> update(@PathVariable("id") Long id,
      @RequestBody FundingExtensionEntity user) {
    CustomResponseHelper<FundingExtensionEntity> response = CustomResponseHelper.<FundingExtensionEntity>builder()
        .body(fundingExtensionImplementation.update(id, user))
        .message("funding extension updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    fundingExtensionImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("funding extension deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
