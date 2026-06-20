package com.duoc.sgf.ms_audit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaRequestDto {
    private String mensaje;
    private String emitidoPor;
}