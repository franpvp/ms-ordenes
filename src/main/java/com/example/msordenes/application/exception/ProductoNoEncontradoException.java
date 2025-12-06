package com.example.msordenes.application.exception;

public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long idProducto) {
        super("Producto no encontrado: " + idProducto);
    }
}