package com.solidwall.tartib.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.nomenclatureSecteur.*;
import com.solidwall.tartib.entities.NomenclatureSecteurEntity;
import com.solidwall.tartib.implementations.NomenclatureSecteurImplementation;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("nomenclature-secteur")
public class NomenclatureSecteurController {

    @Autowired
    private NomenclatureSecteurImplementation nomenclatureSecteurImplementation;

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponseHelper<NomenclatureSecteurEntity>> create(
            @ModelAttribute CreateNomenclatureSecteurDto data) {  // Changed from @RequestBody to @ModelAttribute
        CustomResponseHelper<NomenclatureSecteurEntity> response = CustomResponseHelper
            .<NomenclatureSecteurEntity>builder()
            .body(nomenclatureSecteurImplementation.create(data))
            .message("Nomenclature secteur created successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
    
        return ResponseEntity.status(response.getStatus()).body(response);
            }

    @PutMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponseHelper<NomenclatureSecteurEntity>> update(
            @PathVariable Long id,
            @ModelAttribute UpdateNomenclatureSecteurDto data) {
        CustomResponseHelper<NomenclatureSecteurEntity> response = CustomResponseHelper
            .<NomenclatureSecteurEntity>builder()
            .body(nomenclatureSecteurImplementation.update(id, data))
            .message("Nomenclature secteur updated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<NomenclatureSecteurEntity>> getOne(
            @PathVariable Long id) {
        CustomResponseHelper<NomenclatureSecteurEntity> response = CustomResponseHelper
            .<NomenclatureSecteurEntity>builder()
            .body(nomenclatureSecteurImplementation.getOne(id))
            .message("Nomenclature secteur retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<NomenclatureSecteurEntity>>> findAll(
            @RequestParam(required = false) Map<String, String> filters) {
        CustomResponseHelper<List<NomenclatureSecteurEntity>> response = CustomResponseHelper
            .<List<NomenclatureSecteurEntity>>builder()
            .body(nomenclatureSecteurImplementation.findAll(filters))
            .message("Nomenclature secteurs list retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("active")
    public ResponseEntity<CustomResponseHelper<NomenclatureSecteurEntity>> findActive() {
        CustomResponseHelper<NomenclatureSecteurEntity> response = CustomResponseHelper
            .<NomenclatureSecteurEntity>builder()
            .body(nomenclatureSecteurImplementation.findActive())
            .message("Active nomenclature secteur retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("activate/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> activate(@PathVariable Long id) {
        nomenclatureSecteurImplementation.activate(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Nomenclature secteur activated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        nomenclatureSecteurImplementation.delete(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Nomenclature secteur deleted successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("deactivate-all")
    public ResponseEntity<CustomResponseHelper<Void>> deactivateAll() {
        nomenclatureSecteurImplementation.deactivateAll();
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("All nomenclature secteurs deactivated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/download/{id}")
public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
    try {
        Resource fileResource = nomenclatureSecteurImplementation.downloadJustificationFile(id);
        
        // Get the nomenclature to determine content type
        NomenclatureSecteurEntity nomenclature = nomenclatureSecteurImplementation.getOne(id);
        
        // Set the content type
        String contentType = nomenclatureSecteurImplementation.determineContentType(nomenclature.getJustificationPath());
        
        // Build response with file as attachment for download
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