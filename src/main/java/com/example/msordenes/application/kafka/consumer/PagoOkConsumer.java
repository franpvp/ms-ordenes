package com.example.msordenes.application.kafka.consumer;

import com.example.msordenes.application.dto.PagoOkEvent;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PagoOkConsumer {

    private final OrdenRepository ordenRepository;

    @KafkaListener(
            topics = "${app.kafka.topic.pago-ok}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "pagoOkKafkaListenerFactory"
    )
    public void escucharPagoOk(PagoOkEvent event, Acknowledgment ack) {

        log.info("[ms-ordenes] Recibido PagoOkEvent: {}", event);

        OrdenEntity orden = ordenRepository.findById(event.getIdOrden())
                .orElseThrow(() -> new OrdenNoEncontradaException("Orden no encontrada"));

        orden.setEstadoOrden("PAGO_CORRECTO");
        ordenRepository.save(orden);

        log.info("[ms-ordenes] Orden {} marcada como PAGO_CORRECTO", event.getIdOrden());

        ack.acknowledge();
    }
}