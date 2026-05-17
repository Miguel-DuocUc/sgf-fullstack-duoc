package com.duoc.sgf.ms_bordercontrol.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VisaRequestBasicDto {

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