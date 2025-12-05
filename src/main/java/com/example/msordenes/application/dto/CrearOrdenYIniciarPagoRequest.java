package com.example.msordenes.application.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CrearOrdenYIniciarPagoRequest {
    private Long idCarrito;
    private Long idMetodoPago;


    //carrito, despacho, pone pagar -> orden con su  metodo de pago
}
