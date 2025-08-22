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
import com.solidwall.tartib.dto.admissibilitygrid.CreateDto;
import com.solidwall.tartib.dto.admissibilitygrid.UpdateDto;
import com.solidwall.tartib.entities.AdmissibilityGridEntity;
import com.solidwall.tartib.implementations.AdmissibilityGridImplementation;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

@RestController
@RequestMapping("admissibiliy-grid")
public class AdmissibilityGridController {

  @Autowired
  AdmissibilityGridImplementation admissibilityGridImplementation;
  @Autowired
  AuthenticationFacade authenticationFacade;

  @Autowired
  private NotificationService notificationService;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<AdmissibilityGridEntity>>> findAll() {
    CustomResponseHelper<List<AdmissibilityGridEntity>> response = CustomResponseHelper
        .<List<AdmissibilityGridEntity>>builder()
        .body(admissibilityGridImplementation.findAll())
        .message("grids list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<AdmissibilityGridEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<AdmissibilityGridEntity> response = CustomResponseHelper.<AdmissibilityGridEntity>builder()
        .body(admissibilityGridImplementation.getOne(id))
        .message("grid data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<AdmissibilityGridEntity>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<AdmissibilityGridEntity> response = CustomResponseHelper.<AdmissibilityGridEntity>builder()
        .body(admissibilityGridImplementation.create(data))
        .message("grid created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<AdmissibilityGridEntity>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<AdmissibilityGridEntity> response = CustomResponseHelper.<AdmissibilityGridEntity>builder()
        .body(admissibilityGridImplementation.update(id, data))
        .message("grid updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    admissibilityGridImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("grid deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "find" })
  public ResponseEntity<CustomResponseHelper<AdmissibilityGridEntity>> findOne(
      @RequestParam Map<String, String> reqParam) {
    CustomResponseHelper<AdmissibilityGridEntity> response = CustomResponseHelper
        .<AdmissibilityGridEntity>builder()
        .body(admissibilityGridImplementation.findOne(reqParam))
        .message("Admissibility Grid found")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  // // ACCES NEEDED CHECK
  @PutMapping("activate/{id}")
  public ResponseEntity<CustomResponseHelper<AdmissibilityGridEntity>> activateGrid(@PathVariable Long id,
      @RequestBody Boolean status) {

        long getCurrentUserId = authenticationFacade.getCurrentUserId();
           String message = " Grille d'admissibilité activée avec succès: "
                + admissibilityGridImplementation.getOne(id).getName();

    CustomResponseHelper<AdmissibilityGridEntity> response = CustomResponseHelper.<AdmissibilityGridEntity>builder()
        .body(admissibilityGridImplementation.activateGrid(id, status))
        .message("Grille d'admissibilité activée avec succès")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();


      notificationService.buildAndSendNotification(getCurrentUserId,message, "admissibility_grid_notif" );


    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("desactivateAll")
  public ResponseEntity<CustomResponseHelper<List<AdmissibilityGridEntity>>> desactivateAll() {
    CustomResponseHelper<List<AdmissibilityGridEntity>> response = CustomResponseHelper
        .<List<AdmissibilityGridEntity>>builder()
        .body(admissibilityGridImplementation.desactivateAll())
        .message("Tous les grilles d'admissibilité sont desactivée avec succès")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();

    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
