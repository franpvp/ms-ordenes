package com.example.msordenes.infrastructure.controller;


import com.example.msordenes.application.dto.CrearDespachoRequest;
import com.example.msordenes.application.usecase.CrearDespachoUseCase;
import com.example.msordenes.domain.model.Despacho;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final CrearDespachoUseCase crearDespachoUseCase;

    @PostMapping
    public ResponseEntity<Despacho> crear(@RequestBody CrearDespachoRequest request) {
        Despacho despacho = crearDespachoUseCase.ejecutar(request);
        return ResponseEntity.ok(despacho);
    }

}
