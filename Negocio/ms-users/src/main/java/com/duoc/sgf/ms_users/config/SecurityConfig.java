package com.duoc.sgf.ms_users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Exponemos el JSON para que el Gateway lo pueda leer sin token
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        // En modelo "Confianza en el Gateway", aceptamos el tráfico interno
                        // (En el futuro, aquí volveremos a poner .authenticated() para Zero Trust)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}