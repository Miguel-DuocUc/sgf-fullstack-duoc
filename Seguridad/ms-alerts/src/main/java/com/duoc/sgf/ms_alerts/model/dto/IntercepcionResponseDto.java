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
public class IntercepcionResponseDto {
    private Long id;
    private String pasaporte;
    private LocalDateTime fecha;
    private String lugar;
    private String observaciones;
    private String alertaTipo;
}