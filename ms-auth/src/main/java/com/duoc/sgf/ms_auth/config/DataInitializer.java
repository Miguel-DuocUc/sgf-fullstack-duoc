package com.duoc.sgf.ms_auth.config;

import com.duoc.sgf.ms_auth.model.Usuario;
import com.duoc.sgf.ms_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== CONFIGURANDO OPERADORES DEL SISTEMA FRONTERIZO ===");

        // 1. GESTIÓN DEL ADMINISTRADOR (Fuerza la clave 'admin123')
        usuarioRepository.findByUsername("admin").ifPresentOrElse(
                adminExistente -> {
                    adminExistente.setPassword(passwordEncoder.encode("admin123")); // Actualiza a la nueva
                    adminExistente.setRoles(Set.of("ROLE_ADMIN"));
                    adminExistente.setActivo(true);
                    usuarioRepository.saveAndFlush(adminExistente);
                    log.info("¡Cuenta 'admin' actualizada con éxito a 'admin123'!");
                },
                () -> {
                    Usuario nuevoAdmin = Usuario.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("admin123"))
                            .activo(true)
                            .roles(Set.of("ROLE_ADMIN"))
                            .build();
                    usuarioRepository.saveAndFlush(nuevoAdmin);
                    log.info("Cuenta 'admin' creada desde cero.");
                }
        );

        // 2. GESTIÓN DEL OPERADOR (Mantiene 'user123')
        usuarioRepository.findByUsername("operador01").ifPresentOrElse(
                operadorExistente -> {
                    operadorExistente.setPassword(passwordEncoder.encode("user123"));
                    operadorExistente.setRoles(Set.of("ROLE_OPERATOR"));
                    operadorExistente.setActivo(true);
                    usuarioRepository.saveAndFlush(operadorExistente);
                    log.info("Cuenta 'operador01' sincronizada.");
                },
                () -> {
                    Usuario nuevoOperador = Usuario.builder()
                            .username("operador01")
                            .password(passwordEncoder.encode("user123"))
                            .activo(true)
                            .roles(Set.of("ROLE_OPERATOR"))
                            .build();
                    usuarioRepository.saveAndFlush(nuevoOperador);
                    log.info("Cuenta 'operador01' creada desde cero.");
                }
        );

        log.info("=== ¡SISTEMA INICIALIZADO CON ROLES SEPARADOS! ===");
    }}