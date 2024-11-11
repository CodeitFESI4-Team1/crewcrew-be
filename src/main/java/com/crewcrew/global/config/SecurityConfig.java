package com.crewcrew.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/api/crews")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/crews/*")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/crews/*/join")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/crews/*")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/crews/*/leave")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/crews/joined")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/crews/hosted")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/crews/*/gatherings")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/crews/*/gatherings/*/join")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/crews/*/gatherings/hosted")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/crews/*/gatherings/joined")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/images")
                    .authenticated()
                    .requestMatchers(
                        "/",
                        "/health", // /health 엔드포인트 허용
                        "/auths/login",
                        "/auths/signup",
                        "/v3/api-docs/**",
                        "/swagger*/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/crews/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
        .addFilterAt(
            new LoginFilter(authenticationManager(authenticationConfiguration), loginService),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            new LogoutFilter(loginService),
            org.springframework.security.web.authentication.logout.LogoutFilter.class)
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
        List.of("http://localhost:3000", "https://crewcrew.vercel.app"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
