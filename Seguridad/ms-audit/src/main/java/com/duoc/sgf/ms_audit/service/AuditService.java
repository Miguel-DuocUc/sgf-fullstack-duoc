package com.duoc.sgf.ms_audit.service;

import com.duoc.sgf.ms_audit.model.Auditoria;
import com.duoc.sgf.ms_audit.model.dto.AuditoriaResponseDto;

import java.util.List;

public interface AuditService {

    Auditoria registrarAuditoria(Auditoria auditoria);
    List<AuditoriaResponseDto> obtenerHistorial();
    AuditoriaResponseDto obtenerPorId(Long id);

}