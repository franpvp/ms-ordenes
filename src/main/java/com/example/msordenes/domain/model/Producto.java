package com.example.msordenes.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Producto {

    private Long idProducto;
    private String nombre;
    private Integer precioUnitario;
    private String imageUrl;
}
