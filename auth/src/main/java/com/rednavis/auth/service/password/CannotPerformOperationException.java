package com.rednavis.auth.service.password;

class CannotPerformOperationException extends Exception {

  public CannotPerformOperationException(String message) {
    super(message);
  }

  public CannotPerformOperationException(String message, Throwable source) {
    super(message, source);
  }
}