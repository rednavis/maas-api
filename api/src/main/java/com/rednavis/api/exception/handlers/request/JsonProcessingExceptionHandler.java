package com.rednavis.api.exception.handlers.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class JsonProcessingExceptionHandler extends AbstractBadRequestExceptionHandler<JsonProcessingException> {

  /**
   * JsonProcessingExceptionHandler.
   */
  public JsonProcessingExceptionHandler() {
    super(JsonProcessingException.class);
    log.info("JsonProcessingExceptionHandler created");
  }
}
