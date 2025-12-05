package com.example.msordenes.infrastructure.mapper;


import com.example.msordenes.application.dto.CrearDespachoRequest;
import com.example.msordenes.domain.model.Despacho;
import com.example.msordenes.infrastructure.persistence.entity.DespachoEntity;

public class DespachoMapper {

    public static Despacho toDomain(DespachoEntity e) {
        if (e == null) return null;

        return Despacho.builder()
                .id(e.getId())
                .nombreDestinatario(e.getNombreDestinatario())
                .apellidoDestinatario(e.getApellidoDestinatario())
                .telefono(e.getTelefono())
                .direccion(e.getDireccion())
                .region(e.getRegion())
                .ciudadComuna(e.getCiudadComuna())
                .codigoPostal(e.getCodigoPostal())
                .build();
    }

    public static DespachoEntity toEntity(Despacho d) {
        if (d == null) return null;

        DespachoEntity entity = new DespachoEntity();
        entity.setId(d.getId());
        entity.setNombreDestinatario(d.getNombreDestinatario());
        entity.setApellidoDestinatario(d.getApellidoDestinatario());
        entity.setTelefono(d.getTelefono());
        entity.setDireccion(d.getDireccion());
        entity.setRegion(d.getRegion());
        entity.setCiudadComuna(d.getCiudadComuna());
        entity.setCodigoPostal(d.getCodigoPostal());
        return entity;
    }

    public static Despacho toDtoRequest(CrearDespachoRequest req) {
        return Despacho.builder()
                .id(null)
                .nombreDestinatario(req.getNombreDestinatario())
                .apellidoDestinatario(req.getApellidoDestinatario())
                .telefono(req.getTelefono())
                .direccion(req.getDireccion())
                .region(req.getRegion())
                .ciudadComuna(req.getCiudadComuna())
                .codigoPostal(req.getCodigoPostal())
                .build();
    }
}

