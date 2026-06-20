package com.duoc.sgf.ms_logistics.model.mapper;

import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import org.springframework.stereotype.Component;

@Component
public class PuestoMapper {

    public PuestoFronterizo toEntity(PuestoRequestDto requestDto) {
        return PuestoFronterizo.builder()
                .name(requestDto.getName())
                .direccion(requestDto.getDireccion())
                .cantPersonMax(requestDto.getCantPersonMax())
                .guardPerson(requestDto.getGuardPerson())
                .estadoOperativo(requestDto.getEstadoOperativo())
                .build();
    }

    public PuestoResponseDto toDto(PuestoFronterizo entidad) {
        return PuestoResponseDto.builder()
                .id(entidad.getId())
                .name(entidad.getName())
                .direccion(entidad.getDireccion())
                .cantPersonMax(entidad.getCantPersonMax())
                .guardPerson(entidad.getGuardPerson())
                .estadoOperativo(entidad.getEstadoOperativo())
                .build();
    }
}