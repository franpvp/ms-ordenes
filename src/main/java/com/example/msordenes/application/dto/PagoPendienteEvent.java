package com.example.msordenes.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;


@Data
@Builder
public class PagoPendienteEvent {
    private Long idOrden;
    private Long idCliente;
    private Double monto;
    private String moneda;
    private Long idMetodoPago;
    private OffsetDateTime fechaSolicitud;
}
