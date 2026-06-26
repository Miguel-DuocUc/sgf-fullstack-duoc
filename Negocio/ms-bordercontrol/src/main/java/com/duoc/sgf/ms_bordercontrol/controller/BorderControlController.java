package com.duoc.sgf.ms_bordercontrol.controller;

import com.duoc.sgf.ms_bordercontrol.model.dto.BorderControlRequestDto;
import com.duoc.sgf.ms_bordercontrol.model.dto.BorderControlResponseDto;
import com.duoc.sgf.ms_bordercontrol.service.BorderControlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/border-control")
@RequiredArgsConstructor
@Tag(name = "Control Fronterizo S.D.G.F.", description = "Gestión central de cruces, evaluación de tránsito y emisión de eventos fronterizos.")
public class BorderControlController {

    private final BorderControlService borderControlService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping
    @Operation(summary = "Listar todos los controles", description = "Retorna el historial completo de todos los movimientos y controles fronterizos registrados.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<BorderControlResponseDto>> findAll() {
        return ResponseEntity.ok(borderControlService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener control por ID", description = "Busca un registro de control fronterizo específico utilizando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Control fronterizo encontrado."),
            @ApiResponse(responseCode = "404", description = "El registro de control no existe.")
    })
    public ResponseEntity<BorderControlResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(borderControlService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener controles por usuario", description = "Retorna el historial de cruces y controles asociados a un ciudadano específico.")
    @ApiResponse(responseCode = "200", description = "Historial del usuario obtenido correctamente.")
    public ResponseEntity<List<BorderControlResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(borderControlService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filtrar por estado", description = "Retorna un listado de controles según su estado actual (ej. APROBADO, RECHAZADO, EN_REVISION).")
    @ApiResponse(responseCode = "200", description = "Filtrado aplicado correctamente.")
    public ResponseEntity<List<BorderControlResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(borderControlService.findByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Registrar cruce fronterizo", description = "Registra un nuevo intento de cruce. ATENCIÓN: Esta operación emite un evento a Kafka ('border-events') para notificar a los demás módulos del S.D.G.F.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Control registrado y evento emitido exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.")
    })
    public ResponseEntity<BorderControlResponseDto> create(@Valid @RequestBody BorderControlRequestDto request) {
        BorderControlResponseDto response = borderControlService.create(request);
        // Emisión del evento al bus de Kafka para que otros MS (como ms_alerts) reaccionen
        kafkaTemplate.send("border-events", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar registro de control", description = "Modifica los datos de un registro de control fronterizo existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "El registro a actualizar no existe.")
    })
    public ResponseEntity<BorderControlResponseDto> update(@PathVariable Long id,
                                                           @Valid @RequestBody BorderControlRequestDto request) {
        return ResponseEntity.ok(borderControlService.update(id, request));
    }

    @PatchMapping("/{id}/evaluate")
    @Operation(summary = "Evaluar control", description = "Evalúa y dictamina el resultado final de un control fronterizo. Operación para agentes de frontera.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Control evaluado exitosamente."),
            @ApiResponse(responseCode = "404", description = "El registro no existe.")
    })
    public ResponseEntity<BorderControlResponseDto> evaluate(@PathVariable Long id) {
        return ResponseEntity.ok(borderControlService.evaluate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar control", description = "Elimina físicamente un registro de control del sistema (operación sensible).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado sin contenido de retorno."),
            @ApiResponse(responseCode = "404", description = "El registro a eliminar no existe.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        borderControlService.delete(id);
        return ResponseEntity.noContent().build();
    }
}