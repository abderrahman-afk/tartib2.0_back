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
import com.solidwall.tartib.dto.governorate.CreateDto;
import com.solidwall.tartib.dto.governorate.UpdateDto;
import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.implementations.GovernorateImplementation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("governorate")
public class GovernoratController {

  @Autowired
  GovernorateImplementation governorateImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<GovernorateEntity>>> findAll(@RequestParam Map<String, String> param) {
    CustomResponseHelper<List<GovernorateEntity>> response = CustomResponseHelper.<List<GovernorateEntity>>builder()
        .body(governorateImplementation.findAll(param))
        .message("governorate list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<GovernorateEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<GovernorateEntity> response = CustomResponseHelper.<GovernorateEntity>builder()
        .body(governorateImplementation.getOne(id))
        .message("governorate data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<GovernorateEntity>> create(@Valid @RequestBody CreateDto data) {
    CustomResponseHelper<GovernorateEntity> response = CustomResponseHelper.<GovernorateEntity>builder()
        .body(governorateImplementation.create(data))
        .message("governorate created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<GovernorateEntity>> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateDto user) {
    CustomResponseHelper<GovernorateEntity> response = CustomResponseHelper.<GovernorateEntity>builder()
        .body(governorateImplementation.update(id, user))
        .message("governorate updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    governorateImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("governorate deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({"findByDistrict/{id}"})
  public ResponseEntity<CustomResponseHelper<List<GovernorateEntity>>> findByDistrict(@PathVariable  Long id ) {
    CustomResponseHelper<List<GovernorateEntity>> response = CustomResponseHelper.<List<GovernorateEntity>>builder()
        .body(governorateImplementation.findByDistrict(id))
        .message("governorate list by district")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
