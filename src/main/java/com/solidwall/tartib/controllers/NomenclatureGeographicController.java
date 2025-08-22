package com.solidwall.tartib.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.nomenclatureGeographic.*;
import com.solidwall.tartib.entities.NomenclatureGeographicEntity;
import com.solidwall.tartib.implementations.NomenclatureGeographicImplementation;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("nomenclature-geo")
public class NomenclatureGeographicController {

    @Autowired
    private NomenclatureGeographicImplementation nomenclatureGeographicImplementation;

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponseHelper<NomenclatureGeographicEntity>> create(
            @ModelAttribute CreateNomenclatureGeographicDto data) {  // Changed from @RequestBody to @ModelAttribute
        CustomResponseHelper<NomenclatureGeographicEntity> response = CustomResponseHelper
            .<NomenclatureGeographicEntity>builder()
            .body(nomenclatureGeographicImplementation.create(data))
            .message("Nomenclature Geographic created successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
    
        return ResponseEntity.status(response.getStatus()).body(response);
            }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<NomenclatureGeographicEntity>> update(
            @PathVariable Long id,
            @ModelAttribute UpdateNomenclatureGeographicDto data) {
        CustomResponseHelper<NomenclatureGeographicEntity> response = CustomResponseHelper
            .<NomenclatureGeographicEntity>builder()
            .body(nomenclatureGeographicImplementation.update(id, data))
            .message("Nomenclature Geographic updated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<NomenclatureGeographicEntity>> getOne(
            @PathVariable Long id) {
        CustomResponseHelper<NomenclatureGeographicEntity> response = CustomResponseHelper
            .<NomenclatureGeographicEntity>builder()
            .body(nomenclatureGeographicImplementation.getOne(id))
            .message("Nomenclature Geographic retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<NomenclatureGeographicEntity>>> findAll(
            @RequestParam(required = false) Map<String, String> filters) {
        CustomResponseHelper<List<NomenclatureGeographicEntity>> response = CustomResponseHelper
            .<List<NomenclatureGeographicEntity>>builder()
            .body(nomenclatureGeographicImplementation.findAll(filters))
            .message("Nomenclature Geographics list retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("active")
    public ResponseEntity<CustomResponseHelper<NomenclatureGeographicEntity>> findActive() {
        CustomResponseHelper<NomenclatureGeographicEntity> response = CustomResponseHelper
            .<NomenclatureGeographicEntity>builder()
            .body(nomenclatureGeographicImplementation.findActive())
            .message("Active nomenclature Geographic retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("activate/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> activate(@PathVariable Long id) {
        nomenclatureGeographicImplementation.activate(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Nomenclature Geographic activated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        nomenclatureGeographicImplementation.delete(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Nomenclature Geographic deleted successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("deactivate-all")
    public ResponseEntity<CustomResponseHelper<Void>> deactivateAll() {
        nomenclatureGeographicImplementation.deactivateAll();
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("All nomenclature Geographics deactivated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}