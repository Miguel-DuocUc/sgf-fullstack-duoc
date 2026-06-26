package com.duoc.sgf.ms_health.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos requeridos para emitir una declaración jurada de salud al intentar cruzar la frontera.")
public class HealthDeclarationRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    @Schema(description = "Identificador único del ciudadano que realiza la declaración", example = "1")
    private Long userId;

    @NotNull(message = "El id del documento de identidad es obligatorio")
    @Schema(description = "Identificador del documento (ej. pasaporte) asociado al cruce", example = "42")
    private Long identityDocumentId;

    @NotNull(message = "Debe indicar si presenta síntomas")
    @Schema(description = "Indica si el viajero presenta síntomas compatibles con enfermedades contagiosas", example = "false")
    private Boolean hasSymptoms;

    @Schema(description = "Descripción detallada de los síntomas (obligatorio solo si hasSymptoms es true)", example = "Dolor de cabeza leve y fiebre baja")
    private String symptomsDescription;

    @NotNull(message = "Debe indicar si tuvo contacto estrecho")
    @Schema(description = "Indica si el viajero tuvo contacto reciente con personas diagnosticadas con enfermedades contagiosas", example = "false")
    private Boolean hasRecentContact;

    @NotBlank(message = "El estado de vacunación es obligatorio")
    @Schema(description = "Estado actual del esquema de vacunación del viajero", example = "ESQUEMA_COMPLETO")
    private String vaccinationStatus;

    @Schema(description = "Estado inicial de la declaración de salud", example = "PENDIENTE")
    private String status;

    @Schema(description = "Observaciones adicionales por parte del viajero o del agente inicial", example = "Pasajero presenta pase de movilidad internacional vigente.")
    private String observations;
}