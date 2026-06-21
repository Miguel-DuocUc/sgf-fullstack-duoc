package com.duoc.sgf.ms_visa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaResponseDto {

    private Long id;
    private Long userId;
    private Long identityDocumentId;
    private String visaType;
    private String destinationCountry;
    private String travelPurpose;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String observations;
    private LocalDateTime createdAt;
}