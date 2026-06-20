package com.duoc.sgf.ms_audit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaResponseDto {
    private Long id;
    private String mensaje;
    private String emitidoPor;
    private LocalDateTime fechaRegistro;
}