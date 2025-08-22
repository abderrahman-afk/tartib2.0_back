package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.userrole.CreateDto;
import com.solidwall.tartib.dto.userrole.UpdateDto;
import com.solidwall.tartib.dto.userrole.UserRoleDto;
import com.solidwall.tartib.entities.UserRoleEntity;
import com.solidwall.tartib.implementations.UserRoleImplementation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("user-role")
public class UserRoleController {

  @Autowired
  UserRoleImplementation userRoleImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<UserRoleDto>>> findAll() {
    CustomResponseHelper<List<UserRoleDto>> response = CustomResponseHelper.<List<UserRoleDto>>builder()
        .body(userRoleImplementation.findAll())
        .message("user role list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<UserRoleDto>> getOne(@PathVariable Long id) {
    CustomResponseHelper<UserRoleDto> response = CustomResponseHelper.<UserRoleDto>builder()
        .body(userRoleImplementation.getOne(id))
        .message("user role data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<UserRoleDto>> create(@RequestBody CreateDto data) {
    CustomResponseHelper<UserRoleDto> response = CustomResponseHelper.<UserRoleDto>builder()
        .body(userRoleImplementation.create(data))
        .message("user role created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<UserRoleDto>> update(@PathVariable("id") Long id,
      @RequestBody UpdateDto data) {
    CustomResponseHelper<UserRoleDto> response = CustomResponseHelper.<UserRoleDto>builder()
        .body(userRoleImplementation.update(id, data))
        .message("user role updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    userRoleImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("user role deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "getByUserId/{id}" })
  private ResponseEntity<CustomResponseHelper<UserRoleDto>> getByUserId(@PathVariable Long id) {
    CustomResponseHelper<UserRoleDto> response = CustomResponseHelper.<UserRoleDto>builder()
        .body(userRoleImplementation.getByUserId(id))
        .message("user role data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}