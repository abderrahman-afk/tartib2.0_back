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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.evaluationcomponent.CreateDto;
import com.solidwall.tartib.entities.EvaluationComponentEntity;
import com.solidwall.tartib.implementations.EvaluationComponentImplementation;

@RestController
@RequestMapping("evaluation-component")
public class EvaluationComponentController {

    @Autowired
    EvaluationComponentImplementation componentImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<EvaluationComponentEntity>>> findAll() {
        CustomResponseHelper<List<EvaluationComponentEntity>> response = CustomResponseHelper.<List<EvaluationComponentEntity>>builder()
                .body(componentImplementation.findAll())
                .message("Evaluation components list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("by-grid/{gridId}")
    public ResponseEntity<CustomResponseHelper<List<EvaluationComponentEntity>>> findByGridId(@PathVariable Long gridId) {
        CustomResponseHelper<List<EvaluationComponentEntity>> response = CustomResponseHelper.<List<EvaluationComponentEntity>>builder()
                .body(componentImplementation.findByGridId(gridId))
                .message("Evaluation components for grid")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<EvaluationComponentEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<EvaluationComponentEntity> response = CustomResponseHelper.<EvaluationComponentEntity>builder()
                .body(componentImplementation.getOne(id))
                .message("Evaluation component data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<EvaluationComponentEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<EvaluationComponentEntity> response = CustomResponseHelper.<EvaluationComponentEntity>builder()
                .body(componentImplementation.create(data))
                .message("Evaluation component created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

   
    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        componentImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Evaluation component deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

