package com.example.msordenes.application.usecase;

import com.example.msordenes.application.dto.CrearOrdenYIniciarPagoRequest;
import com.example.msordenes.application.dto.OrdenResponse;
import com.example.msordenes.application.dto.PagoPendienteEvent;
import com.example.msordenes.application.mapper.OrdenDtoMapper;
import com.example.msordenes.domain.exception.CarritoNoEncontradoException;
import com.example.msordenes.domain.model.Carrito;
import com.example.msordenes.domain.model.Orden;
import com.example.msordenes.domain.repository.CarritoRepository;
import com.example.msordenes.domain.repository.OrdenRepository;
import com.example.msordenes.infrastructure.kafka.producer.PagoPendienteProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrearOrdenYIniciarPagoUseCase {


    private static final String ESTADO_INICIAL = "PAGO_PENDIENTE";

    private final OrdenRepository ordenRepository;
    private final CarritoRepository carritoRepository;
    private final OrdenDtoMapper ordenDtoMapper;
    private final PagoPendienteProducer pagoPendienteProducer;

    public OrdenResponse ejecutar(CrearOrdenYIniciarPagoRequest request) {

        Carrito carrito = carritoRepository.buscarCarritoPorId(request.getIdCarrito())
                .orElseThrow(() -> new CarritoNoEncontradoException("" + request.getIdCarrito()));

        Orden orden = Orden.builder()
                .idOrden(null)
                .idCarrito(carrito.getIdCarrito())
                .estadoOrden(ESTADO_INICIAL)
                .fechaOrden(LocalDateTime.now())
                .build();

        Orden ordenGuardada = ordenRepository.guardar(orden);

        log.info("[ms-ordenes] Orden {} creada en estado {}",
                ordenGuardada.getIdOrden(), ordenGuardada.getEstadoOrden());

        PagoPendienteEvent event = PagoPendienteEvent.builder()
                .idOrden(ordenGuardada.getIdOrden())
                .idMetodoPago(request.getIdMetodoPago())
                .fechaSolicitud(LocalDateTime.now())
                .build();

        pagoPendienteProducer.enviar(event);

        return ordenDtoMapper.toResponse(ordenGuardada);
    }
}
