package com.example.msordenes.application.jpa.repository;

import com.example.msordenes.application.jpa.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    boolean existsById(Long id);
}
