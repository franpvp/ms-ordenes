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
}


