package com.example.msordenes.domain.repository;

import com.example.msordenes.domain.model.Producto;

import java.util.Optional;

public interface ProductoRepository {

    Optional<Producto> findById(Long idProducto);
}

