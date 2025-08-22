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
import com.solidwall.tartib.dto.evaluationindicateur.CreateDto;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;
import com.solidwall.tartib.implementations.EvaluationIndicateurImplementation;

@RestController
@RequestMapping("evaluation-indicateur")
public class EvaluationIndicateurController {

    @Autowired
    EvaluationIndicateurImplementation indicateurImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<EvaluationIndicateurEntity>>> findAll() {
        CustomResponseHelper<List<EvaluationIndicateurEntity>> response = CustomResponseHelper.<List<EvaluationIndicateurEntity>>builder()
                .body(indicateurImplementation.findAll())
                .message("Evaluation indicateurs list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("by-critiria/{critiriaId}")
    public ResponseEntity<CustomResponseHelper<List<EvaluationIndicateurEntity>>> findByCritiriaId(@PathVariable Long critiriaId) {
        CustomResponseHelper<List<EvaluationIndicateurEntity>> response = CustomResponseHelper.<List<EvaluationIndicateurEntity>>builder()
                .body(indicateurImplementation.findByCritiriaId(critiriaId))
                .message("Evaluation indicateurs for critiria")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<EvaluationIndicateurEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<EvaluationIndicateurEntity> response = CustomResponseHelper.<EvaluationIndicateurEntity>builder()
                .body(indicateurImplementation.getOne(id))
                .message("Evaluation indicateur data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<EvaluationIndicateurEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<EvaluationIndicateurEntity> response = CustomResponseHelper.<EvaluationIndicateurEntity>builder()
                .body(indicateurImplementation.create(data))
                .message("Evaluation indicateur created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

 

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        indicateurImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Evaluation indicateur deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
