package com.example.msordenes.application.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrdenEstadoDto {

    private Long idOrden;
    private String estadoOrden;
}
