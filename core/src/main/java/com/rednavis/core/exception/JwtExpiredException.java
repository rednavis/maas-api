package com.rednavis.core.exception;

public class JwtExpiredException extends RuntimeException {

  public JwtExpiredException(String message) {
    super(message);
  }

}
