package com.duoc.sgf.ms_audit.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de respuesta que representa un registro inmutable en la bitácora de auditoría.")
public class AuditoriaResponseDto {

    @Schema(description = "Identificador único interno del log de auditoría", example = "10045")
    private Long id;

    @Schema(description = "Descripción detallada del evento registrado", example = "El usuario (ID: 5) aprobó la visa (ID: 105) exitosamente.")
    private String mensaje;

    @Schema(description = "Origen del evento", example = "ms-visa")
    private String emitidoPor;

    @Schema(description = "Fecha y hora exacta en que el evento quedó registrado permanentemente en el sistema")
    private LocalDateTime fechaRegistro;
}