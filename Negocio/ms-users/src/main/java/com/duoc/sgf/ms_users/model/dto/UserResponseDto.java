package com.duoc.sgf.ms_users.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de respuesta con los datos públicos de un usuario del sistema.")
public class UserResponseDto {

    @Schema(description = "Identificador único interno", example = "1")
    private Long id;

    @Schema(description = "Documento de identidad principal", example = "19283746-5")
    private String rut;

    @Schema(description = "Nombre(s)", example = "Miguel")
    private String name;

    @Schema(description = "Apellido(s)", example = "González")
    private String lastName;

    @Schema(description = "Correo electrónico asociado", example = "miguel.gonzalez@estado.sgf")
    private String email;

    @Schema(description = "Rol de acceso asignado", example = "ROLE_BORDER_AGENT")
    private String role;

    @Schema(description = "Estado actual de la cuenta", example = "ACTIVO")
    private String status;

    @Schema(description = "Fecha y hora en que se creó el registro en la base de datos")
    private LocalDateTime createdAt;
}