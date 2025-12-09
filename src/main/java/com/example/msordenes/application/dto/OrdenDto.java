package com.example.msordenes.application.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrdenDto {
    private Long idOrden;
    private Long idCliente;
    private List<DetalleOrdenDto> listaDetalle;
    private DespachoDto despachoDto;
    private PagoPendienteEvent pagoPendienteEvent;


    public Integer getTotalItems() {
        if (listaDetalle == null) {
            return 0;
        }
        return listaDetalle.size();
    }

    public Integer getTotalCantidad() {
        if (listaDetalle == null) {
            return 0;
        }
        return listaDetalle.stream()
                .map(DetalleOrdenDto::getCantidad)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer getTotalPrecio() {
        if (listaDetalle == null) {
            return 0;
        }
        return listaDetalle.stream()
                .map(DetalleOrdenDto::getSubtotal)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
