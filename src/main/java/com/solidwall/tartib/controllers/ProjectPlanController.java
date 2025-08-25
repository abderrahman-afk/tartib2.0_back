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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.project.plan.*;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.implementations.ProjectPlanImplementation;
import com.solidwall.tartib.services.ProjectPlanService;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("project-plan")
public class ProjectPlanController {

  @Autowired
  ProjectPlanImplementation projectPlanImplementation;

  @Autowired
  ProjectPlanService projectPlanService;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectPlanEntity>>> findAll() {
    CustomResponseHelper<List<ProjectPlanEntity>> response = CustomResponseHelper.<List<ProjectPlanEntity>>builder()
        .body(projectPlanImplementation.findAll())
        .message("project plan list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectPlanEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectPlanEntity> response = CustomResponseHelper.<ProjectPlanEntity>builder()
        .body(projectPlanImplementation.getOne(id))
        .message("project plan data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectPlanEntity>> findOne(@RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectPlanEntity> response = CustomResponseHelper
        .<ProjectPlanEntity>builder()
        .body(projectPlanImplementation.findOne(reqParam))
        .message("project plan found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  private ResponseEntity<CustomResponseHelper<ProjectPlanEntity>> create(@ModelAttribute CreateDto data) {
    CustomResponseHelper<ProjectPlanEntity> response = CustomResponseHelper.<ProjectPlanEntity>builder()
        .body(projectPlanImplementation.create(data))
        .message("project plan created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  private ResponseEntity<CustomResponseHelper<ProjectPlanEntity>> update(@PathVariable("id") Long id,
      @ModelAttribute UpdateDto user) {
    CustomResponseHelper<ProjectPlanEntity> response = CustomResponseHelper.<ProjectPlanEntity>builder()
        .body(projectPlanImplementation.update(id, user))
        .message("project plan updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectPlanImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project plan deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
      try {
          Resource fileResource = projectPlanService.downloadPlanFinancementFile(id);

          ProjectPlanEntity projectPlan = projectPlanImplementation.getOne(id);

          String contentType = projectPlanService.determineContentType(projectPlan.getPlanFinancement());

          return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                      fileResource.getFilename() + "\"")
              .body(fileResource);

      } catch (NotFoundException e) {
          return ResponseEntity.notFound().build();
      } catch (Exception e) {
          return ResponseEntity.internalServerError().build();
      }
  }
}
