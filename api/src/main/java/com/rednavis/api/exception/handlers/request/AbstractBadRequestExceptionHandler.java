package com.rednavis.api.exception.handlers.request;

import com.rednavis.api.exception.handlers.AbstractExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

/**
 * Extend this for any exception handler that should return a 400 response.
 */
@Order
public abstract class AbstractBadRequestExceptionHandler<T extends Throwable> extends AbstractExceptionHandler<T> {

  public AbstractBadRequestExceptionHandler(Class<T> exceptionClass) {
    super(exceptionClass);
  }

  @Override
  public HttpStatus getStatus(T ex) {
    return HttpStatus.BAD_REQUEST;
  }
}
