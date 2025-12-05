package com.example.msordenes.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarritoItem {

    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private Integer precioUnitario;

    public Integer getSubtotal() {
        if (precioUnitario == null) {
            return 0;
        }
        return precioUnitario * cantidad;
    }
}
