package com.example.msordenes.application.service;

import com.example.msordenes.application.dto.OrdenDto;
import jakarta.transaction.Transactional;

public interface OrdenService {
    @Transactional
    OrdenDto crearOrden(OrdenDto request);
}
