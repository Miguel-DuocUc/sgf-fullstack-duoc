package com.duoc.sgf.ms_bordercontrol.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos requeridos para registrar un intento de cruce (entrada o salida) en un puesto fronterizo.")
public class BorderControlRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    @Schema(description = "Identificador único del ciudadano que intenta cruzar", example = "1")
    private Long userId;

    @NotNull(message = "El id del documento de identidad es obligatorio")
    @Schema(description = "Identificador del documento presentado (ej. pasaporte)", example = "42")
    private Long identityDocumentId;

    @NotNull(message = "El id de la solicitud de visa es obligatorio")
    @Schema(description = "Identificador de la visa validada para este cruce", example = "105")
    private Long visaRequestId;

    @NotNull(message = "El id de la declaración sanitaria es obligatorio")
    @Schema(description = "Identificador de la declaración jurada de salud vigente", example = "88")
    private Long healthDeclarationId;

    @NotNull(message = "El id del punto de control es obligatorio")
    @Schema(description = "Identificador del puesto logístico por donde se realiza el cruce", example = "3")
    private Long logisticsCheckpointId;

    @NotBlank(message = "El nombre del funcionario es obligatorio")
    @Schema(description = "Nombre o placa del agente de frontera a cargo de la evaluación", example = "Inspector H. Morales")
    private String officerName;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Schema(description = "Dirección del tránsito fronterizo", example = "ENTRADA")
    private String movementType;

    @Schema(description = "Estado de la evaluación del control fronterizo", example = "EN_REVISION")
    private String status;

    @Schema(description = "Observaciones de la inspección en ventanilla", example = "Documentación en regla, se procede a control de equipaje.")
    private String observations;
}