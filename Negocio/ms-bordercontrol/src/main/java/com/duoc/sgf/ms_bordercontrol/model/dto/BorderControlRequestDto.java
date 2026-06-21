package com.duoc.sgf.ms_bordercontrol.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorderControlRequestDto {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id del documento de identidad es obligatorio")
    private Long identityDocumentId;

    @NotNull(message = "El id de la solicitud de visa es obligatorio")
    private Long visaRequestId;

    @NotNull(message = "El id de la declaración sanitaria es obligatorio")
    private Long healthDeclarationId;

    @NotNull(message = "El id de el punto de control es obligatorio")
    private Long logisticsCheckpointId;

    @NotBlank(message = "El nombre del funcionario es obligatorio")
    private String officerName;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String movementType;

    private String status;

    private String observations;
}