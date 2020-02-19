package com.rednavis.api;

import com.rednavis.shared.SharedLibrary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MaasApiApplication {

  public static void main(String... args) {
    SpringApplication.run(MaasApiApplication.class, args);
  }

  @GetMapping("/shared")
  public ResponseEntity<String> getShared() {
    SharedLibrary sharedLibrary = new SharedLibrary();
    return new ResponseEntity<>(sharedLibrary.getVersion(), HttpStatus.OK);
  }
}
