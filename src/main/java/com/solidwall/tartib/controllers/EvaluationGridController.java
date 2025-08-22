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
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.evaluationgrid.CreateDto;
import com.solidwall.tartib.dto.evaluationgrid.UpdateDto;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;
import com.solidwall.tartib.implementations.EvaluationGridImplementation;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

// Grid Controller
@RestController
@RequestMapping("evaluation-grid")
public class EvaluationGridController {

    @Autowired
    EvaluationGridImplementation gridImplementation;
        @Autowired
  AuthenticationFacade authenticationFacade;

  @Autowired
  private NotificationService notificationService;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<EvaluationGridEntity>>> findAll() {
        CustomResponseHelper<List<EvaluationGridEntity>> response = CustomResponseHelper.<List<EvaluationGridEntity>>builder()
                .body(gridImplementation.findAll())
                .message("Evaluation grids list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
                .body(gridImplementation.getOne(id))
                .message("Evaluation grid data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
                .body(gridImplementation.create(data))
                .message("Evaluation grid created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> update(
            @PathVariable Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
                .body(gridImplementation.update(id, data))
                .message("Evaluation grid updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update-general-setting/{id}")
    public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> updateGeneralSetting(
            @PathVariable Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
                .body(gridImplementation.updateGeberalSetting(id, data))
                .message("Evaluation grid updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        gridImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Evaluation grid deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("{id}/max-values")
    public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> updateMaxValues(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> maxValues) {
        try {
            CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
                    .body(gridImplementation.updateMaxValues(id, maxValues))
                    .message("Max values updated successfully")
                    .error(false)
                    .status(HttpStatus.OK.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
                    .message(e.getMessage())
                    .error(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @DeleteMapping("{gridId}/component/{componentId}")
    public ResponseEntity<CustomResponseHelper<Void>> deleteComponent(
            @PathVariable Long gridId,
            @PathVariable Long componentId) {
        try {
            gridImplementation.deleteComponent(gridId, componentId);
            CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                    .message("Component deleted successfully")
                    .error(false)
                    .status(HttpStatus.OK.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalStateException e) {
            CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                    .message(e.getMessage())
                    .error(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @DeleteMapping("{gridId}/component/{componentId}/criteria/{criteriaId}")
    public ResponseEntity<CustomResponseHelper<Void>> deleteCriteria(
            @PathVariable Long gridId,
            @PathVariable Long componentId,
            @PathVariable Long criteriaId) {
        try {
            gridImplementation.deleteCriteria(gridId, componentId, criteriaId);
            CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                    .message("Criteria deleted successfully")
                    .error(false)
                    .status(HttpStatus.OK.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalStateException e) {
            CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                    .message(e.getMessage())
                    .error(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @DeleteMapping("{gridId}/component/{componentId}/criteria/{criteriaId}/indicateur/{indicateurId}")
    public ResponseEntity<CustomResponseHelper<Void>> deleteIndicateur(
            @PathVariable Long gridId,
            @PathVariable Long componentId,
            @PathVariable Long criteriaId,
            @PathVariable Long indicateurId) {
        try {
            gridImplementation.deleteIndicateur(gridId, componentId, criteriaId, indicateurId);
            CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                    .message("Indicateur deleted successfully")
                    .error(false)
                    .status(HttpStatus.OK.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalStateException e) {
            CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                    .message(e.getMessage())
                    .error(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }
     // // ACCES NEEDED CHECK
      @PutMapping("activate/{id}")
  public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> activateGrid(@PathVariable Long id,
      @RequestBody Boolean status) {
         long getCurrentUserId = authenticationFacade.getCurrentUserId();
           String message = " Grille d'évaluation activée avec succès: "
                + gridImplementation.getOne(id).getName();
    CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
        .body(gridImplementation.activateGrid(id, status))
        .message("Grille d'évaluation activée avec succès")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
        notificationService.buildAndSendNotification(getCurrentUserId,message, "evaluation_grid_notif" );

    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("desactivateAll")
  public ResponseEntity<CustomResponseHelper<List<EvaluationGridEntity>>> desactivateAll() {
    CustomResponseHelper<List<EvaluationGridEntity>> response = CustomResponseHelper
        .<List<EvaluationGridEntity>>builder()
        .body(gridImplementation.desactivateAll())
        .message("Tous les grilles d'évaluation sont desactivée avec succès")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("get-default")
  public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> getDefault() {
      CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
              .body(gridImplementation.getDefault())
              .message("Evaluation grid data")
              .error(false)
              .status(HttpStatus.OK.value())
              .timestamp(new Date())
              .build();
      return ResponseEntity.status(response.getStatus()).body(response);
  }
  @PostMapping("duplicate/{id}")
public ResponseEntity<CustomResponseHelper<EvaluationGridEntity>> duplicate(@PathVariable Long id) {
    CustomResponseHelper<EvaluationGridEntity> response = CustomResponseHelper.<EvaluationGridEntity>builder()
            .body(gridImplementation.duplicate(id))
            .message("Grid duplicated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
    return ResponseEntity.status(response.getStatus()).body(response);
}
    @GetMapping("/criteria/{criteriaId}/indicators")
    public ResponseEntity<CustomResponseHelper<List<EvaluationIndicateurEntity>>> getAllIndicators(
            @PathVariable Long criteriaId) {
        CustomResponseHelper<List<EvaluationIndicateurEntity>> response = CustomResponseHelper
            .<List<EvaluationIndicateurEntity>>builder()
            .body(gridImplementation.getAllIndicatorsByCriteriaId(criteriaId))
            .message("Indicators list retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{gridId}/criteria")
    public ResponseEntity<CustomResponseHelper<List<EvaluationCritiriaEntity>>> getAllCriteria(
            @PathVariable Long gridId) {
        CustomResponseHelper<List<EvaluationCritiriaEntity>> response = CustomResponseHelper
            .<List<EvaluationCritiriaEntity>>builder()
            .body(gridImplementation.getAllCriteriaByGridId(gridId))
            .message("Criteria list retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
