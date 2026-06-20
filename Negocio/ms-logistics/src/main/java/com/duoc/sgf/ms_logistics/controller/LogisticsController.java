package com.duoc.sgf.ms_logistics.controller;

import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.service.LogisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsService service;

    @PostMapping
    public ResponseEntity<PuestoResponseDto> crear(@Valid @RequestBody PuestoRequestDto request) { // <-- @Valid agregado
        PuestoResponseDto nuevoPuesto = service.crearPuesto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPuesto);
    }

    @GetMapping
    public ResponseEntity<List<PuestoResponseDto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuestoResponseDto> buscarPorId(@PathVariable Long id) {
        PuestoResponseDto dto = service.buscarporId(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PuestoResponseDto> actualizar(@PathVariable Long id, @Valid @RequestBody PuestoRequestDto request) { // <-- @Valid agregado
        PuestoResponseDto dto = service.actualizarPuesto(id, request);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Long id) {
        boolean eliminado = service.eliminarPuesto(id);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}