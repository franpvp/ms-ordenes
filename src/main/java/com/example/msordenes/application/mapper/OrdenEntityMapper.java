package com.example.msordenes.application.mapper;

import com.example.msordenes.application.dto.DespachoDto;
import com.example.msordenes.application.dto.DetalleOrdenDto;
import com.example.msordenes.application.dto.OrdenDto;
import com.example.msordenes.application.dto.PagoDto;
import com.example.msordenes.application.dto.ProductoDto;
import com.example.msordenes.application.jpa.entity.ClienteEntity;
import com.example.msordenes.application.jpa.entity.DespachoEntity;
import com.example.msordenes.application.jpa.entity.DetalleOrdenEntity;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.entity.PagoEntity;
import com.example.msordenes.application.jpa.entity.ProductoEntity;
import com.example.msordenes.application.util.Constantes;
import com.example.msordenes.application.util.CurrencyUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class OrdenEntityMapper {

    public static OrdenEntity toEntity(OrdenDto orden, DespachoEntity despachoGuardado) {

        return OrdenEntity.builder()
                .cliente(ClienteEntity.builder().id(orden.getIdCliente()).build())
                .estadoOrden(Constantes.PAGO_PENDIENTE)
                .fechaOrden(LocalDateTime.now())
                .despacho(despachoGuardado)
                .build();
    }

    public OrdenDto toDto(OrdenEntity entity) {
        OrdenDto dto = new OrdenDto();
        dto.setIdOrden(entity.getId());

        if (entity.getCliente() != null) {
            dto.setIdCliente(entity.getCliente().getId());
        }

        if (entity.getDetalles() != null) {
            List<DetalleOrdenDto> detallesDto = new ArrayList<>();

            for (DetalleOrdenEntity det : entity.getDetalles()) {
                DetalleOrdenDto detDto = new DetalleOrdenDto();
                detDto.setIdDetalleOrden(det.getId());
                detDto.setCantidad(det.getCantidad());

                if (det.getProducto() != null) {
                    ProductoEntity p = det.getProducto();

                    ProductoDto prod = new ProductoDto();
                    prod.setIdProducto(p.getId());
                    prod.setNombre(p.getNombre());
                    prod.setPrecio(p.getPrecio());
                    prod.setImagenUrl(p.getImagenUrl());

                    detDto.setProducto(prod);
                }

                detallesDto.add(detDto);
            }

            dto.setListaDetalle(detallesDto);
        }

        if (entity.getDespacho() != null) {
            DespachoEntity d = entity.getDespacho();
            DespachoDto despachoDto = new DespachoDto();
            despachoDto.setIdDespacho(d.getId());
            despachoDto.setNombreDestinatario(d.getNombreDestinatario());
            despachoDto.setApellidoDestinatario(d.getApellidoDestinatario());
            despachoDto.setTelefono(d.getTelefono());
            despachoDto.setDireccion(d.getDireccion());
            despachoDto.setRegion(d.getRegion());
            despachoDto.setCiudadComuna(d.getCiudadComuna());
            despachoDto.setCodigoPostal(d.getCodigoPostal());
            dto.setDespachoDto(despachoDto);
        }

        if (entity.getPago() != null) {
            PagoEntity p = entity.getPago();
            PagoDto pagoDto = new PagoDto();
            pagoDto.setMonto(p.getMonto());
            dto.setPagoDto(pagoDto);
        }

        return dto;
    }

    // NUEVO — método utilitario del mapper
    public String getTotalFormateado(OrdenEntity entity) {
        OrdenDto dto = toDto(entity);
        return CurrencyUtils.formatCLP(dto.getTotalPrecio());
    }


}
