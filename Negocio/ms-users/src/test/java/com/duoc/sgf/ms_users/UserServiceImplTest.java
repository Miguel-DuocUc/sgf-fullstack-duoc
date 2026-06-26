package com.duoc.sgf.ms_users;

import com.duoc.sgf.ms_users.model.User;
import com.duoc.sgf.ms_users.model.dto.UserRequestDto;
import com.duoc.sgf.ms_users.model.dto.UserResponseDto;
import com.duoc.sgf.ms_users.model.mapper.UserMapper;
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
    private UserMapper mapper;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        mapper = mock(UserMapper.class);

        userService = new UserServiceImpl(userRepository, passwordEncoder, mapper);
    }

    @Test
    void debeCrearUsuarioCorrectamente() {
        UserRequestDto request = crearRequest();

        User userToSave = crearUsuarioSinId();
        User savedUser = crearUsuario();
        UserResponseDto responseDto = crearResponseDto();

        when(userRepository.existsByRut(request.getRut())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(userToSave);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(mapper.toDto(savedUser)).thenReturn(responseDto);

        UserResponseDto response = userService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getName());
        assertEquals("Perez", response.getLastName());
        assertEquals("juan.perez@test.com", response.getEmail());
        assertEquals("ADMIN", response.getRole());
        assertEquals("ACTIVO", response.getStatus());

        verify(mapper, times(1)).toEntity(request);
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(mapper, times(1)).toDto(savedUser);
    }

    @Test
    void noDebeCrearUsuarioConRutDuplicado() {
        UserRequestDto request = crearRequest();

        when(userRepository.existsByRut(request.getRut())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.create(request));

        verify(userRepository, never()).save(any(User.class));
        verify(mapper, never()).toEntity(any(UserRequestDto.class));
    }

    @Test
    void noDebeCrearUsuarioConCorreoDuplicado() {
        UserRequestDto request = crearRequest();

        when(userRepository.existsByRut(request.getRut())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.create(request));

        verify(userRepository, never()).save(any(User.class));
        verify(mapper, never()).toEntity(any(UserRequestDto.class));
    }

    @Test
    void debeBuscarUsuarioPorIdCorrectamente() {
        User user = crearUsuario();
        UserResponseDto responseDto = crearResponseDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto response = userService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getName());
        assertEquals("ADMIN", response.getRole());

        verify(userRepository, times(1)).findById(1L);
        verify(mapper, times(1)).toDto(user);
    }

    @Test
    void debeLanzarErrorCuandoUsuarioNoExiste() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.findById(99L));

        verify(userRepository, times(1)).findById(99L);
        verify(mapper, never()).toDto(any(User.class));
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

    private UserRequestDto crearRequest() {
        UserRequestDto request = new UserRequestDto();
        request.setRut("12345678-9");
        request.setName("Juan");
        request.setLastName("Perez");
        request.setEmail("juan.perez@test.com");
        request.setPassword("Password123");
        request.setRole("ADMIN");
        request.setStatus("ACTIVO");
        return request;
    }

    private User crearUsuarioSinId() {
        User user = new User();
        user.setRut("12345678-9");
        user.setName("Juan");
        user.setLastName("Perez");
        user.setEmail("juan.perez@test.com");
        user.setPassword("Password123");
        user.setRole("ADMIN");
        user.setStatus("ACTIVO");
        return user;
    }

    private User crearUsuario() {
        User user = new User();
        user.setId(1L);
        user.setRut("12345678-9");
        user.setName("Juan");
        user.setLastName("Perez");
        user.setEmail("juan.perez@test.com");
        user.setPassword("encoded");
        user.setRole("ADMIN");
        user.setStatus("ACTIVO");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private UserResponseDto crearResponseDto() {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setRut("12345678-9");
        response.setName("Juan");
        response.setLastName("Perez");
        response.setEmail("juan.perez@test.com");
        response.setRole("ADMIN");
        response.setStatus("ACTIVO");
        response.setCreatedAt(LocalDateTime.now());
        return response;
    }
}