package com.duoc.sgf.ms_bordercontrol.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthDeclarationBasicDto {

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