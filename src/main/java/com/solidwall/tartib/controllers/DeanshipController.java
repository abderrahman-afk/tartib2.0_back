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
import com.solidwall.tartib.dto.deanship.CreateDto;
import com.solidwall.tartib.dto.deanship.UpdateDto;
import com.solidwall.tartib.entities.DeanshipEntity;
import com.solidwall.tartib.implementations.DeanshipsImplementation;

@RestController
@RequestMapping("deanship")
public class DeanshipController {

  @Autowired
  DeanshipsImplementation deanshipsImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<DeanshipEntity>>> findAll() {
    CustomResponseHelper<List<DeanshipEntity>> response = CustomResponseHelper.<List<DeanshipEntity>>builder()
        .body(deanshipsImplementation.findAll())
        .message("deanship list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<DeanshipEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<DeanshipEntity> response = CustomResponseHelper.<DeanshipEntity>builder()
        .body(deanshipsImplementation.getOne(id))
        .message("deanship data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<DeanshipEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<DeanshipEntity> response = CustomResponseHelper.<DeanshipEntity>builder()
        .body(deanshipsImplementation.create(data))
        .message("deanship created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<DeanshipEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto user) {
    CustomResponseHelper<DeanshipEntity> response = CustomResponseHelper.<DeanshipEntity>builder()
        .body(deanshipsImplementation.update(id, user))
        .message("deanship updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    deanshipsImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("deanship deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

     @GetMapping({"findByDelegation/{id}"})
  public ResponseEntity<CustomResponseHelper<List<DeanshipEntity>>> findByDistrict(@PathVariable  Long id ) {
    CustomResponseHelper<List<DeanshipEntity>> response = CustomResponseHelper.<List<DeanshipEntity>>builder()
        .body(deanshipsImplementation.findByDelegation(id))
        .message("deanship list by delegation")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
