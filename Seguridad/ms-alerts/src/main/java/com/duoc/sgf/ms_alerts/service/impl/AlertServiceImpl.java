package com.duoc.sgf.ms_alerts.service.impl;

import com.duoc.sgf.ms_alerts.model.Alert;
import com.duoc.sgf.ms_alerts.model.Intercepcion;
import com.duoc.sgf.ms_alerts.repository.AlertRepository;
import com.duoc.sgf.ms_alerts.repository.IntercepcionRepository;
import com.duoc.sgf.ms_alerts.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final IntercepcionRepository intercepcionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Alert crearAlerta(Alert alerta) {
        alerta.setFechaCreacion(LocalDateTime.now());
        alerta.setActiva(true);
        return alertRepository.save(alerta);
    }

    @Override
    public List<Alert> obtenerAlertasActivas() {
        return alertRepository.findByActivaTrue();
    }

    @Override
    public List<Alert> verificarPasaporte(String pasaporte) {
        return alertRepository.findByPasaporteCiudadanoAndActivaTrue(pasaporte);
    }

    @Override
    public Alert desactivarAlerta(Long id) {
        Alert alerta = alertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La alerta con ID " + id + " no existe"));
        alerta.setActiva(false);
        return alertRepository.save(alerta);
    }

    @Override
    public void registrarIntercepcion(Alert alerta, String pasaporte) {
        Intercepcion registro = Intercepcion.builder()
                .alerta(alerta)
                .pasaporteCiudadano(pasaporte)
                .fechaIntercepcion(LocalDateTime.now())
                .build();

        intercepcionRepository.save(registro);

        try {
            String mensajeEmergencia = "LOCKDOWN;" + pasaporte + ";" + alerta.getMotivo();
            kafkaTemplate.send("emergency-alerts", mensajeEmergencia);
            System.out.println(">> [MS-ALERTS] ¡Grito de emergencia enviado a Kafka! Cierren barreras.");
        } catch (Exception e) {
            System.err.println(">> [MS-ALERTS] Error crítico de comunicación: " + e.getMessage());
        }
    }
}