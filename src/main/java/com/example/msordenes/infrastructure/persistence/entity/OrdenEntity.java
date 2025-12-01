package com.example.msordenes.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "ORDEN")
@Getter
@Setter
public class OrdenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito", nullable = false)
    private CarritoEntity carrito;

    @Column(name = "fecha_orden", nullable = false)
    private OffsetDateTime fechaOrden;

    @Column(name = "estado_orden", nullable = false, length = 50)
    private String estadoOrden;
}
