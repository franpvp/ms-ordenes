package com.example.msordenes.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Despacho {

    private Long id;
    private String nombreDestinatario;
    private String apellidoDestinatario;
    private String telefono;
    private String direccion;
    private String region;
    private String ciudadComuna;
    private String codigoPostal;
}