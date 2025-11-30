package com.example.msordenes.infrastructure.persistence.jpa;

import com.example.msordenes.infrastructure.persistence.entity.DetalleCarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetalleCarritoJpaRepository extends JpaRepository<DetalleCarritoEntity, Long> {

    Optional<DetalleCarritoEntity> findByCarritoIdAndProductoId(Long idCarrito, Long idProducto);
}

