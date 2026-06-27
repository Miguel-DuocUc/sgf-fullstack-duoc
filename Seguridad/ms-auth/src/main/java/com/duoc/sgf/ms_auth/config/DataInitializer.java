package com.duoc.sgf.ms_auth.config;

import com.duoc.sgf.ms_auth.repository.UsuarioRepository;
import com.duoc.sgf.ms_auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            log.info("--- Inicializando usuarios base con contraseñas encriptadas ---");

            usuarioService.upsertUsuario("superadmin", "super123", Set.of("ROLE_SUPER_ADMIN"));
            usuarioService.upsertUsuario("security_admin", "security123", Set.of("ROLE_SECURITY_ADMIN"));
            usuarioService.upsertUsuario("audit_admin", "audit123", Set.of("ROLE_AUDITOR"));
            usuarioService.upsertUsuario("alert_operator", "alert123", Set.of("ROLE_ALERT_ANALYST"));
            usuarioService.upsertUsuario("border_agent_01", "border123", Set.of("ROLE_BORDER_AGENT"));
            usuarioService.upsertUsuario("border_supervisor", "borderadmin123", Set.of("ROLE_BORDER_SUPERVISOR"));
            usuarioService.upsertUsuario("visa_operator", "visa123", Set.of("ROLE_VISA_OFFICER"));
            usuarioService.upsertUsuario("identity_operator", "identity123", Set.of("ROLE_IDENTITY_OFFICER"));
            usuarioService.upsertUsuario("health_operator", "health123", Set.of("ROLE_HEALTH_OFFICER"));
            usuarioService.upsertUsuario("logistics_operator", "logistics123", Set.of("ROLE_LOGISTICS_OPERATOR"));
            usuarioService.upsertUsuario("infra_admin", "infra123", Set.of("ROLE_INFRA_ADMIN"));
            usuarioService.upsertUsuario("migration_operator", "migration123", Set.of("ROLE_MIGRATION_OPERATOR"));
            usuarioService.upsertUsuario("citizen_demo", "citizen123", Set.of("ROLE_CITIZEN"));

            log.info("--- Usuarios iniciales creados correctamente ---");
        }
    }
}