package com.duoc.sgf.ms_visa.controller;

import com.duoc.sgf.ms_visa.dto.VisaRequestDto;
import com.duoc.sgf.ms_visa.dto.VisaResponseDto;
import com.duoc.sgf.ms_visa.service.VisaRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visa-requests")
@RequiredArgsConstructor
public class VisaRequestController {

    private final VisaRequestService visaRequestService;

    @GetMapping
    public ResponseEntity<List<VisaResponseDto>> findAll() {
        return ResponseEntity.ok(visaRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisaResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(visaRequestService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VisaResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(visaRequestService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VisaResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(visaRequestService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<VisaResponseDto> create(@Valid @RequestBody VisaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(visaRequestService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisaResponseDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody VisaRequestDto request) {
        return ResponseEntity.ok(visaRequestService.update(id, request));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<VisaResponseDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(visaRequestService.approve(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<VisaResponseDto> reject(@PathVariable Long id) {
        return ResponseEntity.ok(visaRequestService.reject(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visaRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}