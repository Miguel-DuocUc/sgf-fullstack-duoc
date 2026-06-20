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
    public void run(String... args) {

        log.info("=== INICIALIZANDO OPERADORES DEL ECOSISTEMA FRONTERIZO ===");

        crearUsuario(
                "superadmin",
                "super123",
                Set.of("ROLE_SUPER_ADMIN")
        );

        crearUsuario(
                "security_admin",
                "security123",
                Set.of("ROLE_SECURITY_ADMIN")
        );

        crearUsuario(
                "audit_admin",
                "audit123",
                Set.of("ROLE_AUDITOR")
        );

        crearUsuario(
                "alert_operator",
                "alert123",
                Set.of("ROLE_ALERT_ANALYST")
        );

        crearUsuario(
                "border_agent_01",
                "border123",
                Set.of("ROLE_BORDER_AGENT")
        );

        crearUsuario(
                "border_supervisor",
                "borderadmin123",
                Set.of("ROLE_BORDER_SUPERVISOR")
        );

        crearUsuario(
                "visa_operator",
                "visa123",
                Set.of("ROLE_VISA_OFFICER")
        );

        crearUsuario(
                "identity_operator",
                "identity123",
                Set.of("ROLE_IDENTITY_OFFICER")
        );

        crearUsuario(
                "health_operator",
                "health123",
                Set.of("ROLE_HEALTH_OFFICER")
        );

        crearUsuario(
                "logistics_operator",
                "logistics123",
                Set.of("ROLE_LOGISTICS_OPERATOR")
        );

        crearUsuario(
                "infra_admin",
                "infra123",
                Set.of("ROLE_INFRA_ADMIN")
        );

        crearUsuario(
                "migration_operator",
                "migration123",
                Set.of("ROLE_MIGRATION_OPERATOR")
        );

        crearUsuario(
                "citizen_demo",
                "citizen123",
                Set.of("ROLE_CITIZEN")
        );

        log.info("=== SISTEMA FRONTERIZO OPERATIVO ===");
    }

    private void crearUsuario(String username, String password, Set<String> roles) {

        usuarioRepository.findByUsername(username)
                .ifPresentOrElse(

                        usuarioExistente -> {

                            usuarioExistente.setPassword(
                                    passwordEncoder.encode(password)
                            );

                            usuarioExistente.setRoles(roles);
                            usuarioExistente.setActivo(true);

                            usuarioRepository.saveAndFlush(usuarioExistente);

                            log.info("Usuario '{}' actualizado.", username);
                        },

                        () -> {

                            Usuario nuevoUsuario = Usuario.builder()
                                    .username(username)
                                    .password(passwordEncoder.encode(password))
                                    .activo(true)
                                    .roles(roles)
                                    .build();

                            usuarioRepository.saveAndFlush(nuevoUsuario);

                            log.info("Usuario '{}' creado.", username);
                        }
                );
    }
}