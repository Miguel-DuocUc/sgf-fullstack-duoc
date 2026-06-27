package com.duoc.sgf.ms_alerts.model.mapper;

import com.duoc.sgf.ms_alerts.model.Intercepcion;
import com.duoc.sgf.ms_alerts.model.dto.IntercepcionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class InterceptionMapper {


    public IntercepcionResponseDto toDto(Intercepcion entity) {
        if (entity == null) {
            return null;
        }

        return IntercepcionResponseDto.builder()
                .id(entity.getId())
                .pasaporte(entity.getPasaporteCiudadano())
                .fecha(entity.getFechaIntercepcion())
                .lugar(entity.getLugar())
                .observaciones(entity.getObservaciones())
                .alertaTipo(entity.getAlerta() != null ? entity.getAlerta().getTipoAlerta() : "N/A")
                .build();
    }
}