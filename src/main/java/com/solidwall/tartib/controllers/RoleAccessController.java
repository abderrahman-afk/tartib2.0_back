package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.roleAccess.CreateDto;
import com.solidwall.tartib.dto.roleAccess.FindDto;
import com.solidwall.tartib.dto.roleAccess.UpdateDto;
import com.solidwall.tartib.entities.RoleAccessEntity;
import com.solidwall.tartib.implementations.RoleAccessImplementation;

@Controller
@RequestMapping("role-access")
public class RoleAccessController {

    @Autowired
    RoleAccessImplementation roleAccessImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<RoleAccessEntity>>> findAll() {
        CustomResponseHelper<List<RoleAccessEntity>> response = CustomResponseHelper.<List<RoleAccessEntity>>builder()
                .body(roleAccessImplementation.findAll())
                .message("role access list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<RoleAccessEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<RoleAccessEntity> response = CustomResponseHelper.<RoleAccessEntity>builder()
                .body(roleAccessImplementation.getOne(id))
                .message("role access data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "find" })
    public ResponseEntity<CustomResponseHelper<RoleAccessEntity>> findOne(FindDto reqParam) {
        CustomResponseHelper<RoleAccessEntity> response = CustomResponseHelper
                .<RoleAccessEntity>builder()
                .body(roleAccessImplementation.findOne(reqParam))
                .message("role access found")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<List<RoleAccessEntity>>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<List<RoleAccessEntity>> response = CustomResponseHelper.<List<RoleAccessEntity>>builder()
                .body(roleAccessImplementation.create(data))
                .message("role access created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<List<RoleAccessEntity>>> update(@PathVariable("id") Long id, @RequestBody UpdateDto data) {
        CustomResponseHelper<List<RoleAccessEntity>> response = CustomResponseHelper.<List<RoleAccessEntity>>builder()
                .body(roleAccessImplementation.update(id, data))
                .message("role access updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        roleAccessImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("access deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
