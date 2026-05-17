package com.duoc.sgf.ms_logistics.model.mapper;
import com.duoc.sgf.ms_logistics.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import org.springframework.stereotype.Component;

@Component
public class PuestoMapper {

    public PuestoFronterizo toEntity(PuestoRequestDto requestDto){

        PuestoFronterizo entidad = new PuestoFronterizo();
        entidad.setName(requestDto.getName());
        entidad.setDireccion(requestDto.getDireccion());
        entidad.setCantPersonMax(requestDto.getCantPersonMax());
        entidad.setGuardPerson(requestDto.getGuardPerson());
        entidad.setEstadoOperativo("OPERATIVO");
        return entidad;
    }
    public PuestoResponseDto toDto(PuestoFronterizo entidad){

        PuestoResponseDto dto = new PuestoResponseDto();
//
        dto.setId(entidad.getId());
        dto.setName(entidad.getName());
        dto.setDireccion(entidad.getDireccion());
        dto.setCantPersonMax(entidad.getCantPersonMax());
        dto.setGuardPerson(entidad.getGuardPerson());
        dto.setEstadoOperativo(entidad.getEstadoOperativo());

        return dto;
    }

}
