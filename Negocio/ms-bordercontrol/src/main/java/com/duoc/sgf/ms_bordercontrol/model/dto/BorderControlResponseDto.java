package com.duoc.sgf.ms_bordercontrol.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de respuesta con el registro consolidado e histórico de un control fronterizo.")
public class BorderControlResponseDto {

    @Schema(description = "Identificador único interno del control", example = "5001")
    private Long id;

    @Schema(description = "Identificador del ciudadano vinculado", example = "1")
    private Long userId;

    @Schema(description = "Identificador del documento de identidad validado", example = "42")
    private Long identityDocumentId;

    @Schema(description = "Identificador de la visa vinculada", example = "105")
    private Long visaRequestId;

    @Schema(description = "Identificador de la declaración de salud evaluada", example = "88")
    private Long healthDeclarationId;

    @Schema(description = "Identificador del puesto logístico", example = "3")
    private Long logisticsCheckpointId;

    @Schema(description = "Nombre del oficial que autorizó o rechazó el paso", example = "Inspector H. Morales")
    private String officerName;

    @Schema(description = "Dirección del tránsito", example = "ENTRADA")
    private String movementType;

    @Schema(description = "Estado y dictamen final del cruce", example = "APROBADO")
    private String status;

    @Schema(description = "Observaciones del oficial de frontera", example = "Tránsito autorizado. Sin novedades.")
    private String observations;

    @Schema(description = "Fecha y hora exacta en que se ejecutó el control fronterizo")
    private LocalDateTime createdAt;
}