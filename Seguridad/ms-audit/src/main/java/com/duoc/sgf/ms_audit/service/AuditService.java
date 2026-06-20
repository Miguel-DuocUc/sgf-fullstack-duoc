package com.duoc.sgf.ms_audit.service;

import com.duoc.sgf.ms_audit.model.Auditoria;
import java.util.List;

public interface AuditService {
    Auditoria registrarAuditoria(Auditoria auditoria);
    List<Auditoria> obtenerHistorial();
    Auditoria obtenerPorId(Long id);
}