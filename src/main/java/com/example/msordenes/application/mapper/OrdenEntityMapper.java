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

    /**
     * Crear OrdenEntity desde OrdenDto (crear orden)
     */
    public static OrdenEntity toEntity(OrdenDto orden, DespachoEntity despachoGuardado) {

        return OrdenEntity.builder()
                .cliente(
                        ClienteEntity.builder()
                                .id(orden.getIdCliente())
                                .build()
                )
                .estadoOrden(Constantes.PAGO_PENDIENTE)
                .fechaOrden(LocalDateTime.now())
                .despacho(despachoGuardado)
                .build();
    }

    /**
     * Mapear OrdenEntity -> OrdenDto (historial / detalle)
     */
    public static OrdenDto toDto(OrdenEntity entity) {

        OrdenDto dto = new OrdenDto();
        dto.setIdOrden(entity.getId());

        if (entity.getCliente() != null) {
            dto.setIdCliente(entity.getCliente().getId());
        }

        // ==========================
        // DETALLES DE ORDEN
        // ==========================
        if (entity.getDetalles() != null && !entity.getDetalles().isEmpty()) {

            List<DetalleOrdenDto> detallesDto = new ArrayList<>();

            for (DetalleOrdenEntity det : entity.getDetalles()) {

                DetalleOrdenDto detDto = new DetalleOrdenDto();
                detDto.setIdDetalleOrden(det.getId());
                detDto.setCantidad(det.getCantidad());

                if (det.getProducto() != null) {
                    ProductoEntity p = det.getProducto();

                    ProductoDto prodDto = new ProductoDto();
                    prodDto.setIdProducto(p.getId());
                    prodDto.setNombre(p.getNombre());

                    prodDto.setPrecio(p.getPrecio());

                    prodDto.setImagenUrl(p.getImagenUrl());

                    detDto.setProducto(prodDto);
                }

                detallesDto.add(detDto);
            }

            dto.setListaDetalle(detallesDto);
        }

        // ==========================
        // DESPACHO
        // ==========================
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

        // ==========================
        // PAGO
        // ==========================
        if (entity.getPago() != null) {

            PagoEntity pago = entity.getPago();
            PagoDto pagoDto = new PagoDto();
            pagoDto.setMonto(pago.getMonto());

            dto.setPagoDto(pagoDto);
        }

        return dto;
    }

    /**
     * Total formateado CLP (opcional)
     */
    public String getTotalFormateado(OrdenEntity entity) {
        OrdenDto dto = toDto(entity);
        return CurrencyUtils.formatCLP(dto.getTotalPrecio());
    }
}