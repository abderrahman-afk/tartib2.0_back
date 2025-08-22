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
import com.solidwall.tartib.dto.project.strategy.CreateDto;
import com.solidwall.tartib.dto.project.strategy.UpdateDto;
import com.solidwall.tartib.entities.ProjectStrategyEntity;
import com.solidwall.tartib.implementations.ProjectStrategyImplementation;

@RestController
@RequestMapping("project-strategy")
public class ProjectStrategyController {

  @Autowired
  ProjectStrategyImplementation projectStrategyImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectStrategyEntity>>> findAll() {
    CustomResponseHelper<List<ProjectStrategyEntity>> response = CustomResponseHelper
        .<List<ProjectStrategyEntity>>builder()
        .body(projectStrategyImplementation.findAll())
        .message("project strategy list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectStrategyEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectStrategyEntity> response = CustomResponseHelper
        .<ProjectStrategyEntity>builder()
        .body(projectStrategyImplementation.getOne(id))
        .message("project strategy data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

   @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectStrategyEntity>> findOne(@RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectStrategyEntity> response = CustomResponseHelper
        .<ProjectStrategyEntity>builder()
        .body(projectStrategyImplementation.findOne(reqParam))
        .message("project strategy found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ProjectStrategyEntity>> create(
      @RequestBody CreateDto data) {
    CustomResponseHelper<ProjectStrategyEntity> response = CustomResponseHelper
        .<ProjectStrategyEntity>builder()
        .body(projectStrategyImplementation.create(data))
        .message("project strategy created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectStrategyEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<ProjectStrategyEntity> response = CustomResponseHelper
        .<ProjectStrategyEntity>builder()
        .body(projectStrategyImplementation.update(id, data))
        .message("project strategy updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectStrategyImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project strategy deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);

  }
}
