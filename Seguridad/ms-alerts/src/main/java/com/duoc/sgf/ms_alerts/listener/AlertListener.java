package com.duoc.sgf.ms_alerts.listener;

import com.duoc.sgf.ms_alerts.model.dto.AlertResponseDto;
import com.duoc.sgf.ms_alerts.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertListener {

    private final AlertService alertService;
    @KafkaListener(topics = "border-events", groupId = "alerts-group")
    public void escucharCruce(String mensajePasaporte) {

        log.info(">> [KAFKA LISTENER] Analizando cruce fronterizo para pasaporte: {}", mensajePasaporte);
        List<AlertResponseDto> alertasActivas = alertService.verificarPasaporte(mensajePasaporte);

        if (!alertasActivas.isEmpty()) {

            AlertResponseDto alertaPrincipal = alertasActivas.get(0);
            alertService.registrarIntercepcion(alertaPrincipal.getId(), mensajePasaporte);

            log.warn(">> [KAFKA LISTENER] ¡ALERTA DETECTADA! Intercepción registrada para alerta ID: {}", alertaPrincipal.getId());

        } else {
            log.info(">> [KAFKA LISTENER] Cruce autorizado. Sin alertas para pasaporte: {}", mensajePasaporte);
        }
    }
}