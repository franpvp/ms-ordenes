package com.example.msordenes.application.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "REINTENTO_PAGO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReintentoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reintento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pago", nullable = false)
    private PagoEntity pago;

    @Column(name = "intento_numero", nullable = false)
    private int intentoNumero;

    @Column(name = "estado_reintento", nullable = false)
    private String estadoReintento;

    @Column(name = "fecha_reintento", nullable = false)
    private LocalDateTime fechaReintento;

    @Column(name = "mensaje_error", length = 500)
    private String mensajeError;
}

