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
import com.solidwall.tartib.dto.project.identity.CreateDto;
import com.solidwall.tartib.dto.project.identity.UpdateDto;
import com.solidwall.tartib.dto.projectblock.ProjectBlockRequest;
import com.solidwall.tartib.entities.ProjectBlockEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.implementations.ProjectIdentityImplementation;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

@RestController
@RequestMapping("project-identity")
public class ProjectIdentityController {

  @Autowired
  ProjectIdentityImplementation projectIdentityImplementation;

  @Autowired
  AuthenticationFacade authenticationFacade;

  @Autowired
  private NotificationService notificationService;

  @GetMapping({ "all" })
  public ResponseEntity<CustomResponseHelper<List<ProjectIdentityEntity>>> findAll(@RequestParam(required = false) Boolean archived) {
    CustomResponseHelper<List<ProjectIdentityEntity>> response = CustomResponseHelper
        .<List<ProjectIdentityEntity>>builder()
        .body(projectIdentityImplementation.findAll(archived))
        .message("project identity list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> findOne(
      @RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper
        .<ProjectIdentityEntity>builder()
        .body(projectIdentityImplementation.findOne(reqParam))
        .message("project identity found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
        .body(projectIdentityImplementation.getOne(id))
        .message("project identity data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
        .body(projectIdentityImplementation.create(data))
        .message("project identity created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto user) {
    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
        .body(projectIdentityImplementation.update(id, user))
        .message("project identity updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  // // ACCES NEEDED CHECK

  @PutMapping("validate/{id}")
  public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> validateProject(@PathVariable Long id) {
    long getCurrentUserId = authenticationFacade.getCurrentUserId();
    String message = " Projet validé avec succès: "
        + projectIdentityImplementation.getOne(id).getName();

    ProjectIdentityEntity validatedProject = projectIdentityImplementation.validateProject(id);

    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
        .body(validatedProject)
        .message("Project validated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    notificationService.buildAndSendNotification(getCurrentUserId, message, "fp_notif");
    return ResponseEntity.ok(response);
  }

  // Force validation endpoint (maybe with admin rights)
  // // ACCES NEEDED CHECK

  @PutMapping("force-validate/{id}")
  public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> forceValidateProject(
      @PathVariable Long id) {
    long getCurrentUserId = authenticationFacade.getCurrentUserId();
    String message = "Project force validated successfully "
        + projectIdentityImplementation.getOne(id).getName();

    ProjectIdentityEntity validatedProject = projectIdentityImplementation.ForcevalidateProject(id);

    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
        .body(validatedProject)
        .message("Project force validated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    notificationService.buildAndSendNotification(getCurrentUserId, message, "fp_notif");
    return ResponseEntity.ok(response);
  }
  // // ACCES NEEDED CHECK

  @PutMapping("finalized-project/{id}")
  public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> finalvalidateProject(
      @PathVariable Long id) {
    long getCurrentUserId = authenticationFacade.getCurrentUserId();
    String message = "Project finalized successfully "
        + projectIdentityImplementation.getOne(id).getName();
    ProjectIdentityEntity validatedProject = projectIdentityImplementation.finalvalidateProject(id);

    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
        .body(validatedProject)
        .message("Project  validated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    notificationService.buildAndSendNotification(getCurrentUserId, message, "fp_notif");

    return ResponseEntity.ok(response);
  }
  // Add to ProjectEvaluationController.java
  // // ACCES NEEDED CHECK

  @PostMapping("block/{projectId}")
  public ResponseEntity<CustomResponseHelper<Void>> blockProject(
      @PathVariable Long projectId,
      @RequestBody ProjectBlockRequest request) {
    long getCurrentUserId = authenticationFacade.getCurrentUserId();
    String message = "Projet bloqué avec succès: "
        + projectIdentityImplementation.getOne(projectId).getName();
    projectIdentityImplementation.blockProject(projectId, request.getReason());

    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("Projet bloqué avec succès")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    notificationService.buildAndSendNotification(getCurrentUserId, message, "fp_notif");
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("unblock-project/{id}")
  public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> unBlockProject(
      @PathVariable Long id) {
    ProjectIdentityEntity validatedProject = projectIdentityImplementation.unBlockProject(id);

    CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
        .body(validatedProject)
        .message("Project unblocked successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();

    return ResponseEntity.ok(response);
  }

  @GetMapping("get-block/{id}")
  public ResponseEntity<CustomResponseHelper<ProjectBlockEntity>> getBlock(
      @PathVariable Long id) {
    ProjectBlockEntity ProjectBlock = projectIdentityImplementation.getBlock(id);

    CustomResponseHelper<ProjectBlockEntity> response = CustomResponseHelper.<ProjectBlockEntity>builder()
        .body(ProjectBlock)
        .message("Project unblocked successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();

    return ResponseEntity.ok(response);
  }

  @PutMapping("update-block-reason/{id}")
  public ResponseEntity<CustomResponseHelper<ProjectBlockEntity>> updateBlockReason(
      @PathVariable Long id,
      @RequestBody ProjectBlockRequest request) {
    ProjectBlockEntity updatedBlock = projectIdentityImplementation.updateBlockreason(id, request.getReason());

    CustomResponseHelper<ProjectBlockEntity> response = CustomResponseHelper.<ProjectBlockEntity>builder()
        .body(updatedBlock)
        .message("Project block reason updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();

    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectIdentityImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project identity deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "archive/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> archive(@PathVariable Long id) {
    projectIdentityImplementation.archive(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project identity archive successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("count-by-status")
  public ResponseEntity<CustomResponseHelper<Long>> countProjectsByStatus(
      @RequestParam(required = false) ProjectStaut status) {
    Long count;
    String message;

    if (status != null) {
      count = projectIdentityImplementation.countProjectsByStatus(status);
      message = "Projects count by status retrieved successfully";
    } else {
      count = (long) projectIdentityImplementation.findAll(false).size();
      message = "Total projects count retrieved successfully";
    }

    CustomResponseHelper<Long> response = CustomResponseHelper.<Long>builder()
        .body(count)
        .message(message)
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();

    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("total-budget")
  public ResponseEntity<CustomResponseHelper<Long>> getTotalProjectsBudget() {
    Long totalBudget = projectIdentityImplementation.getTotalProjectsBudget();
    CustomResponseHelper<Long> response = CustomResponseHelper
        .<Long>builder()
        .body(totalBudget)
        .message("Total projects budget retrieved successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.ok(response);
  }

  @GetMapping("unarchive/{id}")
  private ResponseEntity<CustomResponseHelper<Void>> unarchive(@PathVariable Long id) {
    projectIdentityImplementation.unarchive(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project identity unarchived successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
