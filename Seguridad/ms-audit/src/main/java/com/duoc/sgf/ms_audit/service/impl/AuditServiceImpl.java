package com.duoc.sgf.ms_audit.service.impl;

import com.duoc.sgf.ms_audit.model.Auditoria;
import com.duoc.sgf.ms_audit.repository.AuditoriaRepository;
import com.duoc.sgf.ms_audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditoriaRepository auditoriaRepository;

    @Override
    public Auditoria registrarAuditoria(Auditoria auditoria) {
        return auditoriaRepository.save(auditoria);
    }

    @Override
    public List<Auditoria> obtenerHistorial() {
        return auditoriaRepository.findAll();
    }

    @Override
    public Auditoria obtenerPorId(Long id) {
        return auditoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El registro de auditoría con ID " + id + " no existe"));
    }
}