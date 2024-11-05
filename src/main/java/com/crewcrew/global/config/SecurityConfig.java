package com.crewcrew.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crewcrew.global.security.LoginFilter;
import com.crewcrew.global.security.LoginService;
import com.crewcrew.global.security.LogoutFilter;
import com.crewcrew.global.security.jwt.JwtFilter;
import com.crewcrew.global.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationEntryPoint entryPoint;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtUtil jwtUtil;
  private final LoginService loginService;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .httpBasic(basic -> basic.disable())
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configure(httpSecurity))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/auths/signup",
                        "/auths/signin",
                        "/health",
                        "/v3/api-docs/**",
                        "/swagger*/**",
                        "/")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
        .addFilterAt(
            new LoginFilter(authenticationManager(authenticationConfiguration), loginService),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            new LogoutFilter(loginService),
            org.springframework.security.web.authentication.logout.LogoutFilter.class)
        .build();
  }
}
