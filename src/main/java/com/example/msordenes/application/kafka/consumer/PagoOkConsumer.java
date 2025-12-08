package com.example.msordenes.application.kafka.consumer;

import com.example.msordenes.application.dto.PagoOkEvent;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PagoOkConsumer {

    private final OrdenRepository ordenRepository;


    @KafkaListener(
            topics = "${app.kafka.topic.pago-ok:pago-ok}",
            groupId = "ms-pagos-grp",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escucharPagoOk(PagoOkEvent event) {
        log.info("[ms-pagos] Recibido PagoOkEvent: {}", event);

        OrdenEntity orden = ordenRepository.findById(event.getIdOrden()).orElseThrow(
                () -> new OrdenNoEncontradaException("Orden no encontrada")
        );
        orden.setEstadoOrden("PAGO_CORRECTO");
        ordenRepository.save(orden);
    }
}
