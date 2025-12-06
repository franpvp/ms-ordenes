package com.example.msordenes.application.jpa.repository;

import com.example.msordenes.application.jpa.entity.DespachoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DespachoRepository extends JpaRepository<DespachoEntity, Long> {
}
