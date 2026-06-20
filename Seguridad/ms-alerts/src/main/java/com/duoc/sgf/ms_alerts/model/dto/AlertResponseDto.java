package com.duoc.sgf.ms_alerts.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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