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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.gridbonus.CreateGridBonusDto;
import com.solidwall.tartib.dto.gridbonus.GridBonusResponseDto;
import com.solidwall.tartib.implementations.GridBonusImplementation;

@RestController
@RequestMapping("grid-bonus")
public class GridBonusController {

    @Autowired
    private GridBonusImplementation gridBonusService;

    @PostMapping(value = "create")
    public ResponseEntity<CustomResponseHelper<GridBonusResponseDto>> create(
            @RequestBody CreateGridBonusDto data) {
        CustomResponseHelper<GridBonusResponseDto> response = CustomResponseHelper
            .<GridBonusResponseDto>builder()
            .body(gridBonusService.create(data))
            .message("Grid bonus created successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<GridBonusResponseDto>> update(
            @PathVariable Long id,
            @RequestBody CreateGridBonusDto data) {
        CustomResponseHelper<GridBonusResponseDto> response = CustomResponseHelper
            .<GridBonusResponseDto>builder()
            .body(gridBonusService.update(id, data))
            .message("Grid bonus updated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<GridBonusResponseDto>> getOne(
            @PathVariable Long id) {
        CustomResponseHelper<GridBonusResponseDto> response = CustomResponseHelper
            .<GridBonusResponseDto>builder()
            .body(gridBonusService.getOne(id))
            .message("Grid bonus retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("classification/{classificationId}")
    public ResponseEntity<CustomResponseHelper<List<GridBonusResponseDto>>> findByClassification(
            @PathVariable Long classificationId) {
        CustomResponseHelper<List<GridBonusResponseDto>> response = CustomResponseHelper
            .<List<GridBonusResponseDto>>builder()
            .body(gridBonusService.findByClassification(classificationId))
            .message("Grid bonuses for classification retrieved successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        gridBonusService.delete(id);
        
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Grid bonus deleted successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}