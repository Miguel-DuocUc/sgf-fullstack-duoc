package com.duoc.sgf.ms_gateway.config;

import com.duoc.sgf.ms_gateway.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // Apagamos CSRF porque usamos JWT
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())

                .authorizeExchange(exchanges -> exchanges
                        // 1. Añadimos explícitamente el archivo exacto "/swagger-ui.html"
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/docs/**").permitAll()

                        // 2. El endpoint para loguearse es PÚBLICO
                        .pathMatchers("/api/v1/auth/**").permitAll()

                        // 3. TODO LO DEMÁS ESTÁ BLOQUEADO
                        .anyExchange().authenticated()
                )
                // 4. Inyectamos tu filtro JWT
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}