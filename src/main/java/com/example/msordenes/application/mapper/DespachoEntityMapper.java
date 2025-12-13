package com.example.msordenes.application.mapper;

import com.example.msordenes.application.dto.DespachoDto;
import com.example.msordenes.application.jpa.entity.DespachoEntity;

public class DespachoEntityMapper {

    public static DespachoEntity mapearAEntity(DespachoDto despachoDto) {

        return DespachoEntity.builder()
                .nombreDestinatario(despachoDto.getNombreDestinatario())
                .apellidoDestinatario(despachoDto.getApellidoDestinatario())
                .telefono(despachoDto.getTelefono())
                .direccion(despachoDto.getDireccion())
                .region(despachoDto.getRegion())
                .ciudadComuna(despachoDto.getCiudadComuna())
                .codigoPostal(despachoDto.getCodigoPostal())
                .build();
    }

    public static DespachoDto mapearADto(DespachoEntity entity) {
        if (entity == null) return null;

        return DespachoDto.builder()
                .idDespacho(entity.getId())
                .nombreDestinatario(entity.getNombreDestinatario())
                .apellidoDestinatario(entity.getApellidoDestinatario())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .region(entity.getRegion())
                .ciudadComuna(entity.getCiudadComuna())
                .codigoPostal(entity.getCodigoPostal())
                .build();
    }
}


