package com.example.msordenes.application.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class PagoPendienteEvent {
    private Long idOrden;
    private Long idMetodoPago;
    private Integer monto;
    private boolean reprocesado;
}
