package com.duoc.sgf.ms_audit.controller;

import com.duoc.sgf.ms_audit.model.dto.AuditoriaResponseDto;
import com.duoc.sgf.ms_audit.model.mapper.AuditMapper;
import com.duoc.sgf.ms_audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;
    private final AuditMapper auditMapper;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDto>> obtenerHistorial() {
        List<AuditoriaResponseDto> respuesta = auditService.obtenerHistorial()
                .stream()
                .map(auditMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDto> obtenerPorId(@PathVariable Long id) {
        var entidad = auditService.obtenerPorId(id);
        return ResponseEntity.ok(auditMapper.toDto(entidad));
    }
}