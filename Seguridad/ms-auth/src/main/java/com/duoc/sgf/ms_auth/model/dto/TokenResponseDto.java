package com.duoc.sgf.ms_auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Objeto que contiene el token de acceso generado tras una autenticación exitosa")
public class TokenResponseDto {

    @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo de token de seguridad", example = "Bearer")
    private String tipo = "Bearer";

    public TokenResponseDto(String token) {
        this.token = token;
    }
}