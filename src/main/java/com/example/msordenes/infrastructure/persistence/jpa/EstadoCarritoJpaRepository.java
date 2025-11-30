package com.example.msordenes.infrastructure.persistence.jpa;

import com.example.msordenes.infrastructure.persistence.entity.EstadoCarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoCarritoJpaRepository extends JpaRepository<EstadoCarritoEntity, Long> {

    Optional<EstadoCarritoEntity> findByEstadoCarrito(String estadoCarrito);
}

