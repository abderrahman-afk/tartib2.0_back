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
import com.solidwall.tartib.dto.project.study.CreateDto;
import com.solidwall.tartib.dto.project.study.UpdateDto;
import com.solidwall.tartib.entities.ProjectStudyEntity;
import com.solidwall.tartib.implementations.ProjectStudyImplementation;

@RestController
@RequestMapping("project-study")
public class ProjectStudyController {

  @Autowired
  ProjectStudyImplementation projectStudyImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectStudyEntity>>> findAll() {
    CustomResponseHelper<List<ProjectStudyEntity>> response = CustomResponseHelper
        .<List<ProjectStudyEntity>>builder()
        .body(projectStudyImplementation.findAll())
        .message("project study list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectStudyEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectStudyEntity> response = CustomResponseHelper
        .<ProjectStudyEntity>builder()
        .body(projectStudyImplementation.getOne(id))
        .message("project study data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectStudyEntity>> findOne(@RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectStudyEntity> response = CustomResponseHelper
        .<ProjectStudyEntity>builder()
        .body(projectStudyImplementation.findOne(reqParam))
        .message("project study found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ProjectStudyEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<ProjectStudyEntity> response = CustomResponseHelper
        .<ProjectStudyEntity>builder()
        .body(projectStudyImplementation.create(data))
        .message("project study created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectStudyEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<ProjectStudyEntity> response = CustomResponseHelper
        .<ProjectStudyEntity>builder()
        .body(projectStudyImplementation.update(id, data))
        .message("project study updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectStudyImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project study deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);

  }
}
