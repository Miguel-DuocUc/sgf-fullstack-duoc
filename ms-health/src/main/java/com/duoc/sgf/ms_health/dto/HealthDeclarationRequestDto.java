package com.duoc.sgf.ms_health.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HealthDeclarationRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id del documento de identidad es obligatorio")
    private Long identityDocumentId;

    @NotNull(message = "Debe indicar si presenta síntomas")
    private Boolean hasSymptoms;

    private String symptomsDescription;

    @NotNull(message = "Debe indicar si tuvo contacto reciente")
    private Boolean hasRecentContact;

    @NotBlank(message = "El estado de vacunación es obligatorio")
    private String vaccinationStatus;

    private String status;

    private String observations;
}