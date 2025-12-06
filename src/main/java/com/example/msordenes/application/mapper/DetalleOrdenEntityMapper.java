package com.example.msordenes.application.mapper;

import com.example.msordenes.application.dto.DetalleOrdenDto;
import com.example.msordenes.application.jpa.entity.DetalleOrdenEntity;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.entity.ProductoEntity;

import java.util.List;

public class DetalleOrdenEntityMapper {


    public static List<DetalleOrdenEntity> mapearAListaEntity(List<DetalleOrdenDto> listaDetalleDto, Long idOrden) {
        return listaDetalleDto.stream().map(detalleOrdenDto ->
                        mapearAEntity(detalleOrdenDto, idOrden))
                .toList();
    }

    public static DetalleOrdenEntity mapearAEntity(DetalleOrdenDto detalleOrdenDto, Long idOrden) {
        return DetalleOrdenEntity.builder()
                .orden(OrdenEntity.builder().id(idOrden).build())
                .producto(ProductoEntity.builder().id(detalleOrdenDto.getIdProducto()).build())
                .cantidad(detalleOrdenDto.getCantidad())
                .build();
    }

}
