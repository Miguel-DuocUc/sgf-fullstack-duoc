package com.duoc.sgf.ms_health.controller;

import com.duoc.sgf.ms_health.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.dto.HealthDeclarationResponseDto;
import com.duoc.sgf.ms_health.service.HealthDeclarationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthDeclarationController {

    private final HealthDeclarationService healthDeclarationService;

    @GetMapping
    public ResponseEntity<List<HealthDeclarationResponseDto>> findAll() {
        return ResponseEntity.ok(healthDeclarationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthDeclarationResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(healthDeclarationService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HealthDeclarationResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(healthDeclarationService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<HealthDeclarationResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(healthDeclarationService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<HealthDeclarationResponseDto> create(@Valid @RequestBody HealthDeclarationRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(healthDeclarationService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthDeclarationResponseDto> update(@PathVariable Long id,
                                                               @Valid @RequestBody HealthDeclarationRequestDto request) {
        return ResponseEntity.ok(healthDeclarationService.update(id, request));
    }

    @PatchMapping("/{id}/evaluate")
    public ResponseEntity<HealthDeclarationResponseDto> evaluate(@PathVariable Long id) {
        return ResponseEntity.ok(healthDeclarationService.evaluate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        healthDeclarationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}