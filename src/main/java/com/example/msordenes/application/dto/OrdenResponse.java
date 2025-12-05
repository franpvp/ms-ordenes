package com.example.msordenes.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrdenResponse {

    private Long idOrden;
    private LocalDateTime fechaOrden;
    private String estado;
    private Long idCarrito;
}
