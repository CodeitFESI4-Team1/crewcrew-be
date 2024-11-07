package com.crewcrew.global.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  Server server = new Server().url("https://dev.dev-crewcrew.site/"); // 임시 로컬임

  @Bean
  public OpenAPI crewCrewApi() {
    Info info = new Info().version("1.0.0").title("CrewCrew API").description("CrewCrew API 명세서");

    SecurityScheme securityScheme =
        new SecurityScheme()
            .name(HttpHeaders.AUTHORIZATION)
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .bearerFormat("JWT")
            .scheme("bearer");
    SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");

    return new OpenAPI()
        .servers(List.of(server))
        .info(info)
        .addSecurityItem(securityRequirement)
        .components(new Components().addSecuritySchemes("JWT", securityScheme));
  }

  @Bean
  public GroupedOpenApi crew() {
    return GroupedOpenApi.builder()
        .group("크루 기능")
        .pathsToMatch("/api/crews/**")
        .packagesToScan("com.crewcrew.domain.crew.controller")
        .build();
  }

  @Bean
  public GroupedOpenApi member() {
    return GroupedOpenApi.builder()
        .group("회원 기능")
        .pathsToMatch("/auths/**")
        .packagesToScan("com.crewcrew.domain.member.controller")
        .build();
  }
}
