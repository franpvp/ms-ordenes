package com.example.msordenes.application.mapper;

import com.example.msordenes.application.dto.CarritoItemResponse;
import com.example.msordenes.application.dto.CarritoResponse;
import com.example.msordenes.domain.model.Carrito;
import com.example.msordenes.domain.model.CarritoItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;


@Component
public class CarritoResponseMapper {

    public CarritoResponse toResponse(Carrito carrito) {
        if (carrito == null) return null;

        BigDecimal total = carrito.calcularTotal();

        return CarritoResponse.builder()
                .idCarrito(carrito.getIdCarrito())
                .idCliente(carrito.getIdCliente())
                .estadoCarrito(carrito.getEstado())
                .total(total)
                .items(
                        carrito.getItems().stream()
                                .map(this::toItemResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private CarritoItemResponse toItemResponse(CarritoItem item) {
        return CarritoItemResponse.builder()
                .idProducto(item.getIdProducto())
                .nombreProducto(item.getNombreProducto())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getSubtotal())
                .build();
    }
}
