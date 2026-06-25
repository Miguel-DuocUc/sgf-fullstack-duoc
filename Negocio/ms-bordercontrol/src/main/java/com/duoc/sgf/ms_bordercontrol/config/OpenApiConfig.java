package com.duoc.sgf.ms_bordercontrol.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Le damos un nombre interno a nuestra regla de seguridad
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // 1. Forzamos el enrutamiento a través del Gateway
                .servers(List.of(
                        new Server().url("http://localhost:9090").description("API Gateway (S.D.G.F.)")
                ))
                // 2. Le decimos a Swagger: "Aplica esta regla de seguridad a TODOS los endpoints por defecto"
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 3. Le explicamos a Swagger cómo funciona esta regla (Es un token HTTP tipo Bearer JWT)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}