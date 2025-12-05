package com.example.msordenes.infrastructure.persistence;

import com.example.msordenes.domain.model.Despacho;
import com.example.msordenes.domain.repository.DespachoRepository;
import com.example.msordenes.infrastructure.mapper.DespachoMapper;
import com.example.msordenes.infrastructure.persistence.entity.DespachoEntity;
import com.example.msordenes.infrastructure.persistence.jpa.DespachoJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class DespachoRepositoryImpl implements DespachoRepository {

    private final DespachoJpaRepository despachoRepository;

    @Override
    public Optional<Despacho> obtenerPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public Despacho guardar(Despacho despacho) {
        DespachoEntity entity = DespachoMapper.toEntity(despacho);
        DespachoEntity saved = despachoRepository.save(entity);
        return DespachoMapper.toDomain(saved);
    }
}
