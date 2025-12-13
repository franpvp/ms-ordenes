package com.example.msordenes.application.kafka.consumer;

import com.example.msordenes.application.dto.PagoOkEvent;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.feign.ClienteFeignClient;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import com.example.msordenes.application.mapper.OrdenEntityMapper;
import com.example.msordenes.application.service.impl.EmailService;
import com.example.msordenes.application.service.impl.EmailTemplateService;
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
    private final ClienteFeignClient clienteFeign;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final OrdenEntityMapper ordenEntityMapper;

    @KafkaListener(
            topics = "${app.kafka.topic.pago-ok}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "pagoOkKafkaListenerFactory"
    )
    public void escucharPagoOk(PagoOkEvent event, Acknowledgment ack) {

        log.info("[ms-ordenes] Recibido PagoOkEvent: {}", event);

        OrdenEntity orden = ordenRepository.findByIdWithAll(event.getIdOrden())
                .orElseThrow(() -> new OrdenNoEncontradaException("Orden no encontrada"));

        orden.setEstadoOrden("PAGO_CORRECTO");
        ordenRepository.save(orden);

        try {
            var cliente = clienteFeign.obtenerCliente(orden.getCliente().getId());
            var ordenDto = ordenEntityMapper.toDto(orden);

            String html = emailTemplateService.generarTemplateCompra(
                    ordenDto,
                    cliente.getNombre() + " " + cliente.getApellido()
            );

            emailService.enviarCorreoHtml(
                    cliente.getEmail(),
                    "Confirmación de compra - Orden Nº " + ordenDto.getIdOrden(),
                    html
            );

            log.info("[ms-ordenes] Correo enviado a {}", cliente.getEmail());

        } catch (Exception e) {
            log.error("[ms-ordenes] Error enviando correo: {}", e.getMessage(), e);
        }

        ack.acknowledge();
    }
}