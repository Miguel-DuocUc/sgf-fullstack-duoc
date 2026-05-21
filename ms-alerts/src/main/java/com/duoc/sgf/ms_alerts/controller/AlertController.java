package com.duoc.sgf.ms_alerts.controller;

import com.duoc.sgf.ms_alerts.dto.AlertRequestDto;
import com.duoc.sgf.ms_alerts.dto.AlertResponseDto;
import com.duoc.sgf.ms_alerts.model.Alert;
import com.duoc.sgf.ms_alerts.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<AlertResponseDto> crearAlerta(@Valid @RequestBody AlertRequestDto request) {
        Alert alert = new Alert();
        alert.setPasaporteCiudadano(request.getPasaporteCiudadano());
        alert.setNombreCompleto(request.getNombreCompleto());
        alert.setTipoAlerta(request.getTipoAlerta());
        alert.setMotivo(request.getMotivo());
        alert.setEmitidoPor(request.getEmitidoPor());

        Alert nuevaAlerta = alertService.crearAlerta(alert);

        AlertResponseDto response = convertToDto(nuevaAlerta);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<AlertResponseDto>> listarAlertas() {
        List<AlertResponseDto> response = alertService.obtenerAlertasActivas()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    @GetMapping("/pasaporte/{pasaporte}")
    public ResponseEntity<List<AlertResponseDto>> verificarPasaporte(@PathVariable String pasaporte) {
        List<Alert> alertas = alertService.verificarPasaporte(pasaporte);
        List<AlertResponseDto> response = alertas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    private AlertResponseDto convertToDto(Alert alert) {
        AlertResponseDto dto = new AlertResponseDto();
        dto.setId(alert.getId());
        dto.setPasaporteCiudadano(alert.getPasaporteCiudadano());
        dto.setNombreCompleto(alert.getNombreCompleto());
        dto.setTipoAlerta(alert.getTipoAlerta());
        dto.setMotivo(alert.getMotivo());
        dto.setEmitidoPor(alert.getEmitidoPor());
        dto.setFechaCreacion(alert.getFechaCreacion());
        dto.setActiva(alert.isActiva());
        return dto;
    }
}