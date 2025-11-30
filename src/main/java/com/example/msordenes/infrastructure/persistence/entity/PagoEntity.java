package com.example.msordenes.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PAGO")
@Getter
@Setter
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @Column(name = "id_orden", nullable = false)
    private Long idOrden;

    @Column(name = "id_metodo_pago", nullable = false)
    private Long idMetodoPago;

    @Column(name = "id_estado_pago", nullable = false)
    private Long idEstadoPago;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha_pago", nullable = false)
    private OffsetDateTime fechaPago;
}

