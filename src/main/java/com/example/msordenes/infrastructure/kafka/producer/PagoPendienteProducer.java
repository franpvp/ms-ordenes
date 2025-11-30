package com.example.msordenes.infrastructure.kafka.producer;

import com.example.msordenes.application.dto.PagoPendienteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PagoPendienteProducer {

    private static final String TOPIC = "pago-pendiente";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void enviar(PagoPendienteEvent event) {
        String key = event.getIdOrden().toString();

        log.info("[ms-ordenes] Enviando evento pago-pendiente para orden {} al topic {}",
                event.getIdOrden(), TOPIC);

        kafkaTemplate.send(TOPIC, key, event);
    }
}