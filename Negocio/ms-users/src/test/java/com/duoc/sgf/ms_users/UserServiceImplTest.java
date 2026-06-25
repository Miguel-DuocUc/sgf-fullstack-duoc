package com.duoc.sgf.ms_users;

import com.duoc.sgf.ms_users.model.User;
import com.duoc.sgf.ms_users.model.dto.UserRequestDto;
import com.duoc.sgf.ms_users.model.dto.UserResponseDto;
import com.duoc.sgf.ms_users.repository.UserRepository;
import com.duoc.sgf.ms_users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void debeCrearUsuarioCorrectamente() {
        UserRequestDto request = new UserRequestDto();
        request.setRut("12345678-9");
        request.setName("Juan");
        request.setLastName("Perez");
        request.setEmail("juan.perez@test.com");
        request.setPassword("Password123");
        request.setRole("ADMIN");
        request.setStatus("ACTIVO");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setRut("12345678-9");
        savedUser.setName("Juan");
        savedUser.setLastName("Perez");
        savedUser.setEmail("juan.perez@test.com");
        savedUser.setPassword("encoded");
        savedUser.setRole("ADMIN");
        savedUser.setStatus("ACTIVO");
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.existsByRut("12345678-9")).thenReturn(false);
        when(userRepository.existsByEmail("juan.perez@test.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto response = userService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getName());
        assertEquals("Perez", response.getLastName());
        assertEquals("juan.perez@test.com", response.getEmail());
        assertEquals("ADMIN", response.getRole());
        assertEquals("ACTIVO", response.getStatus());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void noDebeCrearUsuarioConRutDuplicado() {
        UserRequestDto request = new UserRequestDto();
        request.setRut("12345678-9");
        request.setName("Juan");
        request.setLastName("Perez");
        request.setEmail("juan.perez@test.com");
        request.setPassword("Password123");
        request.setRole("ADMIN");
        request.setStatus("ACTIVO");

        when(userRepository.existsByRut("12345678-9")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.create(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void noDebeCrearUsuarioConCorreoDuplicado() {
        UserRequestDto request = new UserRequestDto();
        request.setRut("12345678-9");
        request.setName("Juan");
        request.setLastName("Perez");
        request.setEmail("juan.perez@test.com");
        request.setPassword("Password123");
        request.setRole("ADMIN");
        request.setStatus("ACTIVO");

        when(userRepository.existsByRut("12345678-9")).thenReturn(false);
        when(userRepository.existsByEmail("juan.perez@test.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.create(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void debeBuscarUsuarioPorIdCorrectamente() {
        User user = new User();
        user.setId(1L);
        user.setRut("12345678-9");
        user.setName("Juan");
        user.setLastName("Perez");
        user.setEmail("juan.perez@test.com");
        user.setRole("ADMIN");
        user.setStatus("ACTIVO");
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDto response = userService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getName());
        assertEquals("ADMIN", response.getRole());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void debeLanzarErrorCuandoUsuarioNoExiste() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.findById(99L));

        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void debeEliminarUsuarioCorrectamente() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void noDebeEliminarUsuarioInexistente() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> userService.delete(99L));

        verify(userRepository, never()).deleteById(99L);
    }
}