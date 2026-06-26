package com.duoc.sgf.ms_logistics.controller;

import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.service.LogisticsService;
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
@RequestMapping("/api/v1/logistics")
@RequiredArgsConstructor
@Tag(name = "Logística S.D.G.F.", description = "Gestión de puestos de control, recursos y asignaciones logísticas en la frontera.")
public class LogisticsController {

    private final LogisticsService service;

    @PostMapping
    @Operation(summary = "Crear puesto de control", description = "Registra un nuevo puesto logístico en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Puesto creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.")
    })
    public ResponseEntity<PuestoResponseDto> crear(@Valid @RequestBody PuestoRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearPuesto(request));
    }

    @GetMapping
    @Operation(summary = "Listar puestos", description = "Obtiene todos los puestos logísticos registrados y activos.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<PuestoResponseDto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodo());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar puesto por ID", description = "Busca un puesto específico mediante su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Puesto encontrado."),
            @ApiResponse(responseCode = "404", description = "Puesto logístico no encontrado.")
    })
    public ResponseEntity<PuestoResponseDto> buscarPorId(@PathVariable Long id) {
        // El servicio se encarga de lanzar la excepción si no lo encuentra. ¡Mira qué limpio queda!
        return ResponseEntity.ok(service.buscarporId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar puesto", description = "Modifica los datos de un puesto logístico existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Puesto actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "Puesto logístico no encontrado.")
    })
    public ResponseEntity<PuestoResponseDto> actualizar(@PathVariable Long id, @Valid @RequestBody PuestoRequestDto request) {
        return ResponseEntity.ok(service.actualizarPuesto(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar puesto", description = "Elimina un puesto logístico del sistema (o lo inactiva).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Puesto eliminado correctamente (Sin contenido)."),
            @ApiResponse(responseCode = "404", description = "Puesto logístico no encontrado.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarPuesto(id);
        // Cuando eliminas algo con éxito, el estándar REST es devolver 204 No Content
        return ResponseEntity.noContent().build();
    }
}