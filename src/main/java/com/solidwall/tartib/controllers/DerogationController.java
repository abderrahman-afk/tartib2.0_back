
package com.solidwall.tartib.controllers;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.derrogation.*;
import com.solidwall.tartib.entities.DerogationEntity;
import com.solidwall.tartib.enums.DerogationStatus;
import com.solidwall.tartib.implementations.DerogationImplementation;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("derogation")
public class DerogationController {

    @Autowired
    private DerogationImplementation derogationImplementation;

    @Autowired
  AuthenticationFacade authenticationFacade;

  @Autowired
  private NotificationService notificationService;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<DerogationEntity>>> findAll() {
        CustomResponseHelper<List<DerogationEntity>> response = CustomResponseHelper
            .<List<DerogationEntity>>builder()
            .body(derogationImplementation.findAll())
            .message("Liste des dérogations récupérée avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("status/{status}")
    public ResponseEntity<CustomResponseHelper<List<DerogationEntity>>> findByStatus(
            @PathVariable DerogationStatus status) {
        CustomResponseHelper<List<DerogationEntity>> response = CustomResponseHelper
            .<List<DerogationEntity>>builder()
            .body(derogationImplementation.findByStatus(status))
            .message("Dérogations par statut récupérées avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<CustomResponseHelper<DerogationEntity>> findByProjectId(
            @PathVariable Long projectId) {
        
        // Find the most recent derogation for this project
        DerogationEntity derogation = derogationImplementation.findLatestByProjectId(projectId);
        
        CustomResponseHelper<DerogationEntity> response = CustomResponseHelper
            .<DerogationEntity>builder()
            .body(derogation)
            .message(derogation != null ? "Dérogation trouvée" : "Aucune dérogation trouvée")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("find")
    public ResponseEntity<CustomResponseHelper<DerogationEntity>> findOne(
            @RequestParam Map<String, String> params) {
        CustomResponseHelper<DerogationEntity> response = CustomResponseHelper
            .<DerogationEntity>builder()
            .body(derogationImplementation.findOne(params))
            .message("Dérogation trouvée")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<DerogationEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<DerogationEntity> response = CustomResponseHelper
            .<DerogationEntity>builder()
            .body(derogationImplementation.getOne(id))
            .message("Dérogation récupérée avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
     // // ACCES NEEDED CHECK
    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<DerogationEntity>> create(@RequestBody CreateDto data) {
          long getCurrentUserId = authenticationFacade.getCurrentUserId();
        

        CustomResponseHelper<DerogationEntity> response = CustomResponseHelper
            .<DerogationEntity>builder()
            .body(derogationImplementation.create(data))
            .message("Dérogation créée avec succès")
            .error(false)
            .status(HttpStatus.CREATED.value())
            .timestamp(new Date())
            .build();
         String message = " Dérogation créée avec succès "
                + derogationImplementation.getOne(response.getBody().getId()).getProjectIdentity().getName();  
        notificationService.buildAndSendNotification(getCurrentUserId,message, "derrogation_notif" );
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
     // // ACCES NEEDED CHECK
    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<DerogationEntity>> update(
            @PathVariable Long id,
            @RequestBody UpdateDto data) {

         long getCurrentUserId = authenticationFacade.getCurrentUserId();
          String message = " Dérogation mise à jour avec succès "
                + derogationImplementation.getOne(id).getProjectIdentity().getName();  
        CustomResponseHelper<DerogationEntity> response = CustomResponseHelper
            .<DerogationEntity>builder()
            .body(derogationImplementation.update(id, data))
            .message("Dérogation mise à jour avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
             notificationService.buildAndSendNotification(getCurrentUserId,message, "derrogation_notif" );
        return ResponseEntity.status(response.getStatus()).body(response);
    }
     // // ACCES NEEDED CHECK
    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
         long getCurrentUserId = authenticationFacade.getCurrentUserId();
           String message = " Dérogation supprimée avec succès "
                + derogationImplementation.getOne(id).getProjectIdentity().getName();   

        derogationImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
            .message("Dérogation supprimée avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
         notificationService.buildAndSendNotification(getCurrentUserId,message, "derrogation_notif" );    
        return ResponseEntity.status(response.getStatus()).body(response);

    }
    // // ACCES NEEDED CHECK
    @PostMapping("{id}/accept")
    public ResponseEntity<CustomResponseHelper<DerogationEntity>> accept(
            @PathVariable Long id,
            @RequestParam String decidedBy) {

             long getCurrentUserId = authenticationFacade.getCurrentUserId(); 
            
             String message = " Dérogation acceptée avec succès "
                + derogationImplementation.getOne(id).getProjectIdentity().getName();   

        CustomResponseHelper<DerogationEntity> response = CustomResponseHelper
            .<DerogationEntity>builder()
            .body(derogationImplementation.acceptDerogation(id, decidedBy))
            .message("Dérogation acceptée avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
     notificationService.buildAndSendNotification(getCurrentUserId,message, "derrogation_notif" );

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
     // // ACCES NEEDED CHECK
    @PostMapping("{id}/reject")
    public ResponseEntity<CustomResponseHelper<DerogationEntity>> reject(
            @PathVariable Long id,
            @RequestParam String decidedBy) {

            long getCurrentUserId = authenticationFacade.getCurrentUserId();
             String message = " Dérogation rejetée avec succès: "
                + derogationImplementation.getOne(id).getProjectIdentity().getName();

        CustomResponseHelper<DerogationEntity> response = CustomResponseHelper

            .<DerogationEntity>builder()
            .body(derogationImplementation.rejectDerogation(id, decidedBy))
            .message("Dérogation rejetée avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
            notificationService.buildAndSendNotification(getCurrentUserId,message, "derrogation_notif" );

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}