package com.example.msordenes.infrastructure.persistence.jpa;

import com.example.msordenes.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenJpaRepository extends JpaRepository<OrdenEntity, Long> {
}

