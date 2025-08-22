package com.solidwall.tartib.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dao.AuthDao;
import com.solidwall.tartib.dto.auth.SigninDto;
import com.solidwall.tartib.dto.auth.TokenDto;
import com.solidwall.tartib.implementations.AuthImplementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {

  @Autowired
  AuthImplementation authImplementation;

  @PostMapping("signin")
  public ResponseEntity<CustomResponseHelper<AuthDao>> signin(@Valid @RequestBody SigninDto signinDto,
      HttpServletRequest request) {
    CustomResponseHelper<AuthDao> response = CustomResponseHelper.<AuthDao>builder()
        .body(authImplementation.signin(signinDto, request))
        .message("user signin successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("signin-with-token")
  public ResponseEntity<CustomResponseHelper<AuthDao>> signinWithToken(@RequestBody TokenDto tokenDto,
      HttpServletRequest request) {
    CustomResponseHelper<AuthDao> response = CustomResponseHelper.<AuthDao>builder()
        .body(authImplementation.signinWithToken(tokenDto.getAccessToken(), request))
        .message("user signin successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("signout")
  public ResponseEntity<CustomResponseHelper<Boolean>> signout(@RequestBody TokenDto tokenDto) {
    CustomResponseHelper<Boolean> response = CustomResponseHelper.<Boolean>builder()
        .body(authImplementation.signout(tokenDto.getAccessToken()))
        .message("user signed out successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("refresh-token")
  public ResponseEntity<CustomResponseHelper<AuthDao>> refreshToken(@RequestBody TokenDto tokenDto,
      HttpServletRequest request) {
    CustomResponseHelper<AuthDao> response = CustomResponseHelper.<AuthDao>builder()
        .body(authImplementation.refreshToken(tokenDto.getAccessToken(), request))
        .message("Token refreshed successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
