package com.crewcrew.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncoderConfig {

  @Bean
  @Primary
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
