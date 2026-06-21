package com.duoc.sgf.ms_audit.model.mapper;

import com.duoc.sgf.ms_audit.model.Auditoria;
import com.duoc.sgf.ms_audit.model.dto.AuditoriaRequestDto;
import com.duoc.sgf.ms_audit.model.dto.AuditoriaResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditMapper {


    public Auditoria toEntity(AuditoriaRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        Auditoria auditoria = new Auditoria();
        auditoria.setMensaje(requestDto.getMensaje());
        auditoria.setEmitidoPor(requestDto.getEmitidoPor());
        auditoria.setFechaRegistro(LocalDateTime.now());

        return auditoria;
    }

    public AuditoriaResponseDto toDto(Auditoria auditoria) {
        if (auditoria == null) {
            return null;
        }

        return AuditoriaResponseDto.builder()
                .id(auditoria.getId())
                .mensaje(auditoria.getMensaje())
                .emitidoPor(auditoria.getEmitidoPor())
                .fechaRegistro(auditoria.getFechaRegistro())
                .build();
    }
}