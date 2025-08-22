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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.classificationGeneration.GenerateClassificationDto;
import com.solidwall.tartib.dto.classificationGeneration.GeneratedClassificationResponseDto;
import com.solidwall.tartib.implementations.ClassificationGenerationImplementation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("classificationgeneration")
public class ClassificationGenerationController {

    @Autowired
    private ClassificationGenerationImplementation classificationGenerationImplementation;

    @PostMapping("generate")
    public ResponseEntity<CustomResponseHelper<GeneratedClassificationResponseDto>> generateClassification(
            @RequestBody GenerateClassificationDto data) {
        
        CustomResponseHelper<GeneratedClassificationResponseDto> response = CustomResponseHelper
            .<GeneratedClassificationResponseDto>builder()
            .body(classificationGenerationImplementation.generateClassification(data))
            .message("Classification generated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<GeneratedClassificationResponseDto>>> findAll(
            @RequestParam(required = false) Map<String, String> filters) {
        
        CustomResponseHelper<List<GeneratedClassificationResponseDto>> response = CustomResponseHelper
            .<List<GeneratedClassificationResponseDto>>builder()
            .body(classificationGenerationImplementation.findAll(filters))
            .message("Generated classifications retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
   @GetMapping("latest-by-system/{classificationSystemId}")
    public ResponseEntity<CustomResponseHelper<GeneratedClassificationResponseDto>> findLatestByClassificationSystem(
            @PathVariable Long classificationSystemId) {
        
        CustomResponseHelper<GeneratedClassificationResponseDto> response = CustomResponseHelper
            .<GeneratedClassificationResponseDto>builder()
            .body(classificationGenerationImplementation.findLatestByClassificationSystem(classificationSystemId))
            .message("Latest generated classification for system retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<GeneratedClassificationResponseDto>> getOne(
            @PathVariable Long id) {
        
        CustomResponseHelper<GeneratedClassificationResponseDto> response = CustomResponseHelper
            .<GeneratedClassificationResponseDto>builder()
            .body(classificationGenerationImplementation.getOne(id))
            .message("Generated classification retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("by-system/{classificationSystemId}")
    public ResponseEntity<CustomResponseHelper<List<GeneratedClassificationResponseDto>>> findByClassificationSystem(
            @PathVariable Long classificationSystemId) {
        
        CustomResponseHelper<List<GeneratedClassificationResponseDto>> response = CustomResponseHelper
            .<List<GeneratedClassificationResponseDto>>builder()
            .body(classificationGenerationImplementation.findByClassificationSystem(classificationSystemId))
            .message("Generated classifications for system retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        classificationGenerationImplementation.delete(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Generated classification deleted successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}