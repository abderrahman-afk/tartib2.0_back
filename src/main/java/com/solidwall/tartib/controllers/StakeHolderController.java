package com.solidwall.tartib.controllers;

import java.util.List;
import java.util.Date;

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
import com.solidwall.tartib.dto.stakeholder.CreateDto;
import com.solidwall.tartib.dto.stakeholder.UpdateDto;

import com.solidwall.tartib.entities.StakeHolderEntity;
import com.solidwall.tartib.implementations.StakeHolderImplementation;
@RestController
@RequestMapping("stakeholder")
public class StakeHolderController {
    @Autowired
    StakeHolderImplementation StakeHolderImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<StakeHolderEntity>>> findAll() {
        CustomResponseHelper<List<StakeHolderEntity>> response = CustomResponseHelper.<List<StakeHolderEntity>>builder()
                .body(StakeHolderImplementation.findAll())
                .message("StakeHolder list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<StakeHolderEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<StakeHolderEntity> response = CustomResponseHelper.<StakeHolderEntity>builder()
                .body(StakeHolderImplementation.getOne(id))
                .message("StakeHolder data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<StakeHolderEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<StakeHolderEntity> response = CustomResponseHelper.<StakeHolderEntity>builder()
                .body(StakeHolderImplementation.create(data))
                .message("StakeHolder created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<StakeHolderEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<StakeHolderEntity> response = CustomResponseHelper.<StakeHolderEntity>builder()
                .body(StakeHolderImplementation.update(id, data))
                .message("StakeHolder updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        StakeHolderImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("StakeHolder deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
