package com.example.msordenes.application.controller;

import com.example.msordenes.application.dto.OrdenDto;
import com.example.msordenes.application.service.impl.OrdenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenesController {

    private final OrdenServiceImpl ordenServiceImpl;

    /**
     * Crea una orden en estado PAGO_PENDIENTE
     * y dispara el evento pago-pendiente a Kafka.
     */
    @PostMapping
    public ResponseEntity<OrdenDto> crearOrdenYIniciarPago(
            @RequestBody OrdenDto request
    ) {
        OrdenDto response = ordenServiceImpl.crearOrden(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
