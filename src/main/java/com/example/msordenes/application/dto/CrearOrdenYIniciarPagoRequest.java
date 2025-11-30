package com.example.msordenes.application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CrearOrdenYIniciarPagoRequest {
    private Long idCarrito;
    private Long idMetodoPago;
}
