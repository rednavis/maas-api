package com.rednavis.core.exception;

public class JwtRefreshTokenExpiredException extends JwtException {

  public JwtRefreshTokenExpiredException(String message) {
    super(message);
  }

}
