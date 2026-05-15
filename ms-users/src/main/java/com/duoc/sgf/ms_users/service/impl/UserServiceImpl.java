package com.duoc.sgf.ms_users.service.impl;

import com.duoc.sgf.ms_users.dto.UserRequestDto;
import com.duoc.sgf.ms_users.dto.UserResponseDto;
import com.duoc.sgf.ms_users.model.User;
import com.duoc.sgf.ms_users.repository.UserRepository;
import com.duoc.sgf.ms_users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDto> findAll() {
        log.info("Listando usuarios");
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDto)
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

        return toResponseDto(user);
    }

    @Override
    public UserResponseDto findByRut(String rut) {
        log.info("Buscando usuario por RUT: {}", rut);

        User user = userRepository.findByRut(rut)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con RUT: {}", rut);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
                });

        return toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findByRole(String role) {
        log.info("Buscando usuarios por rol: {}", role);

        return userRepository.findByRole(role.toUpperCase())
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto create(UserRequestDto request) {
        log.info("Creando usuario con RUT: {}", request.getRut());

        if (userRepository.existsByRut(request.getRut())) {
            log.warn("RUT duplicado: {}", request.getRut());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese RUT");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Correo duplicado: {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo");
        }

        User user = new User();
        user.setRut(request.getRut());
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            user.setStatus("ACTIVO");
        } else {
            user.setStatus(request.getStatus().toUpperCase());
        }

        User savedUser = userRepository.save(user);

        log.info("Usuario creado correctamente con id: {}", savedUser.getId());

        return toResponseDto(savedUser);
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto request) {
        log.info("Actualizando usuario con id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Usuario no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
                });

        userRepository.findByRut(request.getRut()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                log.warn("Intento de actualizar con RUT duplicado: {}", request.getRut());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe otro usuario con ese RUT");
            }
        });

        userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                log.warn("Intento de actualizar con correo duplicado: {}", request.getEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe otro usuario con ese correo");
            }
        });

        user.setRut(request.getRut());
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            user.setStatus("ACTIVO");
        } else {
            user.setStatus(request.getStatus().toUpperCase());
        }

        User updatedUser = userRepository.save(user);

        log.info("Usuario actualizado correctamente con id: {}", updatedUser.getId());

        return toResponseDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando usuario con id: {}", id);

        if (!userRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Usuario no encontrado con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        userRepository.deleteById(id);

        log.info("Usuario eliminado correctamente con id: {}", id);
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getRut(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}