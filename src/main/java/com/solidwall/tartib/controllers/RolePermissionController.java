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
import com.solidwall.tartib.dto.rolePermission.CreateDto;
import com.solidwall.tartib.dto.rolePermission.UpdateDto;
import com.solidwall.tartib.entities.RolePermissionEntity;
import com.solidwall.tartib.implementations.RolePermissionImplementation;

@RestController
@RequestMapping("role-permission")
public class RolePermissionController {

    @Autowired
    RolePermissionImplementation rolePermissionImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<RolePermissionEntity>>> findAll() {
        CustomResponseHelper<List<RolePermissionEntity>> response = CustomResponseHelper.<List<RolePermissionEntity>>builder()
                .body(rolePermissionImplementation.findAll())
                .message("role permission list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<RolePermissionEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<RolePermissionEntity> response = CustomResponseHelper.<RolePermissionEntity>builder()
                .body(rolePermissionImplementation.getOne(id))
                .message("role permission data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "find" })
    public ResponseEntity<CustomResponseHelper<RolePermissionEntity>> findOne(@RequestParam Map<String, String> reqParam) {
        CustomResponseHelper<RolePermissionEntity> response = CustomResponseHelper
                .<RolePermissionEntity>builder()
                .body(rolePermissionImplementation.findOne(reqParam))
                .message("role permission found")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<List<RolePermissionEntity>>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<List<RolePermissionEntity>> response = CustomResponseHelper.<List<RolePermissionEntity>>builder()
                .body(rolePermissionImplementation.create(data))
                .message("role permission created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<List<RolePermissionEntity>>> update(@PathVariable("id") Long id, @RequestBody UpdateDto data) {
        CustomResponseHelper<List<RolePermissionEntity>> response = CustomResponseHelper.<List<RolePermissionEntity>>builder()
                .body(rolePermissionImplementation.update(id, data))
                .message("role permission updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        rolePermissionImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("role permission deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
