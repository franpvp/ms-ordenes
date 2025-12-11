package com.example.msordenes.application.service;

import com.example.msordenes.application.dto.OrdenEstadoMetricDto;

import java.util.List;

public interface MetricsService {

    List<OrdenEstadoMetricDto> obtenerMetricasHoy();
    Long obtenerCantidadPorEstadoHoy(String estado);
    Long obtenerUsuariosActivos();

}
