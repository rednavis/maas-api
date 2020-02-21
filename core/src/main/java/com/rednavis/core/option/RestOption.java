package com.rednavis.core.option;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestOption {

  public static final String AUTH_URL = "/api/auth";
  public static final String USER_URL = "/api/users";

  public static final List<String> AUTH_WHITELIST = List.of(
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/v2/api-docs",
      "/webjars/**",
      "/",
      "/favicon.ico",
      "/**/*.png",
      "/**/*.gif",
      "/**/*.svg",
      "/**/*.jpg",
      "/**/*.html",
      "/**/*.css",
      "/**/*.js"
  );
}
