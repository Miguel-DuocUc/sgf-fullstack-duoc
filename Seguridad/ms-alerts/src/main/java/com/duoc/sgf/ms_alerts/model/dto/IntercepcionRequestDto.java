package com.duoc.sgf.ms_alerts.model.dto;

import lombok.Data;

@Data
public class IntercepcionRequestDto {
    private Long alertaId;
    private String pasaporte;
    private String lugar;
    private String observaciones;
}