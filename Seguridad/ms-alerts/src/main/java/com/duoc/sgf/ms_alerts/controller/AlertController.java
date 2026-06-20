package com.duoc.sgf.ms_alerts.controller;

import com.duoc.sgf.ms_alerts.model.dto.AlertRequestDto;
import com.duoc.sgf.ms_alerts.model.dto.AlertResponseDto;
import com.duoc.sgf.ms_alerts.model.Alert;
import com.duoc.sgf.ms_alerts.model.mapper.AlertMapper; // Importamos tu mapper
import com.duoc.sgf.ms_alerts.service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Agregado para consistencia
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final AlertMapper alertMapper;
    @PostMapping
    public ResponseEntity<AlertResponseDto> crearAlerta(@Valid @RequestBody AlertRequestDto request) {
        Alert alert = alertMapper.toEntity(request);
        Alert nuevaAlerta = alertService.crearAlerta(alert);
        return ResponseEntity.status(HttpStatus.CREATED).body(alertMapper.toDto(nuevaAlerta));
    }

    @GetMapping
    public ResponseEntity<List<AlertResponseDto>> listarAlertas() {
        List<AlertResponseDto> response = alertService.obtenerAlertasActivas()
                .stream()
                .map(alertMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pasaporte/{pasaporte}")
    public ResponseEntity<List<AlertResponseDto>> verificarPasaporte(@PathVariable String pasaporte) {
        List<AlertResponseDto> response = alertService.verificarPasaporte(pasaporte)
                .stream()
                .map(alertMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<AlertResponseDto> desactivarAlerta(@PathVariable Long id) {
        Alert alertaDesactivada = alertService.desactivarAlerta(id);
        return ResponseEntity.ok(alertMapper.toDto(alertaDesactivada));
    }
}