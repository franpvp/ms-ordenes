package com.example.msordenes.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DetalleOrdenDto {
    private Long idDetalleOrden;
    private Long idProducto;
    private int cantidad;
    private ProductoDto producto;

    public Integer getSubtotal() {
        int precioUnitario = 0;

        if (producto != null) {
            precioUnitario = producto.getPrecioUnitario();
        }

        return precioUnitario * cantidad;
    }

}
