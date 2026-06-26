package com.duoc.sgf.ms_identity.controller;

import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentRequestDto;
import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentResponseDto;
import com.duoc.sgf.ms_identity.service.IdentityDocumentService;
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
@RequestMapping("/api/v1/identity")
@RequiredArgsConstructor
@Tag(name = "Identidad S.D.G.F.", description = "Gestión, registro y validación de documentos de identidad fronterizos.")
public class IdentityDocumentController {

    private final IdentityDocumentService identityDocumentService;

    @GetMapping
    @Operation(summary = "Listar todos los documentos", description = "Retorna el registro completo de todos los documentos de identidad ingresados al sistema.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<IdentityDocumentResponseDto>> findAll() {
        return ResponseEntity.ok(identityDocumentService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener documento por ID", description = "Busca un documento de identidad específico utilizando su identificador único interno.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento encontrado."),
            @ApiResponse(responseCode = "404", description = "El documento no existe.")
    })
    public ResponseEntity<IdentityDocumentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(identityDocumentService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener documentos por usuario", description = "Retorna todos los documentos de identidad asociados a un ciudadano específico.")
    @ApiResponse(responseCode = "200", description = "Listado de documentos del usuario obtenido.")
    public ResponseEntity<List<IdentityDocumentResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(identityDocumentService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filtrar por estado", description = "Retorna un listado de documentos según su estado actual (ej. ACTIVO, VENCIDO, RETENIDO).")
    @ApiResponse(responseCode = "200", description = "Filtrado aplicado correctamente.")
    public ResponseEntity<List<IdentityDocumentResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(identityDocumentService.findByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo documento", description = "Ingresa un nuevo documento de identidad al ecosistema fronterizo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Documento registrado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.")
    })
    public ResponseEntity<IdentityDocumentResponseDto> create(@Valid @RequestBody IdentityDocumentRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(identityDocumentService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar documento", description = "Modifica los datos de un documento de identidad existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "El documento a actualizar no existe.")
    })
    public ResponseEntity<IdentityDocumentResponseDto> update(@PathVariable Long id,
                                                              @Valid @RequestBody IdentityDocumentRequestDto request) {
        return ResponseEntity.ok(identityDocumentService.update(id, request));
    }

    @PatchMapping("/{id}/validate")
    @Operation(summary = "Validar documento", description = "Cambia el estado del documento a VALIDADO. Operación exclusiva para agentes de identidad.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento validado exitosamente."),
            @ApiResponse(responseCode = "404", description = "El documento no existe.")
    })
    public ResponseEntity<IdentityDocumentResponseDto> validateDocument(@PathVariable Long id) {
        return ResponseEntity.ok(identityDocumentService.validateDocument(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar documento", description = "Elimina físicamente un documento del sistema o lo inactiva de forma lógica.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Documento eliminado sin contenido de retorno."),
            @ApiResponse(responseCode = "404", description = "El documento a eliminar no existe.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        identityDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}