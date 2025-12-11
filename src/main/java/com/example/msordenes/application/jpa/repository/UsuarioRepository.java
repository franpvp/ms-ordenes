package com.example.msordenes.application.jpa.repository;

import com.example.msordenes.application.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Long countByActivoTrue();
}
