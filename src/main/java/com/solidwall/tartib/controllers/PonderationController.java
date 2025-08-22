package com.solidwall.tartib.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.ponderation.CreateDto;
import com.solidwall.tartib.dto.ponderation.UpdateDto;
import com.solidwall.tartib.entities.PonderationEntity;
import com.solidwall.tartib.implementations.PonderationImplementation;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("ponderation")
public class PonderationController {

    @Autowired
    PonderationImplementation ponderationImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<PonderationEntity>>> findAll() {
        CustomResponseHelper<List<PonderationEntity>> response = CustomResponseHelper.<List<PonderationEntity>>builder()
                .body(ponderationImplementation.findAll())
                .message("Ponderations list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<PonderationEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<PonderationEntity> response = CustomResponseHelper.<PonderationEntity>builder()
                .body(ponderationImplementation.getOne(id))
                .message("Ponderation data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<PonderationEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<PonderationEntity> response = CustomResponseHelper.<PonderationEntity>builder()
                .body(ponderationImplementation.create(data))
                .message("Ponderation created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<PonderationEntity>> update(
            @PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<PonderationEntity> response = CustomResponseHelper.<PonderationEntity>builder()
                .body(ponderationImplementation.update(id, data))
                .message("Ponderation updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("activate/{gridid}")
    public ResponseEntity<CustomResponseHelper<PonderationEntity>> update(
            @PathVariable("gridid") Long gridid,
            @RequestBody boolean status) {
        CustomResponseHelper<PonderationEntity> response = CustomResponseHelper.<PonderationEntity>builder()
                .body(ponderationImplementation.activatePonderation(gridid, status))
                .message("Ponderation status updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        ponderationImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Ponderation deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("grid/{gridId}")
    public ResponseEntity<CustomResponseHelper<PonderationEntity>> getByGridId(@PathVariable Long gridId) {
        CustomResponseHelper<PonderationEntity> response = CustomResponseHelper.<PonderationEntity>builder()
                .body(ponderationImplementation.getByEvaluationGridId(gridId))
                .message("Ponderation data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}