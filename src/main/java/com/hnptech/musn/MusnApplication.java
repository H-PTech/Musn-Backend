package com.hnptech.musn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MusnApplication {

  public static void main(String[] args) {
    SpringApplication.run(MusnApplication.class, args);
  }

}
