package com.example.msordenes.application.jpa.repository;

import com.example.msordenes.application.jpa.entity.DetalleOrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrdenEntity, Long> {


}

