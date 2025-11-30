package com.example.msordenes.application.mapper;


import com.example.msordenes.application.dto.OrdenResponse;
import com.example.msordenes.domain.model.Orden;
import org.springframework.stereotype.Component;

@Component
public class OrdenDtoMapper {

    public OrdenResponse toResponse(Orden orden) {
        return OrdenResponse.builder()
                .idOrden(orden.getIdOrden())
                .fechaOrden(orden.getFechaOrden())
                .estado(orden.getEstadoOrden())
                .idCarrito(orden.getIdCarrito())
                .build();
    }
}
