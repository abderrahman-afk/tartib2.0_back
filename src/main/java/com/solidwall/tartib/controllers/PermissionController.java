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
import com.solidwall.tartib.dto.permission.CreateDto;
import com.solidwall.tartib.dto.permission.UpdateDto;
import com.solidwall.tartib.entities.PermissionEntity;
import com.solidwall.tartib.implementations.PermissionImplementation;

@RestController
@RequestMapping("permission")
public class PermissionController {

    @Autowired
    private PermissionImplementation permissionImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<PermissionEntity>>> findAll() {
        CustomResponseHelper<List<PermissionEntity>> response = CustomResponseHelper.<List<PermissionEntity>>builder()
                .body(permissionImplementation.findAll())
                .message("permission list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<PermissionEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<PermissionEntity> response = CustomResponseHelper.<PermissionEntity>builder()
                .body(permissionImplementation.getOne(id))
                .message("permission data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "find" })
    public ResponseEntity<CustomResponseHelper<PermissionEntity>> findOne(@RequestParam Map<String, String> reqParam) {
        CustomResponseHelper<PermissionEntity> response = CustomResponseHelper
                .<PermissionEntity>builder()
                .body(permissionImplementation.findOne(reqParam))
                .message("permission found")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<PermissionEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<PermissionEntity> response = CustomResponseHelper.<PermissionEntity>builder()
                .body(permissionImplementation.create(data))
                .message("permission created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<PermissionEntity>> update(@PathVariable("id") Long id, @RequestBody UpdateDto data) {
        CustomResponseHelper<PermissionEntity> response = CustomResponseHelper.<PermissionEntity>builder()
                .body(permissionImplementation.update(id, data))
                .message("permission updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        permissionImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("permission deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
