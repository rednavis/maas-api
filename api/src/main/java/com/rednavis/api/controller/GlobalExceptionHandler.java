package com.rednavis.api.controller;

import com.rednavis.core.exception.BadRequestException;
import com.rednavis.core.exception.ConflictException;
import com.rednavis.core.exception.MaasAppException;
import com.rednavis.core.exception.NotFoundException;
import com.rednavis.shared.rest.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse runtime(RuntimeException exception) {
    log.error(exception.getMessage());
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse notFoundHandler(NotFoundException notFoundException) {
    log.error(notFoundException.getMessage());
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), notFoundException.getMessage());
  }

  @ExceptionHandler(ConflictException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse conflictHandler(ConflictException conflictException) {
    log.error(conflictException.getMessage());
    return new ErrorResponse(HttpStatus.CONFLICT.value(), conflictException.getMessage());
  }

  @ExceptionHandler({MaasAppException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse httpClientErrorHandler(MaasAppException maasAppException) {
    log.error(maasAppException.getMessage());
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), maasAppException.getMessage());
  }
}
