package com.duoc.sgf.ms_auth.service.impl;

import com.duoc.sgf.ms_auth.model.Usuario;
import com.duoc.sgf.ms_auth.repository.UsuarioRepository;
import com.duoc.sgf.ms_auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void upsertUsuario(String username, String password, Set<String> roles) {

        usuarioRepository.findByUsername(username)
                .ifPresentOrElse(

                        usuarioExistente -> {
                            usuarioExistente.setPassword(passwordEncoder.encode(password));
                            usuarioExistente.setRoles(roles);
                            usuarioExistente.setActivo(true);

                            usuarioRepository.saveAndFlush(usuarioExistente);

                            log.info("Usuario '{}' actualizado en el sistema.", username);
                        },

                        () -> {
                            Usuario nuevoUsuario = Usuario.builder()
                                    .username(username)
                                    .password(passwordEncoder.encode(password))
                                    .activo(true)
                                    .roles(roles)
                                    .build();

                            usuarioRepository.saveAndFlush(nuevoUsuario);

                            log.info("Usuario '{}' creado en el sistema.", username);
                        }
                );
    }
}