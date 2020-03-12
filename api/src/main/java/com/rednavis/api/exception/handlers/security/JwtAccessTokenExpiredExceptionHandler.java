package com.rednavis.api.exception.handlers.security;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import com.rednavis.core.exception.JwtAccessTokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class JwtAccessTokenExpiredExceptionHandler extends AbstractExceptionHandler<JwtAccessTokenExpiredException> {

  /**
   * JwtAccessTokenExpiredExceptionHandler.
   */
  public JwtAccessTokenExpiredExceptionHandler() {
    super(JwtAccessTokenExpiredException.class);
    log.info("JwtAccessTokenExpiredExceptionHandler created");
  }

  @Override
  public HttpStatus getStatus(JwtAccessTokenExpiredException ex) {
    return HttpStatus.UNAUTHORIZED;
  }
}
