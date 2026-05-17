package com.duoc.sgf.ms_health.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthDeclarationResponseDto {

    private Long id;
    private Long userId;
    private Long identityDocumentId;
    private Boolean hasSymptoms;
    private String symptomsDescription;
    private Boolean hasRecentContact;
    private String vaccinationStatus;
    private String riskLevel;
    private String status;
    private String observations;
    private LocalDateTime createdAt;
}