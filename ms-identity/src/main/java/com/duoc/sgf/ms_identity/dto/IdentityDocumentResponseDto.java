package com.duoc.sgf.ms_identity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDocumentResponseDto {

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