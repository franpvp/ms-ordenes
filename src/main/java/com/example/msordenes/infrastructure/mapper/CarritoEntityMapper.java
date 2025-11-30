package com.example.msordenes.infrastructure.mapper;


import com.example.msordenes.application.dto.CarritoItemResponse;
import com.example.msordenes.application.dto.CarritoResponse;
import com.example.msordenes.domain.model.Carrito;
import com.example.msordenes.domain.model.CarritoItem;
import com.example.msordenes.infrastructure.persistence.entity.CarritoEntity;
import com.example.msordenes.infrastructure.persistence.entity.ClienteEntity;
import com.example.msordenes.infrastructure.persistence.entity.DetalleCarritoEntity;
import com.example.msordenes.infrastructure.persistence.entity.EstadoCarritoEntity;
import com.example.msordenes.infrastructure.persistence.entity.ProductoEntity;
import com.example.msordenes.infrastructure.persistence.jpa.EstadoCarritoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarritoEntityMapper {

    private final EstadoCarritoJpaRepository estadoCarritoJpaRepository;
    private final ProductoEntityMapper productoEntityMapper;

    public Carrito toDomain(CarritoEntity entity) {
        if (entity == null) {
            return null;
        }

        Long idCliente = null;
        if (entity.getCliente() != null) {
            idCliente = entity.getCliente().getId();
        }

        String estado = null;
        if (entity.getEstadoCarrito() != null) {
            estado = entity.getEstadoCarrito().getEstadoCarrito();
        }

        List<CarritoItem> items = entity.getItems().stream()
                .map(this::toDomainItem)
                .collect(Collectors.toList());

        return Carrito.builder()
                .idCarrito(entity.getId())
                .idCliente(idCliente)
                .estado(estado)
                .fechaCreacion(entity.getFechaCreacion())
                .items(items)
                .build();
    }

    private CarritoItem toDomainItem(DetalleCarritoEntity detalle) {
        ProductoEntity p = detalle.getProducto();

        return CarritoItem.builder()
                .idProducto(p.getId())
                .nombreProducto(p.getNombre())
                .precioUnitario(p.getPrecioUnitario())
                .cantidad(detalle.getCantidad())
                .build();
    }


    public CarritoEntity toEntity(Carrito carrito) {
        if (carrito == null) {
            return null;
        }

        CarritoEntity entity = new CarritoEntity();
        entity.setId(carrito.getIdCarrito());

        if (carrito.getIdCliente() != null) {
            ClienteEntity cliente = new ClienteEntity();
            cliente.setId(carrito.getIdCliente());
            entity.setCliente(cliente);
        } else {
            entity.setCliente(null);
        }

        if (carrito.getEstado() != null) {
            EstadoCarritoEntity estadoCarrito = estadoCarritoJpaRepository
                    .findByEstadoCarrito(carrito.getEstado())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Estado de carrito no válido: " + carrito.getEstado()
                    ));
            entity.setEstadoCarrito(estadoCarrito);
        } else {
            entity.setEstadoCarrito(null);
        }

        entity.setFechaCreacion(carrito.getFechaCreacion());

        BigDecimal total = carrito.calcularTotal();
        entity.setTotal(total);

        List<DetalleCarritoEntity> detalles = carrito.getItems().stream()
                .map(item -> toDetalleEntity(item, entity))
                .collect(Collectors.toList());

        entity.setItems(detalles);

        return entity;
    }

    private DetalleCarritoEntity toDetalleEntity(CarritoItem item, CarritoEntity carritoEntity) {
        DetalleCarritoEntity detalle = new DetalleCarritoEntity();
        detalle.setCarrito(carritoEntity);

        ProductoEntity producto = productoEntityMapper.toReferenceEntity(item.getIdProducto());
        detalle.setProducto(producto);
        detalle.setCantidad(item.getCantidad());
        detalle.setSubtotal(item.getSubtotal());

        return detalle;
    }


    public CarritoResponse toResponse(Carrito carrito) {
        if (carrito == null) {
            return null;
        }

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
