package com.duoc.sgf.ms_bordercontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorderControlResponseDto {

    private Long id;
    private Long userId;
    private Long identityDocumentId;
    private Long visaRequestId;
    private Long healthDeclarationId;
    private String checkpoint;
    private String officerName;
    private String movementType;
    private String status;
    private String observations;
    private LocalDateTime createdAt;
}