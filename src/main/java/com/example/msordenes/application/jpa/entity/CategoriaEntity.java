package com.example.msordenes.application.jpa.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "CATEGORIA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "nombre_directorio")
    private String nombreDirectorio;
}

