package com.duoc.sgf.ms_alerts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlertRequestDto {

    @NotBlank(message = "El pasaporte es obligatorio")
    @Size(max = 50, message = "El pasaporte no puede superar los 50 caracteres")
    private String pasaporteCiudadano;

    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El tipo de alerta es obligatorio")
    private String tipoAlerta;

    @NotBlank(message = "El motivo de la alerta es obligatorio")
    private String motivo;

    @NotBlank(message = "El identificador del emisor es obligatorio")
    private String emitidoPor;
}