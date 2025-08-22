package com.solidwall.tartib.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.nomenclatureGeographic.*;
import com.solidwall.tartib.entities.NomenclatureGeographicEntity;
import com.solidwall.tartib.implementations.NomenclatureGeographicImplementation;

import java.util.List;
import java.util.Map;
import com.solidwall.tartib.dto.classementPortfolio.ClassificationResponseDto;
import com.solidwall.tartib.dto.classementPortfolio.CreateClassificationDto;
import com.solidwall.tartib.dto.classementPortfolio.UpdateClassificationDto;
import com.solidwall.tartib.implementations.ClassificationImplementation;

@RestController
@RequestMapping("classification")
 public class ClassificationController {

    @Autowired
    private ClassificationImplementation classificationImplementation;

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<CustomResponseHelper<ClassificationResponseDto>> create(
            @ModelAttribute CreateClassificationDto data) {
        
        // Create the classification and wrap in custom response
        CustomResponseHelper<ClassificationResponseDto> response = CustomResponseHelper
            .<ClassificationResponseDto>builder()
            .body(classificationImplementation.create(data))
            .message("Classification created successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<CustomResponseHelper<ClassificationResponseDto>> update(
            @PathVariable Long id,
            @ModelAttribute UpdateClassificationDto data) {
        
        CustomResponseHelper<ClassificationResponseDto> response = CustomResponseHelper
            .<ClassificationResponseDto>builder()
            .body(classificationImplementation.update(id, data))
            .message("Classification updated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
     public ResponseEntity<CustomResponseHelper<ClassificationResponseDto>> getOne(
            @PathVariable Long id) {
        
        CustomResponseHelper<ClassificationResponseDto> response = CustomResponseHelper
            .<ClassificationResponseDto>builder()
            .body(classificationImplementation.getOne(id))
            .message("Classification retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("all")
     public ResponseEntity<CustomResponseHelper<List<ClassificationResponseDto>>> findAll(
            @RequestParam(required = false) Map<String, String> filters) {
        
        CustomResponseHelper<List<ClassificationResponseDto>> response = CustomResponseHelper
            .<List<ClassificationResponseDto>>builder()
            .body(classificationImplementation.findAll(filters))
            .message("Classifications list retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("active")
    public ResponseEntity<CustomResponseHelper<ClassificationResponseDto>> findActive() {
        
        CustomResponseHelper<ClassificationResponseDto> response = CustomResponseHelper
            .<ClassificationResponseDto>builder()
            .body(classificationImplementation.findActive())
            .message("Active classification retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("activate/{id}")
     public ResponseEntity<CustomResponseHelper<Void>> activate(@PathVariable Long id) {
        classificationImplementation.activate(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Classification activated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
     public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        classificationImplementation.delete(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Classification deleted successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("deactivate-all")
    public ResponseEntity<CustomResponseHelper<Void>> deactivateAll() {
        classificationImplementation.deactivateAll();
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("All classifications deactivated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}