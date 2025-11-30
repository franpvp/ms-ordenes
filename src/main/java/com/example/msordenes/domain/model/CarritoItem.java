package com.example.msordenes.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CarritoItem {

    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private BigDecimal precioUnitario;

    public BigDecimal getSubtotal() {
        if (precioUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
