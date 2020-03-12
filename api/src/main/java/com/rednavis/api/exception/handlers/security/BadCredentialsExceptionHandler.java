package com.rednavis.api.exception.handlers.security;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class BadCredentialsExceptionHandler extends AbstractExceptionHandler<BadCredentialsException> {

  /**
   * BadCredentialsExceptionHandler.
   */
  public BadCredentialsExceptionHandler() {
    super(BadCredentialsException.class);
    log.info("BadCredentialsExceptionHandler created");
  }

  @Override
  public HttpStatus getStatus(BadCredentialsException ex) {
    return HttpStatus.UNAUTHORIZED;
  }
}
