package com.duoc.sgf.ms_identity.controller;

import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentRequestDto;
import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentResponseDto;
import com.duoc.sgf.ms_identity.service.IdentityDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/identity")
@RequiredArgsConstructor
public class IdentityDocumentController {

    private final IdentityDocumentService identityDocumentService;

    @GetMapping
    public ResponseEntity<List<IdentityDocumentResponseDto>> findAll() {
        return ResponseEntity.ok(identityDocumentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdentityDocumentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(identityDocumentService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IdentityDocumentResponseDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(identityDocumentService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IdentityDocumentResponseDto>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(identityDocumentService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<IdentityDocumentResponseDto> create(@Valid @RequestBody IdentityDocumentRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(identityDocumentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdentityDocumentResponseDto> update(@PathVariable Long id,
                                                              @Valid @RequestBody IdentityDocumentRequestDto request) {
        return ResponseEntity.ok(identityDocumentService.update(id, request));
    }

    @PatchMapping("/{id}/validate")
    public ResponseEntity<IdentityDocumentResponseDto> validateDocument(@PathVariable Long id) {
        return ResponseEntity.ok(identityDocumentService.validateDocument(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        identityDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}