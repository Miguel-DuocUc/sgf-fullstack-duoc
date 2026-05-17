package com.duoc.sgf.ms_health.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "HEALTH_DECLARATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthDeclaration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "health_declaration_seq")
    @SequenceGenerator(name = "health_declaration_seq", sequenceName = "SEQ_HEALTH_DECLARATIONS", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "identity_document_id", nullable = false)
    private Long identityDocumentId;

    @Column(name = "has_symptoms", nullable = false)
    private Boolean hasSymptoms;

    @Column(name = "symptoms_description", length = 250)
    private String symptomsDescription;

    @Column(name = "has_recent_contact", nullable = false)
    private Boolean hasRecentContact;

    @Column(name = "vaccination_status", nullable = false, length = 50)
    private String vaccinationStatus;

    @Column(name = "risk_level", nullable = false, length = 30)
    private String riskLevel;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 250)
    private String observations;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null || status.isBlank()) {
            status = "PENDIENTE";
        }

        if (riskLevel == null || riskLevel.isBlank()) {
            riskLevel = "BAJO";
        }

        if (hasSymptoms == null) {
            hasSymptoms = false;
        }

        if (hasRecentContact == null) {
            hasRecentContact = false;
        }
    }
}