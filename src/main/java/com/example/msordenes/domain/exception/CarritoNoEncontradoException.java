package com.example.msordenes.domain.exception;

public class CarritoNoEncontradoException extends RuntimeException {

    public CarritoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

