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
import com.solidwall.tartib.dto.roleAssignableRole.AssignableRolesResponse;
import com.solidwall.tartib.dto.roleAssignableRole.CreateDto;
import com.solidwall.tartib.dto.roleAssignableRole.UpdateDto;
import com.solidwall.tartib.entities.RoleAssignableRoleEntity;
import com.solidwall.tartib.implementations.RoleAssignableRoleImplementation;

@RestController
@RequestMapping("role-assignable-roles")
public class RoleAssignableRoleController {

    @Autowired
    private RoleAssignableRoleImplementation roleAssignableRoleImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<RoleAssignableRoleEntity>>> findAll() {
        CustomResponseHelper<List<RoleAssignableRoleEntity>> response = CustomResponseHelper
            .<List<RoleAssignableRoleEntity>>builder()
            .body(roleAssignableRoleImplementation.findAll())
            .message("Role assignable roles list")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("by-role/{roleId}")
    public ResponseEntity<CustomResponseHelper<List<RoleAssignableRoleEntity>>> findByRoleId(@PathVariable Long roleId) {
        CustomResponseHelper<List<RoleAssignableRoleEntity>> response = CustomResponseHelper
            .<List<RoleAssignableRoleEntity>>builder()
            .body(roleAssignableRoleImplementation.findByRoleId(roleId))
            .message("Role assignable roles by role")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
    @GetMapping("assignable-role-ids/{roleId}")
    public ResponseEntity<CustomResponseHelper<List<Long>>> getAssignableRoleIds(@PathVariable Long roleId) {
        CustomResponseHelper<List<Long>> response = CustomResponseHelper
            .<List<Long>>builder()
            .body(roleAssignableRoleImplementation.getAssignableRoleIds(roleId))
            .message("Assignable role IDs for role")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
    @GetMapping("assignable-role-ids-for-current-user")
    public ResponseEntity<CustomResponseHelper<List<Long>>> getAssignableRoleIdsForCurrentUser() {
        CustomResponseHelper<List<Long>> response = CustomResponseHelper
            .<List<Long>>builder()
            .body(roleAssignableRoleImplementation.getAssignableRoleIdsForCurrentUser())
            .message("Assignable role IDs for current user")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<List<RoleAssignableRoleEntity>>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<List<RoleAssignableRoleEntity>> response = CustomResponseHelper
            .<List<RoleAssignableRoleEntity>>builder()
            .body(roleAssignableRoleImplementation.create(data))
            .message("Role assignable roles created successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{roleId}")
    public ResponseEntity<CustomResponseHelper<List<RoleAssignableRoleEntity>>> update(
            @PathVariable Long roleId, @RequestBody UpdateDto data) {
        CustomResponseHelper<List<RoleAssignableRoleEntity>> response = CustomResponseHelper
            .<List<RoleAssignableRoleEntity>>builder()
            .body(roleAssignableRoleImplementation.update(roleId, data))
            .message("Role assignable roles updated successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        roleAssignableRoleImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Role assignable role deleted successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete-by-role/{roleId}")
    public ResponseEntity<CustomResponseHelper<Void>> deleteByRoleId(@PathVariable Long roleId) {
        roleAssignableRoleImplementation.deleteByRoleId(roleId);
        CustomResponseHelper<Void> response = CustomResponseHelper
            .<Void>builder()
            .message("Role assignable roles deleted by role successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}