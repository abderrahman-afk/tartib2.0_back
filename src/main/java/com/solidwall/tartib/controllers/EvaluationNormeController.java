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
import com.solidwall.tartib.dto.evaluationnorme.CreateDto;
import com.solidwall.tartib.entities.EvaluationNormeEntity;
import com.solidwall.tartib.implementations.EvaluationNormeImplementation;

@RestController
@RequestMapping("evaluation-norme")
public class EvaluationNormeController {

    @Autowired
    EvaluationNormeImplementation normeImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<EvaluationNormeEntity>>> findAll() {
        CustomResponseHelper<List<EvaluationNormeEntity>> response = CustomResponseHelper.<List<EvaluationNormeEntity>>builder()
                .body(normeImplementation.findAll())
                .message("Evaluation normes list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("by-indicateur/{indicateurId}")
    public ResponseEntity<CustomResponseHelper<List<EvaluationNormeEntity>>> findByIndicateurId(@PathVariable Long indicateurId) {
        CustomResponseHelper<List<EvaluationNormeEntity>> response = CustomResponseHelper.<List<EvaluationNormeEntity>>builder()
                .body(normeImplementation.findByIndicateurId(indicateurId))
                .message("Evaluation normes for indicateur")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<EvaluationNormeEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<EvaluationNormeEntity> response = CustomResponseHelper.<EvaluationNormeEntity>builder()
                .body(normeImplementation.getOne(id))
                .message("Evaluation norme data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<EvaluationNormeEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<EvaluationNormeEntity> response = CustomResponseHelper.<EvaluationNormeEntity>builder()
                .body(normeImplementation.create(data))
                .message("Evaluation norme created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

  

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        normeImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Evaluation norme deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}