package com.example.msordenes.application.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReintentoResponseDto {

    private Long idPago;
    private int intentoActual;
    private int maxIntentos;
    private String estado;
    private String mensaje;
}
