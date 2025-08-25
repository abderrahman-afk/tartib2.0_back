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
import com.solidwall.tartib.dto.project.logic.CreateDto;
import com.solidwall.tartib.dto.project.logic.UpdateDto;
import com.solidwall.tartib.entities.ProjectLogicEntity;
import com.solidwall.tartib.implementations.ProjectLogicImplementation;
import com.solidwall.tartib.services.ProjectLogicService;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("project-logic")
public class ProjectLogicController {

  @Autowired
  ProjectLogicImplementation projectLogicImplementation;

  @Autowired
  ProjectLogicService projectLogicService;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ProjectLogicEntity>>> findAll() {
    CustomResponseHelper<List<ProjectLogicEntity>> response = CustomResponseHelper.<List<ProjectLogicEntity>>builder()
        .body(projectLogicImplementation.findAll())
        .message("project logic list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ProjectLogicEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ProjectLogicEntity> response = CustomResponseHelper.<ProjectLogicEntity>builder()
        .body(projectLogicImplementation.getOne(id))
        .message("project logic data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<ProjectLogicEntity>> findOne(@RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<ProjectLogicEntity> response = CustomResponseHelper
        .<ProjectLogicEntity>builder()
        .body(projectLogicImplementation.findOne(reqParam))
        .message("project logic found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get-somme/{id}" })
  public ResponseEntity<CustomResponseHelper<Long>> getSommeComposant(@PathVariable  Long id) {
    CustomResponseHelper<Long> response = CustomResponseHelper
        .<Long>builder()
        .body(projectLogicImplementation.getSommeComposant(id))
        .message("project components sum are found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  private ResponseEntity<CustomResponseHelper<ProjectLogicEntity>> create(@ModelAttribute CreateDto data) {
    CustomResponseHelper<ProjectLogicEntity> response = CustomResponseHelper.<ProjectLogicEntity>builder()
        .body(projectLogicImplementation.create(data))
        .message("project logic created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  private ResponseEntity<CustomResponseHelper<ProjectLogicEntity>> update(@PathVariable("id") Long id,
      @ModelAttribute UpdateDto data) {
    CustomResponseHelper<ProjectLogicEntity> response = CustomResponseHelper.<ProjectLogicEntity>builder()
        .body(projectLogicImplementation.update(id, data))
        .message("project logic updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    projectLogicImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("project logic deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("/download/cadre/{id}")
  public ResponseEntity<Resource> downloadCadreFile(@PathVariable Long id) {
      try {
          Resource fileResource = projectLogicService.downloadDocumentCadreFile(id);

          ProjectLogicEntity projectLogic = projectLogicImplementation.getOne(id);

          String contentType = projectLogicService.determineContentType(projectLogic.getDocumentCadre());

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

  @GetMapping("/download/plan-travail/{id}")
  public ResponseEntity<Resource> downloadPlanTravailFile(@PathVariable Long id) {
      try {
          Resource fileResource = projectLogicService.downloadDocumentPlanTravailFile(id);

          ProjectLogicEntity projectLogic = projectLogicImplementation.getOne(id);

          String contentType = projectLogicService.determineContentType(projectLogic.getDocumentPlanTravail());

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
