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
import com.solidwall.tartib.dto.project.zone.CreateDto;
import com.solidwall.tartib.dto.project.zone.UpdateDto;
import com.solidwall.tartib.entities.ProjectZoneEntity;
import com.solidwall.tartib.implementations.ProjectZoneImplementation;

@RestController
@RequestMapping("project-zone")
public class ProjectZoneController {

  @Autowired
  ProjectZoneImplementation projectZoneImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectZoneEntity>>> findAll() {
    CustomResponseHelper<List<ProjectZoneEntity>> response = CustomResponseHelper.<List<ProjectZoneEntity>>builder()
        .body(projectZoneImplementation.findAll())
        .message("project zone list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectZoneEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectZoneEntity> response = CustomResponseHelper.<ProjectZoneEntity>builder()
        .body(projectZoneImplementation.getOne(id))
        .message("project zone data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectZoneEntity>> findOne(@RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectZoneEntity> response = CustomResponseHelper
        .<ProjectZoneEntity>builder()
        .body(projectZoneImplementation.findOne(reqParam))
        .message("project zone found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ProjectZoneEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<ProjectZoneEntity> response = CustomResponseHelper.<ProjectZoneEntity>builder()
        .body(projectZoneImplementation.create(data))
        .message("project zone created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectZoneEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<ProjectZoneEntity> response = CustomResponseHelper.<ProjectZoneEntity>builder()
        .body(projectZoneImplementation.update(id, data))
        .message("project zone updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectZoneImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project zone deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
