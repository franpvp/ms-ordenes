package com.example.msordenes.application.mapper;

import com.example.msordenes.application.dto.DetalleOrdenDto;
import com.example.msordenes.application.dto.ProductoDto;
import com.example.msordenes.application.jpa.entity.DetalleOrdenEntity;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.entity.ProductoEntity;

import java.util.List;

public class DetalleOrdenEntityMapper {

    public static List<DetalleOrdenEntity> mapearAListaEntity(List<DetalleOrdenDto> listaDetalleDto, Long idOrden) {
        return listaDetalleDto.stream()
                .map(detalleOrdenDto -> mapearAEntity(detalleOrdenDto, idOrden))
                .toList();
    }

    public static DetalleOrdenEntity mapearAEntity(DetalleOrdenDto detalleOrdenDto, Long idOrden) {
        return DetalleOrdenEntity.builder()
                .orden(OrdenEntity.builder().id(idOrden).build())
                .producto(ProductoEntity.builder().id(detalleOrdenDto.getIdProducto()).build())
                .cantidad(detalleOrdenDto.getCantidad())
                .build();
    }


    public static List<DetalleOrdenDto> mapearADtoLista(List<DetalleOrdenEntity> entidades) {
        return entidades.stream()
                .map(DetalleOrdenEntityMapper::mapearADto)
                .toList();
    }

    public static DetalleOrdenDto mapearADto(DetalleOrdenEntity entity) {
        if (entity == null) return null;

        return DetalleOrdenDto.builder()
                .idDetalleOrden(entity.getId())
                .idProducto(entity.getProducto() != null ? entity.getProducto().getId() : null)
                .cantidad(entity.getCantidad())
                .producto(mapearProductoDto(entity.getProducto()))
                .build();
    }

    private static ProductoDto mapearProductoDto(ProductoEntity producto) {
        if (producto == null) return null;

        return ProductoDto.builder()
                .idProducto(producto.getId())
                .idCategoria(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .marca(producto.getMarca())
                .precio(producto.getPrecio())
                .imagenUrl(producto.getImagenUrl())
                .build();
    }
}