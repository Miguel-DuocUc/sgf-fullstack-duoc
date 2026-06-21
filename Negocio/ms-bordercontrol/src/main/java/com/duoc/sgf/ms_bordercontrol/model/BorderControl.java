package com.duoc.sgf.ms_bordercontrol.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "BORDER_CONTROLS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorderControl {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "border_control_seq")
    @SequenceGenerator(name = "border_control_seq", sequenceName = "SEQ_BORDER_CONTROLS", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "identity_document_id", nullable = false)
    private Long identityDocumentId;

    @Column(name = "visa_request_id", nullable = false)
    private Long visaRequestId;

    @Column(name = "health_declaration_id", nullable = false)
    private Long healthDeclarationId;

    @Column(name = "logistics_checkpoint_id", nullable = false)
    private Long logisticsCheckpointId;

    @Column(name = "officer_name", nullable = false, length = 100)
    private String officerName;

    @Column(name = "movement_type", nullable = false, length = 30)
    private String movementType;

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
    }
}