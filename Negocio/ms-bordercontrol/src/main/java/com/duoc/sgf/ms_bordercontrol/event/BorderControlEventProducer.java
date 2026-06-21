package com.duoc.sgf.ms_bordercontrol.event;

import com.duoc.sgf.ms_bordercontrol.model.BorderControl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BorderControlEventProducer {

    private static final String TOPIC_AUDIT = "border-control-events";
    private static final String TOPIC_ALERTS = "border-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishBorderControlEvent(BorderControl borderControl) {
        try {
            String auditMessage = "Control fronterizo registrado: id="
                    + borderControl.getId()
                    + ", userId=" + borderControl.getUserId()
                    + ", status=" + borderControl.getStatus()
                    + ", checkpointId=" + borderControl.getLogisticsCheckpointId();
            kafkaTemplate.send(TOPIC_AUDIT, auditMessage);
            log.info("Evento enviado a Audit: {}", auditMessage);


            String alertMessage = objectMapper.writeValueAsString(borderControl);

            kafkaTemplate.send(TOPIC_ALERTS, alertMessage);
            log.info("Evento enviado a Alerts en formato JSON");

        } catch (Exception ex) {
            log.error("No se pudo enviar evento Kafka del control fronterizo id: {}", borderControl.getId(), ex);
        }
    }
}