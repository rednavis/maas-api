package com.rednavis.api.exception;

import java.util.function.Function;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionUtils {

  private static final Function<Throwable, String> EXCEPTION_ID_MAKER = ex -> (ex != null) ? ex.getClass().getSimpleName() : null;

  public static String getExceptionId(Throwable ex) {
    Throwable root = getRootException(ex);
    return EXCEPTION_ID_MAKER.apply(root);
  }

  private static Throwable getRootException(Throwable ex) {
    if (ex == null) {
      return null;
    }
    while (ex.getCause() != null) {
      ex = ex.getCause();
    }
    return ex;
  }
}
