package com.duoc.sgf.ms_alerts.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertResponseDto {
    private Long id;
    private String pasaporteCiudadano;
    private String nombreCompleto;
    private String tipoAlerta;
    private String motivo;
    private String emitidoPor;
    private LocalDateTime fechaCreacion;
    private boolean activa;
}
