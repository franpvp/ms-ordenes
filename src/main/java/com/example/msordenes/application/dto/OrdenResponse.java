package com.example.msordenes.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class OrdenResponse {

    private Long idOrden;
    private OffsetDateTime fechaOrden;
    private String estado;
    private Long idCarrito;
}
