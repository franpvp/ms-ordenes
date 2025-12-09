package com.example.msordenes.application.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ESTADO_PAGO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_pago")
    private Long id;

    @Column(name = "estado", nullable = false, unique = true, length = 50)
    private String estado;

}

