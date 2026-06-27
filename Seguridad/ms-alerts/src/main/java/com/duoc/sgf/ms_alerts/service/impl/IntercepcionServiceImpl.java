package com.duoc.sgf.ms_alerts.service.impl;

import com.duoc.sgf.ms_alerts.model.Alert;
import com.duoc.sgf.ms_alerts.model.Intercepcion;
import com.duoc.sgf.ms_alerts.model.dto.IntercepcionEventDto;
import com.duoc.sgf.ms_alerts.model.dto.IntercepcionResponseDto;
import com.duoc.sgf.ms_alerts.model.mapper.InterceptionMapper;
import com.duoc.sgf.ms_alerts.repository.AlertRepository;
import com.duoc.sgf.ms_alerts.repository.IntercepcionRepository;
import com.duoc.sgf.ms_alerts.service.IntercepcionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntercepcionServiceImpl implements IntercepcionService {

    private final AlertRepository alertRepository;
    private final IntercepcionRepository intercepcionRepository;
    private final InterceptionMapper interceptionMapper;

    @Override
    @Transactional
    public IntercepcionResponseDto registrarIntercepcion(IntercepcionEventDto evento) {
        log.info("Procesando registro de intercepción para pasaporte: {}", evento.getPasaporteCiudadano());

        List<Alert> alertas = alertRepository.findByPasaporteCiudadanoAndActivaTrue(evento.getPasaporteCiudadano());

        if (alertas.isEmpty()) {
            log.warn("Se recibió un evento de bloqueo para {}, pero no se encontró ninguna alerta activa vinculada.", evento.getPasaporteCiudadano());
            return null;
        }

        Intercepcion registro = Intercepcion.builder()
                .alerta(alertas.get(0))
                .pasaporteCiudadano(evento.getPasaporteCiudadano())
                .fechaIntercepcion(evento.getFechaIntento())
                .lugar("Checkpoint ID: " + evento.getCheckpointId())
                .observaciones(evento.getMotivoBloqueo())
                .build();

         Intercepcion guardado = intercepcionRepository.save(registro);

        log.info("Intercepción registrada exitosamente con ID: {}", guardado.getId());
        return interceptionMapper.toDto(guardado);
    }
}