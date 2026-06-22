package com.duoc.sgf.ms_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        // 1. Permitir acceso a la interfaz de Swagger y las rutas de recolección de JSON
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/docs/**").permitAll()
                        // 2. Aquí irán las reglas públicas de tus APIs reales
                        .pathMatchers("/api/v1/auth/**").permitAll()

                        .anyExchange().permitAll()
                );
        // Agrega aquí la validación de tu JWT si ya la tienes configurada

        return http.build();
    }

}