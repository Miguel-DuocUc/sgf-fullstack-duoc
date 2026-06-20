package com.duoc.sgf.ms_alerts.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertRequestDto {

    @NotBlank(message = "El pasaporte es obligatorio")
    private String pasaporteCiudadano;

    private String nombreCompleto;

    @NotBlank(message = "El tipo de alerta es obligatorio")
    private String tipoAlerta;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    @NotBlank(message = "El emisor es obligatorio")
    private String emitidoPor;
}