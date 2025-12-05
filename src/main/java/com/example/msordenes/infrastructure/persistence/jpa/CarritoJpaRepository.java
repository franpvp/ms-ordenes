package com.example.msordenes.infrastructure.persistence.jpa;

import com.example.msordenes.infrastructure.persistence.entity.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CarritoJpaRepository extends JpaRepository<CarritoEntity, Long> {

    Optional<CarritoEntity> findByCliente_IdAndEstadoCarrito_Nombre(Long idCliente, String nombreEstado);


    Optional<CarritoEntity> findById(Long idCliente);

}
