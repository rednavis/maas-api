package com.rednavis.api.exception.handlers.security;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class UsernameNotFoundExceptionHandler extends AbstractExceptionHandler<UsernameNotFoundException> {

  /**
   * UsernameNotFoundExceptionHandler.
   */
  public UsernameNotFoundExceptionHandler() {
    super(UsernameNotFoundException.class);
    log.info("UsernameNotFoundExceptionHandler сreated");
  }

  @Override
  public HttpStatus getStatus(UsernameNotFoundException ex) {
    return HttpStatus.UNAUTHORIZED;
  }
}
