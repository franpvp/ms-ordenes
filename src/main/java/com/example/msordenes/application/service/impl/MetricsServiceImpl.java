package com.example.msordenes.application.service.impl;

import com.example.msordenes.application.dto.OrdenEstadoMetricDto;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import com.example.msordenes.application.jpa.repository.UsuarioRepository;
import com.example.msordenes.application.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<OrdenEstadoMetricDto> obtenerMetricasHoy() {

        LocalDate today = LocalDate.now();

        LocalDateTime inicio = today.atStartOfDay();
        LocalDateTime fin = today.atTime(23, 59, 59);

        return ordenRepository.countOrdenesPorEstadoHoy(inicio, fin);
    }

    @Override
    public Long obtenerCantidadPorEstadoHoy(String estado) {

        LocalDate today = LocalDate.now();

        LocalDateTime inicio = today.atStartOfDay();
        LocalDateTime fin = today.atTime(23, 59, 59);

        return ordenRepository.countByEstadoOrdenAndHoy(estado, inicio, fin);
    }

    @Override
    public Long obtenerUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }
}
