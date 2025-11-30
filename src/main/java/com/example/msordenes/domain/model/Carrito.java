package com.example.msordenes.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Carrito {

    private Long idCarrito;
    private Long idCliente;
    private String estado;
    private OffsetDateTime fechaCreacion;
    @Builder.Default
    private List<CarritoItem> items = new ArrayList<>();

    public BigDecimal calcularTotal() {
        return items.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void agregarOActualizarItem(CarritoItem item) {
        items.stream()
                .filter(i -> i.getIdProducto().equals(item.getIdProducto()))
                .findFirst()
                .ifPresentOrElse(
                        existente -> existente.setCantidad(item.getCantidad()),
                        () -> items.add(item)
                );
    }

    public void eliminarItem(Long idProducto) {
        items.removeIf(i -> i.getIdProducto().equals(idProducto));
    }
}
