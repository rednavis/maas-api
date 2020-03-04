package com.rednavis.auth.jwt;

import lombok.Data;

@Data
public class JwtTokenInfo {

  private String token;
  private int tokenExpirationInSec;
  private long expirationTime;
}
