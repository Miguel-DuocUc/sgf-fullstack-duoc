package com.duoc.sgf.ms_audit.listener;

import com.duoc.sgf.ms_audit.model.dto.AuditoriaRequestDto;
import com.duoc.sgf.ms_audit.model.mapper.AuditMapper;
import com.duoc.sgf.ms_audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaAuditConsumer {
    private final AuditService auditService;
    private final AuditMapper auditMapper;

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
            AuditoriaRequestDto requestDto = AuditoriaRequestDto.builder()
                    .mensaje(mensajeInformativo)
                    .emitidoPor(emitidoPor)
                    .build();
            var entidad = auditMapper.toEntity(requestDto);

            auditService.registrarAuditoria(entidad);
            System.out.println(">> [Audit] Entrada de auditoría guardada con éxito usando Arquitectura CRS.");

        } catch (Exception e) {
            System.err.println(">> [Audit] Error al procesar el log de auditoría: " + e.getMessage());
        }
    }
}