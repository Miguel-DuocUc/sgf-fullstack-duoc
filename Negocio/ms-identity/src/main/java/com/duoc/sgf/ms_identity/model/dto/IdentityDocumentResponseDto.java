package com.duoc.sgf.ms_identity.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de respuesta con los datos consolidados y validados de un documento de identidad.")
public class IdentityDocumentResponseDto {

    @Schema(description = "Identificador único interno del documento", example = "42")
    private Long id;

    @Schema(description = "Identificador del ciudadano titular", example = "1")
    private Long userId;

    @Schema(description = "Tipo de documento", example = "PASAPORTE")
    private String documentType;

    @Schema(description = "Número o código del documento", example = "P987654321")
    private String documentNumber;

    @Schema(description = "País emisor", example = "Chile")
    private String issuingCountry;

    @Schema(description = "Nombre(s) legal(es) del titular", example = "Andrea")
    private String holderName;

    @Schema(description = "Apellido(s) legal(es) del titular", example = "Rojas")
    private String holderLastName;

    @Schema(description = "Fecha de expiración del documento", example = "2032-10-15")
    private LocalDate expirationDate;

    @Schema(description = "Bandera de minoría de edad", example = "false")
    private Boolean minor;

    @Schema(description = "Bandera de autorización notarial verificada", example = "true")
    private Boolean notarizedAuthorization;

    @Schema(description = "Estado de validación del documento", example = "VALIDADO")
    private String status;

    @Schema(description = "Fecha y hora en que se ingresó el documento a la base de datos")
    private LocalDateTime createdAt;
}