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
import com.solidwall.tartib.dto.project.risk.CreateDto;
import com.solidwall.tartib.dto.project.risk.UpdateDto;
import com.solidwall.tartib.entities.ProjectRiskEntity;
import com.solidwall.tartib.implementations.ProjectRiskImplementation;

@RestController
@RequestMapping("project-risk")
public class ProjectRiskController {

  @Autowired
  ProjectRiskImplementation projectRiskImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectRiskEntity>>> findAll() {
    CustomResponseHelper<List<ProjectRiskEntity>> response = CustomResponseHelper
        .<List<ProjectRiskEntity>>builder()
        .body(projectRiskImplementation.findAll())
        .message("project risk list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({"all/{id}"})
  public ResponseEntity<CustomResponseHelper<List<ProjectRiskEntity>>> findAllByProjectId(@PathVariable Long id) {
    CustomResponseHelper<List<ProjectRiskEntity>> response = CustomResponseHelper
        .<List<ProjectRiskEntity>>builder()
        .body(projectRiskImplementation.findAllByProjectIdentity(id))
        .message("project risk list by proejct id")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectRiskEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectRiskEntity> response = CustomResponseHelper.<ProjectRiskEntity>builder()
        .body(projectRiskImplementation.getOne(id))
        .message("project risk data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectRiskEntity>> findOne(@RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectRiskEntity> response = CustomResponseHelper
        .<ProjectRiskEntity>builder()
        .body(projectRiskImplementation.findOne(reqParam))
        .message("project risk found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ProjectRiskEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<ProjectRiskEntity> response = CustomResponseHelper.<ProjectRiskEntity>builder()
        .body(projectRiskImplementation.create(data))
        .message("project risk created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectRiskEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<ProjectRiskEntity> response = CustomResponseHelper.<ProjectRiskEntity>builder()
        .body(projectRiskImplementation.update(id, data))
        .message("project risk updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectRiskImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project risk deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
