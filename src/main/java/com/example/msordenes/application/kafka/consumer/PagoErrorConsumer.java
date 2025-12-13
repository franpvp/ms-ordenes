package com.example.msordenes.application.kafka.consumer;

import com.example.msordenes.application.dto.PagoErrorEvent;
import com.example.msordenes.application.dto.ReintentoResponseDto;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.feign.ClienteFeignClient;
import com.example.msordenes.application.feign.ReintentoFeignClient;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import com.example.msordenes.application.service.impl.EmailService;
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
    private final EmailService emailService;
    private final ClienteFeignClient clienteFeign;

    @KafkaListener(
            topics = "${app.kafka.topic.pago-error}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "pagoErrorKafkaListenerFactory"
    )
    public void escucharPagoError(PagoErrorEvent event, Acknowledgment ack) {

        log.warn("[ms-ordenes] Recibido PagoErrorEvent: {}", event);

        if ("ERROR CONEXION".equalsIgnoreCase(event.getMotivoError())) {

            log.info("[ms-ordenes] Error de conexión, enviando a ms-reintento: {}", event);

            ReintentoResponseDto response = reintentoFeignClient.reintentar(event);

            log.info("[ms-ordenes] Respuesta de ms-reintento: {}", response);

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

        // ==========================================
        // Cargar orden completa con detalles (FETCH JOIN)
        // ==========================================
        OrdenEntity orden = ordenRepository.findByIdWithAll(event.getIdOrden())
                .orElseThrow(() -> new OrdenNoEncontradaException("Orden no encontrada"));

        orden.setEstadoOrden("PAGO_ERROR");
        ordenRepository.save(orden);

        log.error("[ms-ordenes] Orden {} marcada como PAGO_ERROR", event.getIdOrden());

        try {
            var cliente = clienteFeign.obtenerCliente(orden.getCliente().getId());

            emailService.enviarCorreoHtml(
                    cliente.getEmail(),
                    "Error en el pago de tu orden #" + orden.getId(),
                    "<p>Hubo un error al procesar tu pago. Motivo: <strong>"
                            + event.getMotivoError() +
                            "</strong></p>"
            );

        } catch (Exception e) {
            log.error("No se pudo enviar el correo de error: {}", e.getMessage());
        }
    }
}