package com.example.msordenes.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
public class Orden {
    private Long idOrden;
    private LocalDateTime fechaOrden;
    private String estadoOrden;
    private Long idCarrito;
}
