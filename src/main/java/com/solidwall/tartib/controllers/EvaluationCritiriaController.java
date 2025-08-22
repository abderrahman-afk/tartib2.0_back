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
import com.solidwall.tartib.dto.evaluationcritiria.CreateDto;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;
import com.solidwall.tartib.implementations.EvaluationCritiriaImplementation;

@RestController
@RequestMapping("evaluation-critiria")
public class EvaluationCritiriaController {

    @Autowired
    EvaluationCritiriaImplementation critiriaImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<EvaluationCritiriaEntity>>> findAll() {
        CustomResponseHelper<List<EvaluationCritiriaEntity>> response = CustomResponseHelper.<List<EvaluationCritiriaEntity>>builder()
                .body(critiriaImplementation.findAll())
                .message("Evaluation critirias list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("by-component/{componentId}")
    public ResponseEntity<CustomResponseHelper<List<EvaluationCritiriaEntity>>> findByComponentId(@PathVariable Long componentId) {
        CustomResponseHelper<List<EvaluationCritiriaEntity>> response = CustomResponseHelper.<List<EvaluationCritiriaEntity>>builder()
                .body(critiriaImplementation.findByComponentId(componentId))
                .message("Evaluation critirias for component")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<EvaluationCritiriaEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<EvaluationCritiriaEntity> response = CustomResponseHelper.<EvaluationCritiriaEntity>builder()
                .body(critiriaImplementation.getOne(id))
                .message("Evaluation critiria data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<EvaluationCritiriaEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<EvaluationCritiriaEntity> response = CustomResponseHelper.<EvaluationCritiriaEntity>builder()
                .body(critiriaImplementation.create(data))
                .message("Evaluation critiria created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
 
    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        critiriaImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Evaluation critiria deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}