package com.duoc.sgf.ms_users.controller;

import com.duoc.sgf.ms_users.dto.UserRequestDto;
import com.duoc.sgf.ms_users.dto.UserResponseDto;
import com.duoc.sgf.ms_users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<UserResponseDto> findByRut(@PathVariable String rut) {
        return ResponseEntity.ok(userService.findByRut(rut));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDto>> findByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}