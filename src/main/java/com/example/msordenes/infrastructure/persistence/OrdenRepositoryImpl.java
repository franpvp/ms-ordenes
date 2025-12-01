package com.example.msordenes.infrastructure.persistence;

import com.example.msordenes.domain.model.Orden;
import com.example.msordenes.domain.repository.OrdenRepository;
import com.example.msordenes.infrastructure.mapper.OrdenEntityMapper;
import com.example.msordenes.infrastructure.persistence.entity.OrdenEntity;
import com.example.msordenes.infrastructure.persistence.jpa.OrdenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdenRepositoryImpl implements OrdenRepository {

    private final OrdenJpaRepository jpa;
    private final OrdenEntityMapper mapper;

    @Override
    public Orden guardar(Orden orden) {
        OrdenEntity entity = mapper.toEntity(orden);
        OrdenEntity saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Orden> buscarPorId(Long idOrden) {
        return jpa.findById(idOrden)
                .map(mapper::toDomain);
    }
}

