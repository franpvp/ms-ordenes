package com.example.msordenes.infrastructure.mapper;

import com.example.msordenes.domain.model.Orden;
import com.example.msordenes.infrastructure.persistence.entity.CarritoEntity;
import com.example.msordenes.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.stereotype.Component;

@Component
public class OrdenEntityMapper {

    public OrdenEntity toEntity(Orden orden) {
        if (orden == null) {
            return null;
        }

        OrdenEntity entity = new OrdenEntity();
        entity.setCarrito(CarritoEntity.builder().id(orden.getIdCarrito()).build());
        entity.setFechaOrden(orden.getFechaOrden());
        entity.setEstadoOrden(orden.getEstadoOrden());
        return entity;
    }

    public Orden toDomain(OrdenEntity entity) {
        if (entity == null) {
            return null;
        }

        return Orden.builder()
                .idOrden(entity.getId())
                .fechaOrden(entity.getFechaOrden())
                .estadoOrden(entity.getEstadoOrden())
                .idPago(entity.getIdPago())
                .idCarrito(entity.getIdCarrito())
                .build();
    }
}
