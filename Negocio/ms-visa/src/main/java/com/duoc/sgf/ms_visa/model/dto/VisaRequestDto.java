package com.duoc.sgf.ms_visa.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Datos requeridos para solicitar la emisión o actualización de un visado fronterizo.")
public class VisaRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    @Schema(description = "Identificador único del ciudadano solicitante", example = "1")
    private Long userId;

    @NotNull(message = "El id del documento de identidad es obligatorio")
    @Schema(description = "Identificador del documento (ej. pasaporte) previamente registrado", example = "42")
    private Long identityDocumentId;

    @NotBlank(message = "El tipo de visa es obligatorio")
    @Schema(description = "Categoría del visado", example = "TURISMO")
    private String visaType;

    @NotBlank(message = "El país de destino es obligatorio")
    @Schema(description = "País hacia el cual se autoriza el paso", example = "Argentina")
    private String destinationCountry;

    @NotBlank(message = "El motivo del viaje es obligatorio")
    @Schema(description = "Justificación del tránsito fronterizo", example = "Vacaciones familiares")
    private String travelPurpose;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
    @Schema(description = "Fecha de inicio de vigencia de la visa", example = "2026-07-01")
    private LocalDate startDate;

    @NotNull(message = "La fecha de término es obligatoria")
    @Future(message = "La fecha de término debe ser futura")
    @Schema(description = "Fecha de expiración de la visa", example = "2026-08-01")
    private LocalDate endDate;

    @Schema(description = "Estado de la solicitud de visa", example = "PENDIENTE")
    private String status;

    @Schema(description = "Observaciones adicionales ingresadas por el agente de frontera", example = "Presenta pasaje de retorno confirmado.")
    private String observations;
}