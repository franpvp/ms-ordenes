package com.example.msordenes.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PagoOkEvent {
    private Long idOrden;
    private Long idPago;
    private Integer monto;
    private LocalDateTime fechaOk;

}
