package com.duoc.sgf.ms_bordercontrol.model.mapper;

import com.duoc.sgf.ms_bordercontrol.model.BorderControl;
import com.duoc.sgf.ms_bordercontrol.model.dto.BorderControlRequestDto;
import com.duoc.sgf.ms_bordercontrol.model.dto.BorderControlResponseDto;
import org.springframework.stereotype.Component;

@Component
public class BorderControlMapper {

    public BorderControl toEntity(BorderControlRequestDto dto) {
        BorderControl entity = new BorderControl();
        entity.setUserId(dto.getUserId());
        entity.setIdentityDocumentId(dto.getIdentityDocumentId());
        entity.setVisaRequestId(dto.getVisaRequestId());
        entity.setHealthDeclarationId(dto.getHealthDeclarationId());
        entity.setLogisticsCheckpointId(dto.getLogisticsCheckpointId());
        entity.setOfficerName(dto.getOfficerName());
        entity.setMovementType(dto.getMovementType());
        entity.setStatus(dto.getStatus());
        entity.setObservations(dto.getObservations());
        return entity;
    }

    public BorderControlResponseDto toDto(BorderControl entity) {
        return new BorderControlResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getIdentityDocumentId(),
                entity.getVisaRequestId(),
                entity.getHealthDeclarationId(),
                entity.getLogisticsCheckpointId(),
                entity.getOfficerName(),
                entity.getMovementType(),
                entity.getStatus(),
                entity.getObservations(),
                entity.getCreatedAt()
        );
    }
}