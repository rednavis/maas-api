package com.rednavis.api.exception.handlers.security;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class AccessDeniedExceptionHandler extends AbstractExceptionHandler<AccessDeniedException> {

  /**
   * AccessDeniedExceptionHandler.
   */
  public AccessDeniedExceptionHandler() {
    super(AccessDeniedException.class);
    log.info("AccessDeniedException сreated");
  }

  @Override
  public HttpStatus getStatus(AccessDeniedException ex) {
    return HttpStatus.FORBIDDEN;
  }
}
