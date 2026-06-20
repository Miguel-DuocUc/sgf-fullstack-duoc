package com.duoc.sgf.ms_alerts.listener;

import com.duoc.sgf.ms_alerts.model.Alert;
import com.duoc.sgf.ms_alerts.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertListener {

    private final AlertService alertService;

    @KafkaListener(topics = "border-events", groupId = "alerts-group")
    public void escucharCruce(String mensajePasaporte) {

        List<Alert> alertasActivas = alertService.verificarPasaporte(mensajePasaporte);
        if (!alertasActivas.isEmpty()) {
            Alert alertaPrincipal = alertasActivas.get(0);
            alertService.registrarIntercepcion(alertaPrincipal, mensajePasaporte);

        }

    }
}