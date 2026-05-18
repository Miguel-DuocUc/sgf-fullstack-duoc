package com.duoc.sgf.ms_alerts.service.impl;

import com.duoc.sgf.ms_alerts.model.Alert;
import com.duoc.sgf.ms_alerts.repository.AlertRepository;
import com.duoc.sgf.ms_alerts.service.AlertService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AlertServiceImpl(AlertRepository alertRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.alertRepository = alertRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Alert crearAlerta(Alert alerta) {
        alerta.setFechaCreacion(LocalDateTime.now());
        alerta.setActiva(true);

        Alert alertaGuardada = alertRepository.save(alerta);

        try {
            String mensajeKafka = "NUEVA_ALERTA_EMITIDA: Pasaporte " + alertaGuardada.getPasaporteCiudadano()
                    + " - Tipo: " + alertaGuardada.getTipoAlerta();

            kafkaTemplate.send("sgf-trazabilidad", mensajeKafka + ";" + alertaGuardada.getEmitidoPor());
            System.out.println(">> [Kafka] Evento emitido con éxito desde el Impl.");
        } catch (Exception e) {
            System.err.println(">> [Alerta] Kafka está apagado. El evento se enviará cuando el cluster esté activo. Error: " + e.getMessage());
        }

        return alertaGuardada;
    }

    @Override
    public List<Alert> obtenerAlertasActivas() {
        return alertRepository.findByActivaTrue();
    }

    @Override
    public List<Alert> verificarPasaporte(String pasaporte) {
        return alertRepository.findByPasaporteCiudadanoAndActivaTrue(pasaporte);
    }
}
