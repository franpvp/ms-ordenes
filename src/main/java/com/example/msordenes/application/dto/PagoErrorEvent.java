package com.example.msordenes.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PagoErrorEvent {
    private Long idOrden;
    private Long idPago;
    private String motivoError;
    private LocalDateTime fechaError;
}
