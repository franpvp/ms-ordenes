package com.example.msordenes.application.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DespachoDto {
    private Long idDespacho;
    private String nombreDestinatario;
    private String apellidoDestinatario;
    private String telefono;
    private String direccion;
    private String region;
    private String ciudadComuna;
    private String codigoPostal;
}

