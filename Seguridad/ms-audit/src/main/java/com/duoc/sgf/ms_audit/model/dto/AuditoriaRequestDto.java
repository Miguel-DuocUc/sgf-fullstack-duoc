package com.duoc.sgf.ms_audit.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos requeridos para registrar un evento en la bitácora de auditoría del sistema.")
public class AuditoriaRequestDto {

    @NotBlank(message = "El mensaje de auditoría es obligatorio")
    @Schema(description = "Descripción detallada del evento, acción o error registrado", example = "El usuario (ID: 5) aprobó la visa (ID: 105) exitosamente.")
    private String mensaje;

    @NotBlank(message = "El emisor es obligatorio")
    @Schema(description = "Nombre del microservicio, módulo o agente que emite el registro de auditoría", example = "ms-visa")
    private String emitidoPor;
}