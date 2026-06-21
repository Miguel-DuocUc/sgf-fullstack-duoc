package com.duoc.sgf.ms_health.service;

import com.duoc.sgf.ms_health.model.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationResponseDto;

import java.util.List;

public interface HealthDeclarationService {

    List<HealthDeclarationResponseDto> findAll();
    HealthDeclarationResponseDto findById(Long id);
    List<HealthDeclarationResponseDto> findByUserId(Long userId);
    List<HealthDeclarationResponseDto> findByStatus(String status);
    HealthDeclarationResponseDto create(HealthDeclarationRequestDto request);
    HealthDeclarationResponseDto update(Long id, HealthDeclarationRequestDto request);
    HealthDeclarationResponseDto evaluate(Long id);

    void delete(Long id);
}