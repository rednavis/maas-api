package com.rednavis.auth.jwt;

import lombok.Data;

@Data
public class JwtTokenInfo {

  private String token;
  private long expirationTime;
}
