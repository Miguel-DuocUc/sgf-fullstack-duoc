package com.duoc.sgf.ms_visa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "VISA_REQUESTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisaRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visa_request_seq")
    @SequenceGenerator(name = "visa_request_seq", sequenceName = "SEQ_VISA_REQUESTS", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "identity_document_id", nullable = false)
    private Long identityDocumentId;

    @Column(name = "visa_type", nullable = false, length = 50)
    private String visaType;

    @Column(name = "destination_country", nullable = false, length = 80)
    private String destinationCountry;

    @Column(name = "travel_purpose", nullable = false, length = 150)
    private String travelPurpose;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

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