package com.example.msordenes.application.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CarritoResponse {

    private Long idCarrito;
    private Long idCliente;
    private String estadoCarrito;
    private BigDecimal total;
    private List<CarritoItemResponse> items;
}
