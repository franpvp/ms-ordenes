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
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escucharPagoError(PagoErrorEvent event) {
        log.info("[ms-ordenes] Recibido PagoErrorEvent: {}", event);

        if ("ERROR CONEXION".equalsIgnoreCase(event.getMotivoError())) {

            log.info("[ms-ordenes] Error recuperable, enviando a ms-reintento: {}", event);
            ReintentoResponseDto response = reintentoFeignClient.reintentar(event);

            log.info("[ms-ordenes] Respuesta de ms-reintento: {}", response);

            if ("AGOTADO".equals(response.getEstado())) {
                cambiarOrdenAError(event);
            }
            return;
        }

        cambiarOrdenAError(event);
    }

    private void cambiarOrdenAError(PagoErrorEvent event) {
        OrdenEntity orden = ordenRepository.findById(event.getIdOrden())
                .orElseThrow(() -> new OrdenNoEncontradaException("Orden no encontrada"));

        orden.setEstadoOrden("PAGO_ERROR");
        ordenRepository.save(orden);
    }
}

