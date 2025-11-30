package com.example.msordenes.infrastructure.mapper;

import com.example.msordenes.domain.model.Producto;
import com.example.msordenes.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoEntityMapper {

    public Producto toDomain(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }

        return Producto.builder()
                .idProducto(entity.getId())
                .nombre(entity.getNombre())
                .precioUnitario(entity.getPrecioUnitario())
                .imageUrl(entity.getImagenUrl())
                .build();
    }

    public ProductoEntity toEntity(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoEntity entity = new ProductoEntity();
        entity.setId(producto.getIdProducto());
        entity.setNombre(producto.getNombre());
        entity.setPrecioUnitario(producto.getPrecioUnitario());
        entity.setImagenUrl(producto.getImageUrl());
        return entity;
    }

    public ProductoEntity toReferenceEntity(Long idProducto) {
        if (idProducto == null) {
            return null;
        }
        ProductoEntity entity = new ProductoEntity();
        entity.setId(idProducto);
        return entity;
    }
}

