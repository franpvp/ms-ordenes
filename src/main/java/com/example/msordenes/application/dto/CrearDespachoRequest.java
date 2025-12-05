package com.example.msordenes.application.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearDespachoRequest {

    private String nombreDestinatario;
    private String apellidoDestinatario;
    private String telefono;
    private String direccion;
    private String region;
    private String ciudadComuna;
    private String codigoPostal;
}

