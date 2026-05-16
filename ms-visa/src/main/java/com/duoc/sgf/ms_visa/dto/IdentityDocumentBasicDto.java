package com.duoc.sgf.ms_visa.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IdentityDocumentBasicDto {

    private Long id;
    private Long userId;
    private String documentType;
    private String documentNumber;
    private String issuingCountry;
    private String holderName;
    private String holderLastName;
    private LocalDate expirationDate;
    private Boolean minor;
    private Boolean notarizedAuthorization;
    private String status;
    private LocalDateTime createdAt;
}