package com.example.msordenes.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDto {

    private Long idProducto;
    private Long idCategoria;

    private String nombre;
    private String descripcion;
    private String marca;
    private Integer precio;
    private String imagenUrl;

    public Integer getPrecioUnitario() {
        if (precio == null) {
            return 0;
        }
        return precio;
    }
}

