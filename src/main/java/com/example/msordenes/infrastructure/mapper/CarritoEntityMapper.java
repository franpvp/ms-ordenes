package com.example.msordenes.infrastructure.mapper;

import com.example.msordenes.domain.model.Carrito;
import com.example.msordenes.domain.model.CarritoItem;
import com.example.msordenes.infrastructure.persistence.entity.*;
import com.example.msordenes.infrastructure.persistence.jpa.EstadoCarritoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarritoEntityMapper {

    private final EstadoCarritoJpaRepository estadoCarritoJpaRepository;
    private final ProductoEntityMapper productoEntityMapper;

    public Carrito toDomain(CarritoEntity carritoEntity) {
        if (carritoEntity == null) {
            return null;
        }

        return Carrito.builder()
                .idCarrito(carritoEntity.getId())
                .idCliente(carritoEntity.getCliente().getId())
                .estado(carritoEntity.getEstadoCarrito().getNombre())
                .fechaCreacion(carritoEntity.getFechaCreacion())
                .items(obtenerItemsCarrito(carritoEntity))
                .build();
    }


    private List<CarritoItem> obtenerItemsCarrito(CarritoEntity carritoEntity) {
        return carritoEntity.getItems().stream()
                .map(this::toDomainItem)
                .collect(Collectors.toList());
    }


    private CarritoItem toDomainItem(DetalleCarritoEntity detalle) {
        ProductoEntity producto = detalle.getProducto();

        return CarritoItem.builder()
                .idProducto(producto.getId())
                .nombreProducto(producto.getNombre())
                .precioUnitario(producto.getPrecioUnitario())
                .cantidad(detalle.getCantidad())
                .build();
    }


    public CarritoEntity toEntity(Carrito carrito) {
        if (carrito == null) return null;

        CarritoEntity entity = new CarritoEntity();
        entity.setId(carrito.getIdCarrito());

        if (carrito.getIdCliente() != null) {
            ClienteEntity clienteRef = new ClienteEntity();
            clienteRef.setId(carrito.getIdCliente());
            entity.setCliente(clienteRef);
        }

        if (carrito.getEstado() != null) {
            EstadoCarritoEntity estado = estadoCarritoJpaRepository
                    .findByNombre(carrito.getEstado())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Estado inválido: " + carrito.getEstado())
                    );
            entity.setEstadoCarrito(estado);
        }

        entity.setFechaCreacion(carrito.getFechaCreacion());
        entity.setTotal(carrito.calcularTotal());

        List<DetalleCarritoEntity> detalles = carrito.getItems().stream()
                .map(item -> toDetalleEntity(item, entity))
                .collect(Collectors.toList());

        entity.setItems(detalles);

        return entity;
    }

    private DetalleCarritoEntity toDetalleEntity(CarritoItem item, CarritoEntity carritoEntity) {
        DetalleCarritoEntity detalle = new DetalleCarritoEntity();

        detalle.setCarrito(carritoEntity);
        detalle.setCantidad(item.getCantidad());
        detalle.setSubtotal(item.getSubtotal());

        ProductoEntity productoRef =
                productoEntityMapper.toReferenceEntity(item.getIdProducto());

        detalle.setProducto(productoRef);

        return detalle;
    }


}
