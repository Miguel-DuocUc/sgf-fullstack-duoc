package com.duoc.sgf.ms_audit.service;

import com.duoc.sgf.ms_audit.model.Auditoria;
import com.duoc.sgf.ms_audit.repository.AuditoriaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class KafkaAuditConsumer {

    private final AuditoriaRepository auditoriaRepository;

    public KafkaAuditConsumer(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @KafkaListener(topics = "border-control-events", groupId = "grupo-auditoria")
    public void escucharTrazabilidad(String mensajeCompleto) {
        System.out.println(">> [Kafka ms-audit] Mensaje crudo recibido: " + mensajeCompleto);

        try {
            String mensajeInformativo = "Evento capturado";
            String emitidoPor = "SISTEMA_GLOBAL";

            if (mensajeCompleto != null && mensajeCompleto.contains(";")) {
                String[] partes = mensajeCompleto.split(";");
                mensajeInformativo = partes[0];
                emitidoPor = partes[1];
            } else {
                mensajeInformativo = mensajeCompleto;
            }

            Auditoria auditoria = new Auditoria();
            auditoria.setMensaje(mensajeInformativo);
            auditoria.setEmitidoPor(emitidoPor);
            auditoria.setFechaRegistro(LocalDateTime.now());

            auditoriaRepository.save(auditoria);
            System.out.println(">> [Audit] Entrada de auditoría guardada con éxito en Oracle.");

        } catch (Exception e) {
            System.err.println(">> [Audit] Error al procesar el log de auditoría: " + e.getMessage());
        }
    }
}