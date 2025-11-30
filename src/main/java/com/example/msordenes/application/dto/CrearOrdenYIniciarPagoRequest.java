package com.example.msordenes.application.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrearOrdenYIniciarPagoRequest {
    private Long idCarrito;
    private Long idMetodoPago;
}
