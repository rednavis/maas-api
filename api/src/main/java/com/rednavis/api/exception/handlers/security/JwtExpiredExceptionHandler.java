package com.rednavis.api.exception.handlers.security;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import com.rednavis.core.exception.JwtExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class JwtExpiredExceptionHandler extends AbstractExceptionHandler<JwtExpiredException> {

  /**
   * JwtExpiredExceptionHandler.
   */
  public JwtExpiredExceptionHandler() {
    super(JwtExpiredException.class);
    log.info("JwtExpiredExceptionHandler created");
  }

  @Override
  public HttpStatus getStatus(JwtExpiredException ex) {
    return HttpStatus.UNAUTHORIZED;
  }
}
