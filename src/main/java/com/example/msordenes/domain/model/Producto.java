package com.example.msordenes.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Producto {

    private Long idProducto;
    private String nombre;
    private BigDecimal precioUnitario;
    private String imageUrl;
}
