package com.duoc.sgf.ms_health.model.mapper;

import com.duoc.sgf.ms_health.model.HealthDeclaration;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class HealthDeclarationMapper {

    public HealthDeclaration toEntity(HealthDeclarationRequestDto dto) {
        HealthDeclaration entity = new HealthDeclaration();
        entity.setUserId(dto.getUserId());
        entity.setIdentityDocumentId(dto.getIdentityDocumentId());
        entity.setHasSymptoms(dto.getHasSymptoms());
        entity.setSymptomsDescription(dto.getSymptomsDescription());
        entity.setHasRecentContact(dto.getHasRecentContact());
        entity.setVaccinationStatus(dto.getVaccinationStatus());
        entity.setStatus(dto.getStatus());
        entity.setObservations(dto.getObservations());
        return entity;
    }

    public HealthDeclarationResponseDto toDto(HealthDeclaration entity) {
        return new HealthDeclarationResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getIdentityDocumentId(),
                entity.getHasSymptoms(),
                entity.getSymptomsDescription(),
                entity.getHasRecentContact(),
                entity.getVaccinationStatus(),
                entity.getRiskLevel(),
                entity.getStatus(),
                entity.getObservations(),
                entity.getCreatedAt()
        );
    }
}