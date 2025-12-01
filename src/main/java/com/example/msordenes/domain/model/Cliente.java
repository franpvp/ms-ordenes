package com.example.msordenes.domain.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class Cliente {
    private Long id;
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String ciudad;
    private LocalDate fechaRegistro = LocalDate.now();
}

