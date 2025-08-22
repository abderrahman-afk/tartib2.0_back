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
import com.solidwall.tartib.dto.access.CreateDto;
import com.solidwall.tartib.dto.access.UpdateDto;
import com.solidwall.tartib.entities.AccessEntity;
import com.solidwall.tartib.implementations.AccessImplementation;

@RestController
@RequestMapping("access")
public class AccessController {

    @Autowired
    AccessImplementation accessImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<AccessEntity>>> findAll() {
        CustomResponseHelper<List<AccessEntity>> response = CustomResponseHelper.<List<AccessEntity>>builder()
                .body(accessImplementation.findAll())
                .message("access list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<AccessEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<AccessEntity> response = CustomResponseHelper.<AccessEntity>builder()
                .body(accessImplementation.getOne(id))
                .message("access data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "find" })
    public ResponseEntity<CustomResponseHelper<AccessEntity>> findOne(@RequestParam Map<String, String> reqParam) {
        CustomResponseHelper<AccessEntity> response = CustomResponseHelper
                .<AccessEntity>builder()
                .body(accessImplementation.findOne(reqParam))
                .message("access found")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<AccessEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<AccessEntity> response = CustomResponseHelper.<AccessEntity>builder()
                .body(accessImplementation.create(data))
                .message("access created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<AccessEntity>> update(@PathVariable("id") Long id, @RequestBody UpdateDto data) {
        CustomResponseHelper<AccessEntity> response = CustomResponseHelper.<AccessEntity>builder()
                .body(accessImplementation.update(id, data))
                .message("access updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        accessImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("access deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
