package com.duoc.sgf.ms_identity.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Datos requeridos para registrar o actualizar un documento de identidad en el sistema fronterizo.")
public class IdentityDocumentRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    @Schema(description = "Identificador único del ciudadano titular", example = "1")
    private Long userId;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Schema(description = "Tipo de documento presentado", example = "PASAPORTE")
    private String documentType;

    @NotBlank(message = "El número de documento es obligatorio")
    @Schema(description = "Número o código alfanumérico del documento", example = "P987654321")
    private String documentNumber;

    @NotBlank(message = "El país emisor es obligatorio")
    @Schema(description = "País que emitió el documento de identidad", example = "Chile")
    private String issuingCountry;

    @NotBlank(message = "El nombre del titular es obligatorio")
    @Schema(description = "Nombre(s) legal(es) del titular tal como aparece en el documento", example = "Andrea")
    private String holderName;

    @NotBlank(message = "El apellido del titular es obligatorio")
    @Schema(description = "Apellido(s) legal(es) del titular", example = "Rojas")
    private String holderLastName;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "El documento no puede estar vencido")
    @Schema(description = "Fecha de expiración del documento. No puede ser una fecha pasada.", example = "2032-10-15")
    private LocalDate expirationDate;

    @Schema(description = "Indica si el titular del documento es menor de edad", example = "false")
    private Boolean minor;

    @Schema(description = "Indica si el titular (en caso de ser menor) posee autorización notarial para el tránsito", example = "true")
    private Boolean notarizedAuthorization;

    @Schema(description = "Estado actual del documento en el sistema", example = "ACTIVO")
    private String status;
}