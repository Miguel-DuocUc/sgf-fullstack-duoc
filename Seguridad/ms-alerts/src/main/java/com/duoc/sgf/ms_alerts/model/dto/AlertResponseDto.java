package com.duoc.sgf.ms_alerts.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta con los datos consolidados y el estado de una alerta de seguridad.")
public class AlertResponseDto {

    @Schema(description = "Identificador único interno de la alerta en el ecosistema", example = "9012")
    private Long id;

    @Schema(description = "Documento de identidad o pasaporte bloqueado", example = "P987654321")
    private String pasaporteCiudadano;

    @Schema(description = "Nombre completo del ciudadano reportado", example = "Andrea Rojas")
    private String nombreCompleto;

    @Schema(description = "Categoría o nivel de la alerta", example = "ARRAIGO_NACIONAL")
    private String tipoAlerta;

    @Schema(description = "Razón o dictamen de la restricción", example = "Orden de detención pendiente por tribunal competente")
    private String motivo;

    @Schema(description = "Entidad que originó la alerta", example = "INTERPOL")
    private String emitidoPor;

    @Schema(description = "Fecha y hora exacta en que la alerta ingresó al sistema fronterizo")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Indica si la alerta sigue vigente (true) o si ya fue levantada/resuelta (false)", example = "true")
    private boolean activa;
}