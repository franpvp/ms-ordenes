package com.example.msordenes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CarritoResponse {

    private Long idCarrito;
    private Long idCliente;
    private String estadoCarrito;
    private BigDecimal total;
    private List<CarritoItemResponse> items;
}
