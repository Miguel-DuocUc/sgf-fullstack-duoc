package com.duoc.sgf.ms_auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto de transferencia de datos para la solicitud de inicio de sesión")
public class LoginRequestDto {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario registrado en el sistema", example = "miguel_admin")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña en texto plano para validación", example = "Password123!")
    private String password;
}