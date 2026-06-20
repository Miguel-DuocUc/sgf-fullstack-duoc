package com.duoc.sgf.ms_alerts.model.mapper;

import com.duoc.sgf.ms_alerts.model.dto.AlertRequestDto;
import com.duoc.sgf.ms_alerts.model.dto.AlertResponseDto;
import com.duoc.sgf.ms_alerts.model.Alert;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AlertMapper {

    public Alert toEntity(AlertRequestDto requestDto) {
        return Alert.builder()
                .pasaporteCiudadano(requestDto.getPasaporteCiudadano())
                .nombreCompleto(requestDto.getNombreCompleto())
                .tipoAlerta(requestDto.getTipoAlerta())
                .motivo(requestDto.getMotivo())
                .emitidoPor(requestDto.getEmitidoPor())
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    public AlertResponseDto toDto(Alert entidad) {
        return AlertResponseDto.builder()
                .id(entidad.getId())
                .pasaporteCiudadano(entidad.getPasaporteCiudadano())
                .nombreCompleto(entidad.getNombreCompleto())
                .tipoAlerta(entidad.getTipoAlerta())
                .motivo(entidad.getMotivo())
                .emitidoPor(entidad.getEmitidoPor())
                .fechaCreacion(entidad.getFechaCreacion())
                .activa(entidad.isActiva())
                .build();
    }
}