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
    private final ReintentoFeignClient reintentoFeignClient; // <--- Feign de reintentos

    @KafkaListener(
            topics = "${app.kafka.topic.pago-error:pago-error}",
            groupId = "ms-pagos-grp",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escucharPagoError(PagoErrorEvent event) {

        log.info("[ms-pagos] Recibido PagoErrorEvent: {}", event);

        if ("ERROR CONEXION".equalsIgnoreCase(event.getMotivoError())) {

            log.info("[ms-pagos] Error recuperable, enviando a ms-reintento: {}", event);
            ReintentoResponseDto response = reintentoFeignClient.reintentar(event);

            log.info("[ms-pagos] Respuesta de ms-reintento: {}", response);
            if ("AGOTADO".equals(response.getEstado())) {

                log.warn("[ms-pagos] Pago {} sin posibilidad de reintento. Marcando orden como PAGO_ERROR",
                        event.getIdPago());

                cambiarOrdenAError(event);
            }
            return;
        }

        cambiarOrdenAError(event);

        log.info("[ms-pagos] Pago error no recuperable, marcando orden {} como PAGO_ERROR", event.getIdOrden());
    }

    private void cambiarOrdenAError(PagoErrorEvent event) {
        OrdenEntity orden = getOrden(event);
        orden.setEstadoOrden("PAGO_ERROR");
        ordenRepository.save(orden);
    }

    private OrdenEntity getOrden(PagoErrorEvent event) {
        return ordenRepository.findById(event.getIdOrden())
                .orElseThrow(() -> new OrdenNoEncontradaException("Orden no encontrada"));
    }
}
