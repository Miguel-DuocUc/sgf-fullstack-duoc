package com.duoc.sgf.ms_visa.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de respuesta con los datos consolidados de un visado en el sistema.")
public class VisaResponseDto {

    @Schema(description = "Identificador único interno de la solicitud de visa", example = "105")
    private Long id;

    @Schema(description = "Identificador del ciudadano vinculado", example = "1")
    private Long userId;

    @Schema(description = "Identificador del documento de identidad validado", example = "42")
    private Long identityDocumentId;

    @Schema(description = "Categoría del visado", example = "TURISMO")
    private String visaType;

    @Schema(description = "País hacia el cual se autoriza el paso", example = "Argentina")
    private String destinationCountry;

    @Schema(description = "Justificación del tránsito", example = "Vacaciones familiares")
    private String travelPurpose;

    @Schema(description = "Fecha de inicio de vigencia", example = "2026-07-01")
    private LocalDate startDate;

    @Schema(description = "Fecha de expiración", example = "2026-08-01")
    private LocalDate endDate;

    @Schema(description = "Estado actual de la visa en el sistema", example = "APROBADA")
    private String status;

    @Schema(description = "Observaciones del oficial a cargo", example = "Presenta pasaje de retorno confirmado.")
    private String observations;

    @Schema(description = "Fecha y hora exacta en que se registró la solicitud")
    private LocalDateTime createdAt;
}