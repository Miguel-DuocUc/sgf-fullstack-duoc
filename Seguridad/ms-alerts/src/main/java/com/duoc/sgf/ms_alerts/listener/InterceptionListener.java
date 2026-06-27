package com.duoc.sgf.ms_alerts.listener;

import com.duoc.sgf.ms_alerts.model.dto.IntercepcionEventDto;
import com.duoc.sgf.ms_alerts.service.IntercepcionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InterceptionListener {

    private final IntercepcionService intercepcionService;

    @KafkaListener(topics = "intercepciones-topic", groupId = "alerts-group")
    public void escucharIntercepcion(IntercepcionEventDto evento) {

        log.info("--------------------------------------------------");
        log.info("🛰️ [MOVIMIENTO] Intento de cruce detectado");
        log.info("🆔 Pasaporte: {}", evento.getPasaporteCiudadano());
        log.info("📍 Ubicación: {}", evento.getCheckpointId());
        log.warn("🚨 [INTERCEPCIÓN] ¡ALERTA ACTIVADA PARA: {}!", evento.getPasaporteCiudadano());
        log.info("📝 Motivo: {}", evento.getMotivoBloqueo());
        log.info("--------------------------------------------------");

        intercepcionService.registrarIntercepcion(evento);
    }
}