package com.duoc.sgf.ms_alerts.service.impl;

import com.duoc.sgf.ms_alerts.model.Alert;
import com.duoc.sgf.ms_alerts.model.Intercepcion;
import com.duoc.sgf.ms_alerts.model.dto.AlertRequestDto;
import com.duoc.sgf.ms_alerts.model.dto.AlertResponseDto;
import com.duoc.sgf.ms_alerts.model.mapper.AlertMapper;
import com.duoc.sgf.ms_alerts.repository.AlertRepository;
import com.duoc.sgf.ms_alerts.repository.IntercepcionRepository;
import com.duoc.sgf.ms_alerts.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final IntercepcionRepository intercepcionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AlertMapper alertMapper;

    @Override
    public AlertResponseDto crearAlerta(AlertRequestDto request) {
        Alert alerta = alertMapper.toEntity(request);

        // Tu lógica de negocio impecable
        alerta.setFechaCreacion(LocalDateTime.now());
        alerta.setActiva(true);

        Alert alertaGuardada = alertRepository.save(alerta);
        return alertMapper.toDto(alertaGuardada);
    }

    @Override
    public List<AlertResponseDto> obtenerAlertasActivas() {
        return alertRepository.findByActivaTrue()
                .stream()
                .map(alertMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponseDto> verificarPasaporte(String pasaporte) {
        return alertRepository.findByPasaporteCiudadanoAndActivaTrue(pasaporte)
                .stream()
                .map(alertMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AlertResponseDto desactivarAlerta(Long id) {
        Alert alerta = alertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La alerta con ID " + id + " no existe"));

        alerta.setActiva(false);
        Alert alertaActualizada = alertRepository.save(alerta);

        return alertMapper.toDto(alertaActualizada);
    }

    @Override
    public void registrarIntercepcion(Long alertaId, String pasaporte) {
        Alert alerta = alertRepository.findById(alertaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La alerta con ID " + alertaId + " no existe"));

        Intercepcion registro = Intercepcion.builder()
                .alerta(alerta)
                .pasaporteCiudadano(pasaporte)
                .fechaIntercepcion(LocalDateTime.now())
                .build();

        intercepcionRepository.save(registro);

        try {
            String mensajeEmergencia = "LOCKDOWN;" + pasaporte + ";" + alerta.getMotivo();
            kafkaTemplate.send("emergency-alerts", mensajeEmergencia);

            log.info(">> [MS-ALERTS] ¡Grito de emergencia enviado a Kafka! Cierren barreras.");

        } catch (Exception e) {
            log.error(">> [MS-ALERTS] Error crítico de comunicación: {}", e.getMessage());
        }
    }
}