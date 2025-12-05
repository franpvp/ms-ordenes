package com.example.msordenes.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime fechaOrden = LocalDateTime.now();

    @Column(name = "estado_orden", nullable = false, length = 50)
    private String estadoOrden;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_despacho", referencedColumnName = "id_despacho")
    private DespachoEntity despacho;
}
