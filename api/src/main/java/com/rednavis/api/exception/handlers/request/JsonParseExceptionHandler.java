package com.rednavis.api.exception.handlers.request;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order
public class JsonParseExceptionHandler extends AbstractBadRequestExceptionHandler<JsonParseException> {

  /**
   * JsonParseExceptionHandler.
   */
  public JsonParseExceptionHandler() {
    super(JsonParseException.class);
    log.info("JsonParseExceptionHandler created");
  }
}
