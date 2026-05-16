package com.duoc.sgf.ms_identity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "IDENTITY_DOCUMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identity_document_seq")
    @SequenceGenerator(name = "identity_document_seq", sequenceName = "SEQ_IDENTITY_DOCUMENTS", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;

    @Column(name = "document_number", nullable = false, unique = true, length = 30)
    private String documentNumber;

    @Column(name = "issuing_country", nullable = false, length = 80)
    private String issuingCountry;

    @Column(name = "holder_name", nullable = false, length = 80)
    private String holderName;

    @Column(name = "holder_last_name", nullable = false, length = 80)
    private String holderLastName;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Boolean minor;

    @Column(name = "notarized_authorization", nullable = false)
    private Boolean notarizedAuthorization;

    @Column(nullable = false, length = 30)
    private String status;

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

        if (minor == null) {
            minor = false;
        }

        if (notarizedAuthorization == null) {
            notarizedAuthorization = false;
        }
    }
}