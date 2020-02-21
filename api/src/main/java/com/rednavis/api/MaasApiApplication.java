package com.rednavis.api;

import com.rednavis.auth.AuthModule;
import com.rednavis.core.CoreModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AuthModule.class,
    CoreModule.class})
public class MaasApiApplication {

  public static void main(String... args) {
    SpringApplication.run(MaasApiApplication.class, args);
  }
}
