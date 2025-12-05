package com.example.msordenes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PagoPendienteEvent {
    private Long idOrden;
    private Long idMetodoPago;
    private LocalDateTime fechaSolicitud;
}
