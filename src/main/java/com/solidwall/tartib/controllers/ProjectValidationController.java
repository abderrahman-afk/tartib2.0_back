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
import com.solidwall.tartib.dto.project.validation.CreateDto;
import com.solidwall.tartib.dto.project.validation.UpdateDto;
import com.solidwall.tartib.entities.ProjectValidationEntity;
import com.solidwall.tartib.implementations.ProjectValidationImplementation;

@RestController
@RequestMapping("project-validation")
public class ProjectValidationController {

  @Autowired
  ProjectValidationImplementation validationFpImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectValidationEntity>>> findAll() {
    CustomResponseHelper<List<ProjectValidationEntity>> response = CustomResponseHelper
        .<List<ProjectValidationEntity>>builder()
        .body(validationFpImplementation.findAll())
        .message("project validation list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectValidationEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectValidationEntity> response = CustomResponseHelper.<ProjectValidationEntity>builder()
        .body(validationFpImplementation.getOne(id))
        .message("project validation data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectValidationEntity>> findOne(@RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectValidationEntity> response = CustomResponseHelper
        .<ProjectValidationEntity>builder()
        .body(validationFpImplementation.findOne(reqParam))
        .message("project validation found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ProjectValidationEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<ProjectValidationEntity> response = CustomResponseHelper.<ProjectValidationEntity>builder()
        .body(validationFpImplementation.create(data))
        .message("project validation created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectValidationEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<ProjectValidationEntity> response = CustomResponseHelper.<ProjectValidationEntity>builder()
        .body(validationFpImplementation.update(id, data))
        .message("project validation updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    validationFpImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project validation deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
