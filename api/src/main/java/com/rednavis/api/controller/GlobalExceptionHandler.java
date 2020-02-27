package com.rednavis.api.controller;

import com.rednavis.core.exception.BadRequestException;
import com.rednavis.core.exception.ConflictException;
import com.rednavis.core.exception.MaasApiException;
import com.rednavis.core.exception.NotFoundException;
import com.rednavis.shared.http.HttpStatusCode;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * badRequestHandelr.
   *
   * @param badRequestException badRequestException
   * @return
   */
  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse badRequestHandelr(BadRequestException badRequestException) {
    log.error(badRequestException.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), badRequestException.getMessage());
    return ApiResponse.createErrorResponse(HttpStatusCode.BAD_REQUEST, errorResponse);
  }

  /**
   * notFoundHandler.
   *
   * @param notFoundException notFoundException
   * @return
   */
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiResponse notFoundHandler(NotFoundException notFoundException) {
    log.error(notFoundException.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), notFoundException.getMessage());
    return ApiResponse.createErrorResponse(HttpStatusCode.NOT_FOUND, errorResponse);
  }

  /**
   * conflictHandler.
   *
   * @param conflictException conflictException
   * @return
   */
  @ExceptionHandler(ConflictException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiResponse conflictHandler(ConflictException conflictException) {
    log.error(conflictException.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), conflictException.getMessage());
    return ApiResponse.createErrorResponse(HttpStatusCode.CONFLICT, errorResponse);
  }

  /**
   * maasApiHandler.
   *
   * @param maasApiException maasApiException
   * @return
   */
  @ExceptionHandler({MaasApiException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse maasApiHandler(MaasApiException maasApiException) {
    log.error(maasApiException.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), maasApiException.getMessage());
    return ApiResponse.createErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, errorResponse);
  }
}
