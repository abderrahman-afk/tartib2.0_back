package com.solidwall.tartib.core.exceptions;

public class UnauthorisedException extends RuntimeException {

  public UnauthorisedException() {
  }

  public UnauthorisedException(String message) {
    super(message);
  }

}
