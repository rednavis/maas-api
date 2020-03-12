package com.rednavis.api.exception.handlers.security;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import com.rednavis.core.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class JwtExceptionHandler extends AbstractExceptionHandler<JwtException> {

  /**
   * JwtExceptionHandler.
   */
  public JwtExceptionHandler() {
    super(JwtException.class);
    log.info("JwtExceptionHandler created");
  }

  @Override
  public HttpStatus getStatus(JwtException ex) {
    return HttpStatus.UNAUTHORIZED;
  }
}
