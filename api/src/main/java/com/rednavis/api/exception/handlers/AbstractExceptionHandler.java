package com.rednavis.api.exception.handlers;

import com.rednavis.api.exception.ExceptionUtils;
import com.rednavis.shared.rest.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Extend this to code an exception handler.
 */
@Getter
public abstract class AbstractExceptionHandler<T extends Throwable> {

  private Class<T> exceptionClass;

  public AbstractExceptionHandler(Class<T> exceptionClass) {
    this.exceptionClass = exceptionClass;
  }

  protected String getExceptionId(T ex) {
    return ExceptionUtils.getExceptionId(ex);
  }

  protected String getMessage(T ex) {
    return ex.getMessage();
  }

  protected abstract HttpStatus getStatus(T ex);

  /**
   * getErrorResponse.
   *
   * @param ex ex
   * @return
   */
  public ErrorResponse getErrorResponse(T ex) {
    ErrorResponse.ErrorResponseBuilder errorResponseBuilder = ErrorResponse.builder()
        .exceptionId(getExceptionId(ex))
        .message(getMessage(ex));

    HttpStatus status = getStatus(ex);
    if (status != null) {
      errorResponseBuilder.status(status.value());
      errorResponseBuilder.error(status.getReasonPhrase());
    }

    return errorResponseBuilder.build();
  }
}
