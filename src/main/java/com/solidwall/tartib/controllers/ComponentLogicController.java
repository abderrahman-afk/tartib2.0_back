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
import com.solidwall.tartib.dto.componentlogic.CreateDto;
import com.solidwall.tartib.dto.componentlogic.UpdateDto;
import com.solidwall.tartib.entities.ComponentLogicEntity;
import com.solidwall.tartib.implementations.ComponentLogicImplementation;

@RestController
@RequestMapping("component-logic")
public class ComponentLogicController {
    
    @Autowired
    ComponentLogicImplementation componentLogicImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<ComponentLogicEntity>>> findAll() {
        CustomResponseHelper<List<ComponentLogicEntity>> response = CustomResponseHelper.<List<ComponentLogicEntity>>builder()
                .body(componentLogicImplementation.findAll())
                .message("ComponentLogic  list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<ComponentLogicEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<ComponentLogicEntity> response = CustomResponseHelper.<ComponentLogicEntity>builder()
                .body(componentLogicImplementation.getOne(id))
                .message("ComponentLogic  data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<ComponentLogicEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<ComponentLogicEntity> response = CustomResponseHelper.<ComponentLogicEntity>builder()
                .body(componentLogicImplementation.create(data))
                .message("ComponentLogic  created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<ComponentLogicEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<ComponentLogicEntity> response = CustomResponseHelper.<ComponentLogicEntity>builder()
                .body(componentLogicImplementation.update(id, data))
                .message("ComponentLogic  updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        componentLogicImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("ComponentLogic  deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
