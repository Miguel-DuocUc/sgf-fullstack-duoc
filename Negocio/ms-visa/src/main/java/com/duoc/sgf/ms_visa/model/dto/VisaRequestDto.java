package com.duoc.sgf.ms_visa.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VisaRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id del documento de identidad es obligatorio")
    private Long identityDocumentId;

    @NotBlank(message = "El tipo de visa es obligatorio")
    private String visaType;

    @NotBlank(message = "El país de destino es obligatorio")
    private String destinationCountry;

    @NotBlank(message = "El motivo del viaje es obligatorio")
    private String travelPurpose;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
    private LocalDate startDate;

    @NotNull(message = "La fecha de término es obligatoria")
    @Future(message = "La fecha de término debe ser futura")
    private LocalDate endDate;

    private String status;

    private String observations;
}