package com.solidwall.tartib.core.handlers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.core.exceptions.UnauthorisedException;
import com.solidwall.tartib.core.helpers.CustomErrorHelper;

@RestControllerAdvice
public class AppExceptionHandler {

  @ExceptionHandler(value = { UnauthorisedException.class })
  public ResponseEntity<Object> servletException(UnauthorisedException unauthorisedException) {
    CustomErrorHelper customErrorHelper = CustomErrorHelper.builder()
        .message(unauthorisedException.getMessage())
        .status(HttpStatus.UNAUTHORIZED.value())
        .error(true)
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(customErrorHelper, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = { NotFoundException.class })
  public ResponseEntity<Object> notFoundException(NotFoundException notFoundException) {

    CustomErrorHelper customErrorHelper = CustomErrorHelper.builder()
        .message(notFoundException.getMessage())
        .status(HttpStatus.NOT_FOUND.value())
        .error(true)
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(customErrorHelper, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = { FoundException.class })
  public ResponseEntity<Object> foundException(FoundException foundException) {

    CustomErrorHelper customErrorHelper = CustomErrorHelper.builder()
        .message(foundException.getMessage())
        .status(HttpStatus.FOUND.value())
        .error(true)
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(customErrorHelper, HttpStatus.FOUND);
  }

  @ExceptionHandler(value = { BadRequestException.class })
  public ResponseEntity<Object> badRequestException(BadRequestException badRequestException) {

    CustomErrorHelper customErrorHelper = CustomErrorHelper.builder()
        .message(badRequestException.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(true)
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(customErrorHelper, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Object> HandleMethodArgumentNotValid(
      MethodArgumentNotValidException methodArgumentNotValidException) {

    Map<String, String> errors = new HashMap<>();
    methodArgumentNotValidException.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    CustomErrorHelper customErrorHelper = CustomErrorHelper.builder()
        .message(errors.values().toArray()[0].toString())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(true)
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(customErrorHelper, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = { RuntimeException.class })
  public ResponseEntity<Object> runtimeException(RuntimeException runtimeException) {

    CustomErrorHelper customErrorHelper = CustomErrorHelper.builder()
        .message(runtimeException.getMessage())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(true)
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(customErrorHelper, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}