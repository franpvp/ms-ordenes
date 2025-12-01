package com.example.msordenes.infrastructure.persistence.jpa;

import com.example.msordenes.infrastructure.persistence.entity.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoJpaRepository extends JpaRepository<CarritoEntity, Long> {

    Optional<CarritoEntity> findByCliente_IdAndEstadoCarrito_Nombre(Long idCliente, String nombreEstado);


    Optional<CarritoEntity> findById(Long idCliente);

}
