package com.example.msordenes.infrastructure.persistence.jpa;


import com.example.msordenes.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository  extends JpaRepository<ClienteEntity, Long> {
}
