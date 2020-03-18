package com.rednavis.api.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("maas")
public class MaasProperty {

  private Bpm bpm;

  @Data
  public static class Bpm {

    private String server;
  }
}
