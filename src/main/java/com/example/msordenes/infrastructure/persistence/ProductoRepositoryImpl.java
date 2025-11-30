package com.example.msordenes.infrastructure.persistence;

import com.example.msordenes.domain.model.Producto;
import com.example.msordenes.domain.repository.ProductoRepository;
import com.example.msordenes.infrastructure.mapper.ProductoEntityMapper;
import com.example.msordenes.infrastructure.persistence.jpa.ProductoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductoRepositoryImpl implements ProductoRepository {

    private final ProductoJpaRepository productoJpaRepository;
    private final ProductoEntityMapper productoEntityMapper;

    @Override
    public Optional<Producto> findById(Long idProducto) {
        return productoJpaRepository.findById(idProducto)
                .map(productoEntityMapper::toDomain);
    }
}

