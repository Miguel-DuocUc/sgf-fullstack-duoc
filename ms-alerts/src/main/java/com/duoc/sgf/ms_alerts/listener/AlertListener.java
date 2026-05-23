package com.duoc.sgf.ms_alerts.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AlertListener {
    @KafkaListener(topics = "border-events", groupId = "alerts-group")
    public void escucharCruce(String mensaje) {
        System.out.println("\n=====================================================");
        System.out.println("[MS-ALERTS] 🚨 ALERTA RECIBIDA VÍA KAFKA");
        System.out.println("Procesando notificación urgente...");
        System.out.println("Mensaje original: " + mensaje);
        System.out.println("=====================================================\n");

        }
}