package com.duoc.sgf.ms_visa.model.mapper;

import com.duoc.sgf.ms_visa.model.VisaRequest;
import com.duoc.sgf.ms_visa.model.dto.VisaRequestDto;
import com.duoc.sgf.ms_visa.model.dto.VisaResponseDto;
import org.springframework.stereotype.Component;

@Component
public class VisaRequestMapper {

    public VisaRequest toEntity(VisaRequestDto dto) {
        VisaRequest entity = new VisaRequest();
        entity.setUserId(dto.getUserId());
        entity.setIdentityDocumentId(dto.getIdentityDocumentId());
        entity.setVisaType(dto.getVisaType());
        entity.setDestinationCountry(dto.getDestinationCountry());
        entity.setTravelPurpose(dto.getTravelPurpose());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setStatus(dto.getStatus());
        entity.setObservations(dto.getObservations());
        return entity;
    }

    public VisaResponseDto toDto(VisaRequest entity) {
        return new VisaResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getIdentityDocumentId(),
                entity.getVisaType(),
                entity.getDestinationCountry(),
                entity.getTravelPurpose(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus(),
                entity.getObservations(),
                entity.getCreatedAt()
        );
    }
}