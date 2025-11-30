package com.example.msordenes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

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
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
