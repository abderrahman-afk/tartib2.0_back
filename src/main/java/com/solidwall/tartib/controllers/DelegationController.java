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
import com.solidwall.tartib.dto.delegation.CreateDto;
import com.solidwall.tartib.dto.delegation.UpdateDto;
import com.solidwall.tartib.entities.DelegationEntity;
import com.solidwall.tartib.implementations.DelegationImplementation;

@RestController
@RequestMapping("delegation")
public class DelegationController {

  @Autowired
  DelegationImplementation delegationImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<DelegationEntity>>> findAll() {
    CustomResponseHelper<List<DelegationEntity>> response = CustomResponseHelper.<List<DelegationEntity>>builder()
        .body(delegationImplementation.findAll())
        .message("delegation list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<DelegationEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<DelegationEntity> response = CustomResponseHelper.<DelegationEntity>builder()
        .body(delegationImplementation.getOne(id))
        .message("delegation data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<DelegationEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<DelegationEntity> response = CustomResponseHelper.<DelegationEntity>builder()
        .body(delegationImplementation.create(data))
        .message("delegation created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<DelegationEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto user) {
    CustomResponseHelper<DelegationEntity> response = CustomResponseHelper.<DelegationEntity>builder()
        .body(delegationImplementation.update(id, user))
        .message("delegation updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    delegationImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("delegation deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);

  }

    @GetMapping({"findByGouvernorat/{id}"})
  public ResponseEntity<CustomResponseHelper<List<DelegationEntity>>> findByDistrict(@PathVariable  Long id ) {
    CustomResponseHelper<List<DelegationEntity>> response = CustomResponseHelper.<List<DelegationEntity>>builder()
        .body(delegationImplementation.findByGovernorat(id))
        .message("delegation list by gouvernorat")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
