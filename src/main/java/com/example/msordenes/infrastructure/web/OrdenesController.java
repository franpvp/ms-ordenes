package com.example.msordenes.infrastructure.web;

import com.example.msordenes.application.dto.CrearOrdenYIniciarPagoRequest;
import com.example.msordenes.application.dto.OrdenResponse;

import com.example.msordenes.application.usecase.CrearOrdenYIniciarPagoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenesController {

    private final CrearOrdenYIniciarPagoUseCase crearOrdenYIniciarPagoUseCase;

    /**
     * Crea una orden en estado PAGO_PENDIENTE
     * y dispara el evento pago-pendiente a Kafka.
     */
    @PostMapping
    public ResponseEntity<OrdenResponse> crearOrdenYIniciarPago(
            @RequestBody CrearOrdenYIniciarPagoRequest request
    ) {
        OrdenResponse response = crearOrdenYIniciarPagoUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
