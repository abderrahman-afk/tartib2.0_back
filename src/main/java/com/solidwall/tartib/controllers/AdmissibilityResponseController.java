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
import com.solidwall.tartib.dto.responseadmissibilitygrid.CreateDto;
import com.solidwall.tartib.dto.responseadmissibilitygrid.UpdateDto;
import com.solidwall.tartib.entities.AdmissibilityResponseGridEntity;
import com.solidwall.tartib.implementations.AdmissibilityResponseImplementation;

@RestController
@RequestMapping("admissibility-response")
public class AdmissibilityResponseController {

    @Autowired
    private AdmissibilityResponseImplementation admissibilityResponseImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<AdmissibilityResponseGridEntity>>> findAll() {
        CustomResponseHelper<List<AdmissibilityResponseGridEntity>> response = CustomResponseHelper
                .<List<AdmissibilityResponseGridEntity>>builder()
                .body(admissibilityResponseImplementation.findAll())
                .message("Admissibility responses retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<AdmissibilityResponseGridEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<AdmissibilityResponseGridEntity> response = CustomResponseHelper
                .<AdmissibilityResponseGridEntity>builder()
                .body(admissibilityResponseImplementation.getOne(id))
                .message("Admissibility response retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("find")
    public ResponseEntity<CustomResponseHelper<AdmissibilityResponseGridEntity>> findOne(
            @RequestParam Map<String, String> params) {
        CustomResponseHelper<AdmissibilityResponseGridEntity> response = CustomResponseHelper
                .<AdmissibilityResponseGridEntity>builder()
                .body(admissibilityResponseImplementation.findOne(params))
                .message("Admissibility response found")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<AdmissibilityResponseGridEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<AdmissibilityResponseGridEntity> response = CustomResponseHelper
                .<AdmissibilityResponseGridEntity>builder()
                .body(admissibilityResponseImplementation.create(data))
                .message("Admissibility response created successfully")
                .error(false)
                .status(HttpStatus.CREATED.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<AdmissibilityResponseGridEntity>> update(
            @PathVariable Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<AdmissibilityResponseGridEntity> response = CustomResponseHelper
                .<AdmissibilityResponseGridEntity>builder()
                .body(admissibilityResponseImplementation.update(id, data))
                .message("Admissibility response updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        admissibilityResponseImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Admissibility response deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("verify/{id}")
    public ResponseEntity<CustomResponseHelper<AdmissibilityResponseGridEntity>> verifyAdmissibility(
            @PathVariable Long id) {
        CustomResponseHelper<AdmissibilityResponseGridEntity> response = CustomResponseHelper
                .<AdmissibilityResponseGridEntity>builder()
                .body(admissibilityResponseImplementation.verifyAdmissibility(id))
                .message("Admissibility verification completed")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PostMapping("revaluate/{id}")
    public ResponseEntity<CustomResponseHelper<AdmissibilityResponseGridEntity>> revaluate(@PathVariable Long id) {
        CustomResponseHelper<AdmissibilityResponseGridEntity> response = CustomResponseHelper
                .<AdmissibilityResponseGridEntity>builder()
                .body(admissibilityResponseImplementation.reevaluate(id))
                .message("Admissibility response re-evaluated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("{id}/derogation")
    public ResponseEntity<CustomResponseHelper<AdmissibilityResponseGridEntity>> submitDerogation(
            @PathVariable Long id,
            @RequestBody String derogationText) {
        CustomResponseHelper<AdmissibilityResponseGridEntity> response = CustomResponseHelper
                .<AdmissibilityResponseGridEntity>builder()
                .body(admissibilityResponseImplementation.submitDerogation(id, derogationText))
                .message("Derogation request submitted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/latest-grid-version")
public ResponseEntity<CustomResponseHelper<Integer>> getLatestGridVersion() {
    Integer version = admissibilityResponseImplementation.getLatestGridVersion();
    
    CustomResponseHelper<Integer> response = CustomResponseHelper.<Integer>builder()
        .body(version)
        .message("Latest grid version retrieved successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
        
    return ResponseEntity.status(response.getStatus()).body(response);
}
}