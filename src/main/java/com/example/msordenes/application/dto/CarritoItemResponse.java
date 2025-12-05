package com.example.msordenes.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CarritoItemResponse {

    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private Integer precioUnitario;
    private Integer subtotal;
}
