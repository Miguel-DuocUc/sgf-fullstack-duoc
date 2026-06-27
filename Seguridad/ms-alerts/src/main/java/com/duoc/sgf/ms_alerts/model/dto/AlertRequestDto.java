package com.duoc.sgf.ms_alerts.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para registrar y emitir una nueva alerta de seguridad sobre un ciudadano.")
public class AlertRequestDto {

    @NotBlank(message = "El pasaporte es obligatorio")
    @Schema(description = "Documento de identidad o pasaporte del ciudadano sujeto a la restricción", example = "P987654321")
    private String pasaporteCiudadano;

    @Schema(description = "Nombre completo del ciudadano (referencial para los agentes de frontera)", example = "Andrea Rojas")
    private String nombreCompleto;

    @NotBlank(message = "El tipo de alerta es obligatorio")
    @Schema(description = "Categoría o nivel de la alerta de seguridad", example = "ARRAIGO_NACIONAL")
    private String tipoAlerta;

    @NotBlank(message = "El motivo es obligatorio")
    @Schema(description = "Justificación legal, judicial o policial para la emisión de la alerta", example = "Orden de detención pendiente por tribunal competente")
    private String motivo;

    @NotBlank(message = "El emisor es obligatorio")
    @Schema(description = "Agencia, tribunal o institución internacional que dicta la alerta", example = "INTERPOL")
    private String emitidoPor;
}