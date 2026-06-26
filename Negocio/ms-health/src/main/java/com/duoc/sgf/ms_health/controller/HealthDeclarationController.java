package com.duoc.sgf.ms_health.controller;

import com.duoc.sgf.ms_health.model.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationResponseDto;
import com.duoc.sgf.ms_health.service.HealthDeclarationService;
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
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
@Tag(name = "Salud S.D.G.F.", description = "Gestión de declaraciones juradas de salud y control sanitario en la frontera.")
public class HealthDeclarationController {

    private final HealthDeclarationService healthDeclarationService;

    @GetMapping
    @Operation(summary = "Listar todas las declaraciones", description = "Retorna el historial completo de todas las declaraciones juradas de salud ingresadas al sistema.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<HealthDeclarationResponseDto>> findAll() {
        return ResponseEntity.ok(healthDeclarationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener declaración por ID", description = "Busca una declaración de salud específica utilizando su identificador único interno.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Declaración encontrada."),
            @ApiResponse(responseCode = "404", description = "La declaración no existe.")
    })
    public ResponseEntity<HealthDeclarationResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(healthDeclarationService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener declaraciones por usuario", description = "Retorna todas las declaraciones de salud asociadas a un ciudadano específico.")
    @ApiResponse(responseCode = "200", description = "Listado de declaraciones del usuario obtenido.")
    public ResponseEntity<List<HealthDeclarationResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(healthDeclarationService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filtrar por estado", description = "Retorna un listado de declaraciones según su estado de evaluación (ej. PENDIENTE, APROBADA, CUARENTENA).")
    @ApiResponse(responseCode = "200", description = "Filtrado aplicado correctamente.")
    public ResponseEntity<List<HealthDeclarationResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(healthDeclarationService.findByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva declaración", description = "Ingresa una nueva declaración jurada de salud al ecosistema fronterizo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Declaración registrada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.")
    })
    public ResponseEntity<HealthDeclarationResponseDto> create(@Valid @RequestBody HealthDeclarationRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(healthDeclarationService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar declaración", description = "Modifica los datos de una declaración de salud existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Declaración actualizada correctamente."),
            @ApiResponse(responseCode = "404", description = "La declaración a actualizar no existe."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.")
    })
    public ResponseEntity<HealthDeclarationResponseDto> update(@PathVariable Long id,
                                                               @Valid @RequestBody HealthDeclarationRequestDto request) {
        return ResponseEntity.ok(healthDeclarationService.update(id, request));
    }

    @PatchMapping("/{id}/evaluate")
    @Operation(summary = "Evaluar declaración", description = "Cambia el estado de la declaración tras la revisión de un oficial de salud (ROLE_HEALTH_OFFICER).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Declaración evaluada exitosamente."),
            @ApiResponse(responseCode = "404", description = "La declaración no existe.")
    })
    public ResponseEntity<HealthDeclarationResponseDto> evaluate(@PathVariable Long id) {
        return ResponseEntity.ok(healthDeclarationService.evaluate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar declaración", description = "Elimina físicamente una declaración del sistema o la inactiva de forma lógica.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Declaración eliminada sin contenido de retorno."),
            @ApiResponse(responseCode = "404", description = "La declaración a eliminar no existe.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        healthDeclarationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}