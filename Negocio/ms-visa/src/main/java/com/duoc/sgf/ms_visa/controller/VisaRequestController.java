package com.duoc.sgf.ms_visa.controller;

import com.duoc.sgf.ms_visa.model.dto.VisaRequestDto;
import com.duoc.sgf.ms_visa.model.dto.VisaResponseDto;
import com.duoc.sgf.ms_visa.service.VisaRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visas")
@RequiredArgsConstructor
@Tag(name = "Visados S.D.G.F.", description = "Gestión integral de solicitudes, aprobaciones y rechazos de visas fronterizas.")
public class VisaRequestController {

    private final VisaRequestService visaRequestService;

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes", description = "Retorna el historial completo de todas las solicitudes de visa ingresadas al sistema.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<VisaResponseDto>> findAll() {
        return ResponseEntity.ok(visaRequestService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud por ID", description = "Busca una solicitud de visa específica utilizando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada."),
            @ApiResponse(responseCode = "404", description = "La solicitud no existe en el sistema.")
    })
    public ResponseEntity<VisaResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(visaRequestService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener visas por usuario", description = "Retorna todas las solicitudes de visa asociadas a un ciudadano específico.")
    @ApiResponse(responseCode = "200", description = "Listado de visas del usuario obtenido.")
    public ResponseEntity<List<VisaResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(visaRequestService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filtrar por estado", description = "Retorna un listado de visas según su estado actual (ej. PENDIENTE, APROBADA, RECHAZADA).")
    @ApiResponse(responseCode = "200", description = "Filtrado aplicado correctamente.")
    public ResponseEntity<List<VisaResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(visaRequestService.findByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Ingresar nueva solicitud", description = "Registra una nueva petición de visa en el sistema fronterizo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud de visa creada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no son válidos.")
    })
    public ResponseEntity<VisaResponseDto> create(@Valid @RequestBody VisaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(visaRequestService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar solicitud", description = "Modifica los datos completos de una solicitud de visa existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud actualizada correctamente."),
            @ApiResponse(responseCode = "404", description = "La solicitud a actualizar no existe.")
    })
    public ResponseEntity<VisaResponseDto> update(@PathVariable Long id, @Valid @RequestBody VisaRequestDto request) {
        return ResponseEntity.ok(visaRequestService.update(id, request));
    }

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Aprobar visa", description = "Cambia el estado de la solicitud a APROBADA. Operación exclusiva para agentes autorizados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visa aprobada exitosamente."),
            @ApiResponse(responseCode = "404", description = "La solicitud no existe.")
    })
    public ResponseEntity<VisaResponseDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(visaRequestService.approve(id));
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Rechazar visa", description = "Cambia el estado de la solicitud a RECHAZADA. Operación exclusiva para agentes autorizados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visa rechazada exitosamente."),
            @ApiResponse(responseCode = "404", description = "La solicitud no existe.")
    })
    public ResponseEntity<VisaResponseDto> reject(@PathVariable Long id) {
        return ResponseEntity.ok(visaRequestService.reject(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar solicitud", description = "Borra físicamente una solicitud de visa del sistema (usar con precaución).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Solicitud eliminada sin contenido de retorno."),
            @ApiResponse(responseCode = "404", description = "La solicitud a eliminar no existe.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visaRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}