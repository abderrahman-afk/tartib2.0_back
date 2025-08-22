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
import com.solidwall.tartib.dto.project.CreateDto;
import com.solidwall.tartib.dto.project.UpdateDto;
import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.implementations.ProjectImplementation;

@RestController
@RequestMapping("project")
public class ProjectController {

  @Autowired
  ProjectImplementation projectImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectEntity>>> findAll() {
    CustomResponseHelper<List<ProjectEntity>> response = CustomResponseHelper.<List<ProjectEntity>>builder()
        .body(projectImplementation.findAll())
        .message("projects list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectEntity> response = CustomResponseHelper.<ProjectEntity>builder()
        .body(projectImplementation.getOne(id))
        .message("project found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ProjectEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<ProjectEntity> response = CustomResponseHelper.<ProjectEntity>builder()
        .body(projectImplementation.create(data))
        .message("project created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto user) {
    CustomResponseHelper<ProjectEntity> response = CustomResponseHelper.<ProjectEntity>builder()
        .body(projectImplementation.update(id, user))
        .message("project updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
