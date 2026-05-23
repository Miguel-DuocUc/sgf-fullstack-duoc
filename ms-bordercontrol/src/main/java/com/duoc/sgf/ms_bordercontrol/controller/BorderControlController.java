package com.duoc.sgf.ms_bordercontrol.controller;

import com.duoc.sgf.ms_bordercontrol.dto.BorderControlRequestDto;
import com.duoc.sgf.ms_bordercontrol.dto.BorderControlResponseDto;
import com.duoc.sgf.ms_bordercontrol.service.BorderControlService;
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
public class BorderControlController {

    private final BorderControlService borderControlService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping
    public ResponseEntity<List<BorderControlResponseDto>> findAll() {
        return ResponseEntity.ok(borderControlService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorderControlResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(borderControlService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorderControlResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(borderControlService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorderControlResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(borderControlService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<BorderControlResponseDto> create(@Valid @RequestBody BorderControlRequestDto request) {
        BorderControlResponseDto response = borderControlService.create(request);
        kafkaTemplate.send("border-events", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorderControlResponseDto> update(@PathVariable Long id,
                                                           @Valid @RequestBody BorderControlRequestDto request) {
        return ResponseEntity.ok(borderControlService.update(id, request));
    }

    @PatchMapping("/{id}/evaluate")
    public ResponseEntity<BorderControlResponseDto> evaluate(@PathVariable Long id) {
        return ResponseEntity.ok(borderControlService.evaluate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        borderControlService.delete(id);
        return ResponseEntity.noContent().build();
    }
}