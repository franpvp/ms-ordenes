package com.example.msordenes.domain.repository;

import com.example.msordenes.domain.model.Orden;

import java.util.Optional;

public interface OrdenRepository {
    Orden guardar(Orden orden);
    Optional<Orden> buscarPorId(Long idOrden);
}

