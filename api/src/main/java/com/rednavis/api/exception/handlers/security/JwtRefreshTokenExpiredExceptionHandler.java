package com.rednavis.api.exception.handlers.security;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import com.rednavis.core.exception.JwtRefreshTokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class JwtRefreshTokenExpiredExceptionHandler extends AbstractExceptionHandler<JwtRefreshTokenExpiredException> {

  /**
   * JwtRefreshTokenExpiredExceptionHandler.
   */
  public JwtRefreshTokenExpiredExceptionHandler() {
    super(JwtRefreshTokenExpiredException.class);
    log.info("JwtRefreshTokenExpiredExceptionHandler created");
  }

  @Override
  public HttpStatus getStatus(JwtRefreshTokenExpiredException ex) {
    return HttpStatus.UNAUTHORIZED;
  }
}
