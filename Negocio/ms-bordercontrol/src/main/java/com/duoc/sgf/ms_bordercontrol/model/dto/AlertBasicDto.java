package com.duoc.sgf.ms_bordercontrol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertBasicDto {
    private String tipoAlerta;
    private String motivo;
    private boolean activa;
}