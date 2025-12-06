package com.example.msordenes.application.controller;


import com.example.msordenes.application.dto.DespachoDto;
import com.example.msordenes.application.service.impl.DespachoServiceImpl;
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

    private final DespachoServiceImpl despachoServiceImpl;

    @PostMapping
    public ResponseEntity crear(@RequestBody DespachoDto request) {
        despachoServiceImpl.guardarDespacho(request);
        return ResponseEntity.status(201).build();
    }

}
