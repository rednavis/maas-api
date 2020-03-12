package com.rednavis.core.exception;

public class JwtAccessTokenExpiredException extends JwtException {

  public JwtAccessTokenExpiredException(String message) {
    super(message);
  }

}
