package com.solidwall.tartib.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.role.RoleDto;
import com.solidwall.tartib.entities.RoleAssignableRoleEntity;
import com.solidwall.tartib.entities.RoleEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.entities.UserRoleEntity;
import com.solidwall.tartib.implementations.RoleImplementation;
import com.solidwall.tartib.repositories.RoleAssignableRoleRepository;
import com.solidwall.tartib.repositories.UserRepository;

@RestController
@RequestMapping("role")
public class RoleController {

  @Autowired
  RoleImplementation roleImplementation;
  @Autowired
    RoleAssignableRoleRepository roleAssignableRoleRepository;
  @Autowired
    UserRepository userRepository;
  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<RoleDto>>> findAll() {
    CustomResponseHelper<List<RoleDto>> response = CustomResponseHelper.<List<RoleDto>>builder()
        .body(roleImplementation.findAll())
        .message("role list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<RoleDto>> getOne(@PathVariable Long id) {
    CustomResponseHelper<RoleDto> response = CustomResponseHelper.<RoleDto>builder()
        .body(roleImplementation.getOne(id))
        .message("role information")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<RoleEntity>> create(@RequestBody RoleEntity role) {
    CustomResponseHelper<RoleEntity> response = CustomResponseHelper.<RoleEntity>builder()
        .body(roleImplementation.create(role))
        .message("role created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<RoleEntity>> update(@PathVariable("id") Long id,
      @RequestBody RoleEntity role) {
    CustomResponseHelper<RoleEntity> response = CustomResponseHelper.<RoleEntity>builder()
        .body(roleImplementation.update(id, role))
        .message("role updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    roleImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("role deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
@GetMapping("assignable-roles-for-current-user")
public ResponseEntity<CustomResponseHelper<List<RoleEntity>>> getAssignableRolesForCurrentUser() {
    // Get the current authenticated user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    
    // Find the user
    UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found"));
    
    // Get the user's roles
    List<RoleEntity> userRoles = user.getUserRoles().stream()
            .map(UserRoleEntity::getRole)
            .collect(Collectors.toList());
    
    // Get the assignable roles for each of the user's roles
    Set<RoleEntity> assignableRoles = new HashSet<>();
    for (RoleEntity role : userRoles) {
        List<RoleAssignableRoleEntity> roleAssignableRoles = roleAssignableRoleRepository.findByRole(role);
        for (RoleAssignableRoleEntity roleAssignableRole : roleAssignableRoles) {
            assignableRoles.add(roleAssignableRole.getAssignableRole());
        }
    }
    
    // Convert to list
    List<RoleEntity> assignableRolesList = new ArrayList<>(assignableRoles);
    
    CustomResponseHelper<List<RoleEntity>> response = CustomResponseHelper
            .<List<RoleEntity>>builder()
            .body(assignableRolesList)
            .message("Assignable roles for current user")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();
    
    return ResponseEntity.status(response.getStatus()).body(response);
}
}
