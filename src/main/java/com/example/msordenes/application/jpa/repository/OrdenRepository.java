package com.example.msordenes.application.jpa.repository;

import com.example.msordenes.application.jpa.entity.OrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends JpaRepository<OrdenEntity, Long> {
}

