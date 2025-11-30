package com.example.msordenes.infrastructure.persistence;

import com.example.msordenes.domain.repository.OrdenRepository;
import com.example.msordenes.infrastructure.persistence.jpa.OrdenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

