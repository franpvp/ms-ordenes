package com.example.msordenes.infrastructure.persistence.jpa;

import com.example.msordenes.infrastructure.persistence.entity.EstadoCarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoCarritoJpaRepository extends JpaRepository<EstadoCarritoEntity, Long> {

    Optional<EstadoCarritoEntity> findByNombre(String estadoCarrito);
}

