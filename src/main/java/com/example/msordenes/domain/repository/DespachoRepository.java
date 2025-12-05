package com.example.msordenes.domain.repository;

import com.example.msordenes.domain.model.Despacho;

import java.util.Optional;

public interface DespachoRepository {

    Optional<Despacho> obtenerPorId(Long id);

    Despacho guardar(Despacho despacho);
}
