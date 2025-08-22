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
import com.solidwall.tartib.dto.cnapValidationReserve.CreateDto;
import com.solidwall.tartib.dto.cnapValidationReserve.UpdateDto;
import com.solidwall.tartib.entities.CnapValidationReserveEntity;
import com.solidwall.tartib.implementations.CnapValidationReserveImplementation;
@RestController
@RequestMapping("cnap-validation-reserve")
public class CnapValidationReserveController {

    @Autowired
    private CnapValidationReserveImplementation cnapValidationReserveImplementation;

     @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<CnapValidationReserveEntity>>> findAll() {
        CustomResponseHelper<List<CnapValidationReserveEntity>> response = CustomResponseHelper
            .<List<CnapValidationReserveEntity>>builder()
            .body(cnapValidationReserveImplementation.findAll())
            .message("Liste des réserves de validation CNAP récupérée avec succès")
            .error(false)
            .status(200)
            .timestamp(new java.util.Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("find-by-project/{projectIdentityId}")
    public ResponseEntity<CustomResponseHelper<CnapValidationReserveEntity>> findByProjectIdentityId(
            @PathVariable Long projectIdentityId) {
        CnapValidationReserveEntity reserve = cnapValidationReserveImplementation.findByProjectIdentityId(projectIdentityId);
        CustomResponseHelper<CnapValidationReserveEntity> response = CustomResponseHelper
            .<CnapValidationReserveEntity>builder()
            .body(reserve)
            .message("Réserve de validation CNAP trouvée avec succès")
            .error(false)
            .status(200)
            .timestamp(new java.util.Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("find-one/{id}")
    public ResponseEntity<CustomResponseHelper<CnapValidationReserveEntity>> findOne(@PathVariable Long id) {
        CnapValidationReserveEntity reserve = cnapValidationReserveImplementation.getOne(id);
        CustomResponseHelper<CnapValidationReserveEntity> response = CustomResponseHelper
            .<CnapValidationReserveEntity>builder()
            .body(reserve)
            .message("Réserve de validation CNAP trouvée avec succès")
            .error(false)
            .status(200)
            .timestamp(new java.util.Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

 @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<CnapValidationReserveEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<CnapValidationReserveEntity> response = CustomResponseHelper
            .<CnapValidationReserveEntity>builder()
            .body(cnapValidationReserveImplementation.create(data))
            .message(" cnapValidationReserve créée avec succès")
            .error(false)
            .status(HttpStatus.CREATED.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<CnapValidationReserveEntity>> update(
            @PathVariable Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<CnapValidationReserveEntity> response = CustomResponseHelper
            .<CnapValidationReserveEntity>builder()
            .body(cnapValidationReserveImplementation.update(id, data))
            .message(" cnapValidationReserve mise à jour avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        cnapValidationReserveImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
            .message(" cnapValidationReserve supprimée avec succès")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
