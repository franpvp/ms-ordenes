package com.example.msordenes.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ESTADO_PAGO")
@Getter
@Setter
public class EstadoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_pago")
    private Long id;

    @Column(name = "estado", nullable = false, unique = true, length = 50)
    private String estado;

}

