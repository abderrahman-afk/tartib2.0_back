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
import com.solidwall.tartib.dto.component.CreateDto;
import com.solidwall.tartib.dto.component.UpdateDto;
import com.solidwall.tartib.entities.ComponentEntity;
import com.solidwall.tartib.implementations.ComponentImplementation;

@RestController
@RequestMapping("component")
public class ComponentController {

    @Autowired
    ComponentImplementation componentImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<ComponentEntity>>> findAll() {
        CustomResponseHelper<List<ComponentEntity>> response = CustomResponseHelper.<List<ComponentEntity>>builder()
                .body(componentImplementation.findAll())
                .message("component list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<ComponentEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<ComponentEntity> response = CustomResponseHelper.<ComponentEntity>builder()
                .body(componentImplementation.getOne(id))
                .message("component data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "find" })
    public ResponseEntity<CustomResponseHelper<ComponentEntity>> findOne(@RequestParam Map<String, String> reqParam) {
        CustomResponseHelper<ComponentEntity> response = CustomResponseHelper
                .<ComponentEntity>builder()
                .body(componentImplementation.findOne(reqParam))
                .message("component found")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<ComponentEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<ComponentEntity> response = CustomResponseHelper.<ComponentEntity>builder()
                .body(componentImplementation.create(data))
                .message("component created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<ComponentEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<ComponentEntity> response = CustomResponseHelper.<ComponentEntity>builder()
                .body(componentImplementation.update(id, data))
                .message("component updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        componentImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("component deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
