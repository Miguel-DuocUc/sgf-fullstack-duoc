package com.duoc.sgf.ms_audit.controller;

import com.duoc.sgf.ms_audit.model.dto.AuditoriaResponseDto;
import com.duoc.sgf.ms_audit.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Tag(name = "Auditoría S.D.G.F.", description = "Historial y trazabilidad de las operaciones críticas del sistema fronterizo.")
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @Operation(summary = "Obtener historial completo", description = "Retorna una lista con todos los registros de auditoría del sistema.")
    @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente.")
    public ResponseEntity<List<AuditoriaResponseDto>> obtenerHistorial() {
        return ResponseEntity.ok(auditService.obtenerHistorial());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro por ID", description = "Busca y retorna un evento de auditoría específico utilizando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado."),
            @ApiResponse(responseCode = "404", description = "El registro de auditoría no existe.")
    })
    public ResponseEntity<AuditoriaResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(auditService.obtenerPorId(id));
    }
}