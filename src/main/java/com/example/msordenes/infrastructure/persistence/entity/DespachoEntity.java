package com.example.msordenes.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DESPACHO")
@Getter
@Setter
public class DespachoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho")
    private Long id;

    @Column(name = "nombre_destinatario", nullable = false, length = 100)
    private String nombreDestinatario;

    @Column(name = "apellido_destinatario", nullable = false, length = 100)
    private String apellidoDestinatario;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "ciudad_comuna", nullable = false, length = 100)
    private String ciudadComuna;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @OneToOne(mappedBy = "despacho")
    private CarritoEntity carrito;
}

