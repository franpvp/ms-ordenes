package com.example.msordenes.application.controller;

import com.example.msordenes.application.dto.OrdenDto;
import com.example.msordenes.application.dto.OrdenEstadoDto;
import com.example.msordenes.application.service.impl.OrdenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
@CrossOrigin
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


    /**
     * Obtiene la última orden del cliente por idCliente
     */
    @GetMapping("/cliente/{idCliente}/ultima")
    public ResponseEntity<OrdenEstadoDto> obtenerUltimaOrdenPorCliente(
            @PathVariable Long idCliente
    ) {
        OrdenEstadoDto dto = ordenServiceImpl.buscarOrdenPorCliente(idCliente);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{idOrden}")
    public ResponseEntity<OrdenDto> buscarOrdenPorId(@PathVariable Long idOrden) {
        OrdenDto dto = ordenServiceImpl.buscarOrdenById(idOrden);
        return ResponseEntity.ok(dto);
    }


}
