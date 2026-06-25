package com.duoc.sgf.ms_users.service.impl;

import com.duoc.sgf.ms_users.model.User;
import com.duoc.sgf.ms_users.model.dto.UserRequestDto;
import com.duoc.sgf.ms_users.model.dto.UserResponseDto;
import com.duoc.sgf.ms_users.repository.UserRepository;
import com.duoc.sgf.ms_users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.duoc.sgf.ms_users.model.mapper.UserMapper;


import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;  // ← agregado

    @Override
    public List<UserResponseDto> findAll() {
        log.info("Listando todos los usuarios registrados");
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)  // ← cambiado
                .toList();
    }

    @Override
    public UserResponseDto findById(Long id) {
        log.info("Buscando usuario por id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
                });
        log.info("Usuario encontrado correctamente con id: {}", id);
        return mapper.toDto(user);  // ← cambiado
    }

    @Override
    public UserResponseDto findByRut(String rut) {
        log.info("Buscando usuario por RUT: {}", rut);
        User user = userRepository.findByRut(rut)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con RUT: {}", rut);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
                });
        log.info("Usuario encontrado correctamente con RUT: {}", rut);
        return mapper.toDto(user);  // ← cambiado
    }

    @Override
    public List<UserResponseDto> findByRole(String role) {
        log.info("Buscando usuarios por rol: {}", role);
        List<UserResponseDto> users = userRepository.findByRole(role.toUpperCase())
                .stream()
                .map(mapper::toDto)  // ← cambiado
                .toList();
        log.info("Cantidad de usuarios encontrados para el rol {}: {}", role, users.size());
        return users;
    }

    @Override
    public UserResponseDto create(UserRequestDto request) {
        log.info("Iniciando creación de usuario con RUT: {}", request.getRut());

        if (userRepository.existsByRut(request.getRut())) {
            log.warn("No se pudo crear usuario. RUT duplicado: {}", request.getRut());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese RUT");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("No se pudo crear usuario. Correo duplicado: {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo");
        }

        User user = mapper.toEntity(request);  // ← cambiado
        // La contraseña se encripta aquí, no en el mapper
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());
        user.setStatus(request.getStatus() == null || request.getStatus().isBlank()
                ? "ACTIVO" : request.getStatus().toUpperCase());

        User saved = userRepository.save(user);
        log.info("Usuario creado correctamente con id: {}", saved.getId());
        return mapper.toDto(saved);  // ← cambiado
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto request) {
        log.info("Iniciando actualización de usuario con id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Usuario no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
                });

        userRepository.findByRut(request.getRut()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                log.warn("No se pudo actualizar usuario {}. RUT duplicado: {}", id, request.getRut());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe otro usuario con ese RUT");
            }
        });

        userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                log.warn("No se pudo actualizar usuario {}. Correo duplicado: {}", id, request.getEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe otro usuario con ese correo");
            }
        });

        user.setRut(request.getRut());
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        // La contraseña se encripta aquí, no en el mapper
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());
        user.setStatus(request.getStatus() == null || request.getStatus().isBlank()
                ? "ACTIVO" : request.getStatus().toUpperCase());

        User updated = userRepository.save(user);
        log.info("Usuario actualizado correctamente con id: {}", updated.getId());
        return mapper.toDto(updated);  // ← cambiado
    }

    @Override
    public void delete(Long id) {
        log.info("Iniciando eliminación de usuario con id: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Usuario no encontrado con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        userRepository.deleteById(id);
        log.info("Usuario eliminado correctamente con id: {}", id);
    }
}
