package com.duoc.sgf.ms_health.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de respuesta con los datos consolidados y el nivel de riesgo sanitario evaluado.")
public class HealthDeclarationResponseDto {

    @Schema(description = "Identificador único interno de la declaración de salud", example = "88")
    private Long id;

    @Schema(description = "Identificador del ciudadano vinculado", example = "1")
    private Long userId;

    @Schema(description = "Identificador del documento de identidad validado", example = "42")
    private Long identityDocumentId;

    @Schema(description = "Bandera de síntomas declarados", example = "false")
    private Boolean hasSymptoms;

    @Schema(description = "Descripción de los síntomas", example = "Ninguno")
    private String symptomsDescription;

    @Schema(description = "Bandera de contacto estrecho reciente", example = "false")
    private Boolean hasRecentContact;

    @Schema(description = "Estado del esquema de vacunación", example = "ESQUEMA_COMPLETO")
    private String vaccinationStatus;

    @Schema(description = "Nivel de riesgo epidemiológico calculado por el sistema o asignado por el oficial", example = "BAJO")
    private String riskLevel;

    @Schema(description = "Estado de resolución sanitaria de la declaración", example = "APROBADA")
    private String status;

    @Schema(description = "Observaciones del oficial de salud a cargo", example = "Sin observaciones. Tránsito sanitariamente seguro.")
    private String observations;

    @Schema(description = "Fecha y hora exacta en que se registró la declaración en el sistema")
    private LocalDateTime createdAt;
}