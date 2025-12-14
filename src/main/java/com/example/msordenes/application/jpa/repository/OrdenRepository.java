package com.example.msordenes.application.jpa.repository;

import com.example.msordenes.application.dto.OrdenEstadoMetricDto;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<OrdenEntity, Long> {

    Optional<OrdenEntity> findTopByClienteIdOrderByFechaOrdenDesc(Long idCliente);

    @Query("""
        SELECT new com.example.msordenes.application.dto.OrdenEstadoMetricDto(o.estadoOrden, COUNT(o))
        FROM OrdenEntity o
        WHERE o.fechaOrden BETWEEN :inicio AND :fin
        GROUP BY o.estadoOrden
    """)
    List<OrdenEstadoMetricDto> countOrdenesPorEstadoHoy(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
        SELECT COUNT(o)
        FROM OrdenEntity o
        WHERE o.estadoOrden = :estado
        AND o.fechaOrden BETWEEN :inicio AND :fin
    """)
    Long countByEstadoOrdenAndHoy(
            @Param("estado") String estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
        SELECT o FROM OrdenEntity o
        LEFT JOIN FETCH o.cliente c
        LEFT JOIN FETCH o.detalles d
        LEFT JOIN FETCH d.producto p
        LEFT JOIN FETCH o.despacho des
        LEFT JOIN FETCH o.pago pag
        WHERE o.id = :id
    """)
    Optional<OrdenEntity> findByIdWithAll(@Param("id") Long id);

    @EntityGraph(attributePaths = {
            "cliente",
            "detalles",
            "detalles.producto",
            "pago",
            "pago.estadoPago",
            "despacho"
    })
    List<OrdenEntity> findByCliente_IdOrderByFechaOrdenDesc(Long idCliente);
}