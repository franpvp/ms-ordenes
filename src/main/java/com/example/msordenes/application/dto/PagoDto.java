package com.example.msordenes.application.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class PagoDto {
    private Long idOrden;
    private Long idMetodoPago;
    private Integer monto;
    private boolean reprocesado;
}
