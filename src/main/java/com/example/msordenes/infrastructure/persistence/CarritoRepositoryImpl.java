package com.example.msordenes.infrastructure.persistence;

import com.example.msordenes.domain.model.Carrito;
import com.example.msordenes.domain.repository.CarritoRepository;
import com.example.msordenes.infrastructure.mapper.CarritoEntityMapper;
import com.example.msordenes.infrastructure.persistence.entity.CarritoEntity;
import com.example.msordenes.infrastructure.persistence.jpa.CarritoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CarritoRepositoryImpl implements CarritoRepository {

    private final CarritoJpaRepository carritoJpaRepository;
    private final CarritoEntityMapper carritoEntityMapper;

    @Override
    public Optional<Carrito> buscarCarritoActivoPorCliente(Long idCliente) {
        return carritoJpaRepository.findByCliente_IdAndEstadoCarrito_Nombre(idCliente, "ACTIVO")
                .map(carritoEntityMapper::toDomain);
    }

    @Override
    public Optional<Carrito> buscarCarritoPorId(Long idCarrito) {
        return carritoJpaRepository.findById(idCarrito)
                .map(carritoEntityMapper::toDomain);
    }

    @Override
    public Carrito guardarCarrito(Carrito carrito) {
        CarritoEntity entity = carritoEntityMapper.toEntity(carrito);
        CarritoEntity saved = carritoJpaRepository.save(entity);
        return carritoEntityMapper.toDomain(saved);
    }
}
