package com.example.msordenes.application.exception;

public class OrdenNoEncontradaException extends RuntimeException {

    public OrdenNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
