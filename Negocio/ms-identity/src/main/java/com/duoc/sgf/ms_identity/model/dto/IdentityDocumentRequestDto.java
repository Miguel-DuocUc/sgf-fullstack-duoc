package com.duoc.sgf.ms_identity.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class IdentityDocumentRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType;

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    @NotBlank(message = "El país emisor es obligatorio")
    private String issuingCountry;

    @NotBlank(message = "El nombre del titular es obligatorio")
    private String holderName;

    @NotBlank(message = "El apellido del titular es obligatorio")
    private String holderLastName;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "El documento no puede estar vencido")
    private LocalDate expirationDate;

    private Boolean minor;

    private Boolean notarizedAuthorization;

    private String status;
}