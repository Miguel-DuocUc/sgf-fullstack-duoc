package com.duoc.sgf.ms_audit.service.impl;

import com.duoc.sgf.ms_audit.model.Auditoria;
import com.duoc.sgf.ms_audit.model.dto.AuditoriaResponseDto;
import com.duoc.sgf.ms_audit.model.mapper.AuditMapper;
import com.duoc.sgf.ms_audit.repository.AuditoriaRepository;
import com.duoc.sgf.ms_audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditMapper auditMapper;

    @Override
    public Auditoria registrarAuditoria(Auditoria auditoria) {
        return auditoriaRepository.save(auditoria);
    }

    @Override
    public List<AuditoriaResponseDto> obtenerHistorial() {
        return auditoriaRepository.findAll()
                .stream()
                .map(auditMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuditoriaResponseDto obtenerPorId(Long id) {
        return auditoriaRepository.findById(id)
                .map(auditMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El registro de auditoría con ID " + id + " no existe"
                ));
    }
}