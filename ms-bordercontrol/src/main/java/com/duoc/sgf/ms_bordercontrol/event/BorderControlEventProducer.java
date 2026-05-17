package com.duoc.sgf.ms_bordercontrol.event;

import com.duoc.sgf.ms_bordercontrol.model.BorderControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BorderControlEventProducer {

    private static final String TOPIC = "border-control-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishBorderControlEvent(BorderControl borderControl) {
        try {
            String message = "Control fronterizo registrado: id="
                    + borderControl.getId()
                    + ", userId=" + borderControl.getUserId()
                    + ", status=" + borderControl.getStatus()
                    + ", checkpoint=" + borderControl.getCheckpoint();

            kafkaTemplate.send(TOPIC, message);

            log.info("Evento Kafka enviado correctamente: {}", message);

        } catch (Exception ex) {
            log.warn("No se pudo enviar evento Kafka del control fronterizo id: {}", borderControl.getId());
        }
    }
}