package com.duoc.sgf.ms_auth.controller;

import com.duoc.sgf.ms_auth.model.dto.LoginRequestDto;
import com.duoc.sgf.ms_auth.model.dto.TokenResponseDto;
import com.duoc.sgf.ms_auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para el control de acceso y generación de tokens de seguridad")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión de usuario", description = "Valida las credenciales ingresadas y, de ser correctas, devuelve un token JWT tipo Bearer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, token generado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, faltan campos obligatorios"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas o usuario no autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        TokenResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}