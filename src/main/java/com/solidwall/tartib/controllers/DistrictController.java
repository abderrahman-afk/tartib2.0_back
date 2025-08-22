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
import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.implementations.DistrictImplementation;

@RestController
@RequestMapping("district")
public class DistrictController {

  @Autowired
  DistrictImplementation districtImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<DistrictEntity>>> findAll() {
    CustomResponseHelper<List<DistrictEntity>> response = CustomResponseHelper.<List<DistrictEntity>>builder()
        .body(districtImplementation.findAll())
        .message("districts list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<DistrictEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<DistrictEntity> response = CustomResponseHelper.<DistrictEntity>builder()
        .body(districtImplementation.getOne(id))
        .message("district data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
  @GetMapping({ "getNameById/{id}" })
  private ResponseEntity<CustomResponseHelper<String>> getByName(@PathVariable Long id) {
    String name = districtImplementation.getNameById(id);
    CustomResponseHelper<String> response = CustomResponseHelper.<String>builder()
        .body(name)
        .message("district name")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<DistrictEntity>> create(@RequestBody DistrictEntity data) {
    CustomResponseHelper<DistrictEntity> response = CustomResponseHelper.<DistrictEntity>builder()
        .body(districtImplementation.create(data))
        .message("district created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<DistrictEntity>> update(@PathVariable("id") Long id,
      @RequestBody DistrictEntity user) {
    CustomResponseHelper<DistrictEntity> response = CustomResponseHelper.<DistrictEntity>builder()
        .body(districtImplementation.update(id, user))
        .message("district updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    districtImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("district deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
