package com.duoc.sgf.ms_alerts.controller;

import com.duoc.sgf.ms_alerts.model.dto.AlertRequestDto;
import com.duoc.sgf.ms_alerts.model.dto.AlertResponseDto;
import com.duoc.sgf.ms_alerts.service.AlertService;
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
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Alertas S.D.G.F.", description = "Gestión de alertas de seguridad, órdenes de arraigo y restricciones de paso fronterizo.")
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    @Operation(summary = "Crear nueva alerta", description = "Registra una nueva alerta de seguridad en el sistema para un individuo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alerta creada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.")
    })
    public ResponseEntity<AlertResponseDto> crearAlerta(@Valid @RequestBody AlertRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertService.crearAlerta(request));
    }

    @GetMapping
    @Operation(summary = "Listar alertas activas", description = "Retorna un listado de todas las alertas de seguridad que se encuentran actualmente vigentes.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<AlertResponseDto>> listarAlertas() {
        return ResponseEntity.ok(alertService.obtenerAlertasActivas());
    }

    @GetMapping("/pasaporte/{pasaporte}")
    @Operation(summary = "Verificar pasaporte", description = "Consulta si existe alguna alerta activa asociada a un número de pasaporte específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontraron alertas para este pasaporte.")
    })
    public ResponseEntity<List<AlertResponseDto>> verificarPasaporte(@PathVariable String pasaporte) {
        return ResponseEntity.ok(alertService.verificarPasaporte(pasaporte));
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar alerta", description = "Cambia el estado de una alerta a inactiva (ej. cuando se resuelve una orden judicial).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta desactivada correctamente."),
            @ApiResponse(responseCode = "404", description = "La alerta especificada no existe.")
    })
    public ResponseEntity<AlertResponseDto> desactivarAlerta(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.desactivarAlerta(id));
    }
}