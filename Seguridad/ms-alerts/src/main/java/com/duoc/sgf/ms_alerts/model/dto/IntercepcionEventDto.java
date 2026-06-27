package com.duoc.sgf.ms_alerts.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntercepcionEventDto {
    private String pasaporteCiudadano;
    private Long checkpointId;
    private String motivoBloqueo;
    private LocalDateTime fechaIntento;
}