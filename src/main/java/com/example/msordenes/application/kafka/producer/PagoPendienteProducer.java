package com.example.msordenes.application.kafka.producer;

import com.example.msordenes.application.dto.PagoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PagoPendienteProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private String topic = "pago-pendiente";

    public void enviar(PagoDto event) {
        String key = event.getIdOrden().toString();

        log.info("[ms-ordenes] Enviando evento pago-pendiente para orden {} al topic {}",
                event.getIdOrden(), topic);

        kafkaTemplate.send(topic, key, event);
    }
}