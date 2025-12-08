package com.example.msordenes.application.service;

import com.example.msordenes.application.dto.OrdenDto;
import com.example.msordenes.application.dto.OrdenEstadoDto;
import jakarta.transaction.Transactional;

public interface OrdenService {
    @Transactional
    OrdenDto crearOrden(OrdenDto request);

    OrdenEstadoDto buscarOrdenPorCliente(Long idCliente);
}
