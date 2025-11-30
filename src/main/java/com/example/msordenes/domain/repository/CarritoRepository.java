package com.example.msordenes.domain.repository;

import com.example.msordenes.domain.model.Carrito;

import java.util.Optional;

public interface CarritoRepository {

    Optional<Carrito> buscarCarritoActivoPorCliente(Long idCliente);

    Optional<Carrito> buscarCarritoPorId(Long idCarrito);

    Carrito guardarCarrito(Carrito carrito);
}

