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
import com.solidwall.tartib.dto.CreateUserDto;
import com.solidwall.tartib.dto.user.ChangePasswordDto;
import com.solidwall.tartib.dto.user.UserProfileDto;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.implementations.UserImplementation;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  UserImplementation userImplementation;
  @Autowired
  AuthenticationFacade authenticationFacade;

  @Autowired
  UserService userService;

  @GetMapping("profile")
  public ResponseEntity<CustomResponseHelper<UserProfileDto>> getCurrentUserProfile() {
    UserEntity currentUser = authenticationFacade.getCurrentUser();
    UserProfileDto profileDto = userService.getUserProfile(currentUser);
    CustomResponseHelper<UserProfileDto> response = CustomResponseHelper.<UserProfileDto>builder()
        .body(profileDto)
        .message("current user profile")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<UserEntity>>> findAll() {
    CustomResponseHelper<List<UserEntity>> response = CustomResponseHelper.<List<UserEntity>>builder()
        .body(userImplementation.findAll())
        .message("user list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<UserEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<UserEntity> response = CustomResponseHelper.<UserEntity>builder()
        .body(userImplementation.getOne(id))
        .message("user data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<UserEntity>> create(@RequestBody CreateUserDto  user) {
    CustomResponseHelper<UserEntity> response = CustomResponseHelper.<UserEntity>builder()
        .body(userImplementation.create(user))
        .message("user created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<UserEntity>> update(@PathVariable("id") Long id,
      @RequestBody CreateUserDto  user) {
    CustomResponseHelper<UserEntity> response = CustomResponseHelper.<UserEntity>builder()
        .body(userImplementation.update(id, user))
        .message("user updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    userImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("user deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
  @PostMapping("change-password")
  public ResponseEntity<CustomResponseHelper<Void>> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
    try {
      UserEntity currentUser = authenticationFacade.getCurrentUser();
      userService.changePassword(currentUser, changePasswordDto);
      
      CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
          .message("Password changed successfully")
          .error(false)
          .status(HttpStatus.OK.value())
          .timestamp(new Date())
          .build();
      return ResponseEntity.status(response.getStatus()).body(response);
    } catch (RuntimeException e) {
      CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
          .message(e.getMessage())
          .error(true)
          .status(HttpStatus.BAD_REQUEST.value())
          .timestamp(new Date())
          .build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
  }

}
