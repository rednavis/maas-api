package com.rednavis.api.exception;

import com.rednavis.shared.rest.response.ErrorResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorAttributesExceptionHandler<T extends Throwable> extends DefaultErrorAttributes {

  /**
   * Component that actually builds the error response.
   */
  private final ErrorResponseComposer<T> errorResponseComposer;

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
    addErrorDetails(errorAttributes, request);
    return errorAttributes;
  }

  /**
   * Handles exceptions.
   */
  private void addErrorDetails(Map<String, Object> errorAttributes, ServerRequest request) {
    Throwable ex = getError(request);
    log.error(ex.getMessage());

    errorResponseComposer.compose((T) ex).ifPresent(errorResponse -> {
      // check for nulls - errorResponse may have left something for the DefaultErrorAttributes
      if (errorResponse.getExceptionId() != null) {
        errorAttributes.put(ErrorResponse.Fields.exceptionId, errorResponse.getExceptionId());
      }
      if (errorResponse.getMessage() != null) {
        errorAttributes.put(ErrorResponse.Fields.message, errorResponse.getMessage());
      }
      Integer status = errorResponse.getStatus();
      if (status != null) {
        errorAttributes.put(ErrorResponse.Fields.status, status);
        errorAttributes.put(ErrorResponse.Fields.error, errorResponse.getError());
      }
    });
    if (errorAttributes.get(ErrorResponse.Fields.exceptionId) == null) {
      errorAttributes.put(ErrorResponse.Fields.exceptionId, ExceptionUtils.getExceptionId(ex));
    }
  }
}
