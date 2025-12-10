package com.example.msordenes.application.kafka.consumer;

import com.example.msordenes.application.dto.PagoErrorEvent;
import com.example.msordenes.application.dto.ReintentoResponseDto;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.feign.ReintentoFeignClient;
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
public class PagoErrorConsumer {

    private final OrdenRepository ordenRepository;
    private final ReintentoFeignClient reintentoFeignClient;

    @KafkaListener(
            topics = "${app.kafka.topic.pago-error}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "pagoErrorKafkaListenerFactory"
    )
    public void escucharPagoError(PagoErrorEvent event, Acknowledgment ack) {

        log.warn("[ms-ordenes] Recibido PagoErrorEvent: {}", event);

        if ("ERROR CONEXION".equalsIgnoreCase(event.getMotivoError())) {

            log.info("[ms-ordenes] 🔄 Error de conexión, enviando a ms-reintento: {}", event);

            ReintentoResponseDto response = reintentoFeignClient.reintentar(event);

            log.info("[ms-ordenes] 📩 Respuesta de ms-reintento: {}", response);

            if ("AGOTADO".equalsIgnoreCase(response.getEstado())) {
                cambiarOrdenAError(event);
            }

            ack.acknowledge();
            return;
        }

        cambiarOrdenAError(event);

        ack.acknowledge();
    }

    private void cambiarOrdenAError(PagoErrorEvent event) {

        OrdenEntity orden = ordenRepository.findById(event.getIdOrden())
                .orElseThrow(() -> new OrdenNoEncontradaException("Orden no encontrada"));

        orden.setEstadoOrden("PAGO_ERROR");
        ordenRepository.save(orden);

        log.error("[ms-ordenes] 🔥 Orden {} marcada como PAGO_ERROR", event.getIdOrden());
    }
}