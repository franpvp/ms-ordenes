package com.example.msordenes.application.controller;

import com.example.msordenes.application.dto.OrdenEstadoMetricDto;
import com.example.msordenes.application.service.MetricsService;
import com.example.msordenes.application.service.UsuarioMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.msordenes.application.util.Constantes.ESTADO_CORRECTO;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;
    private final UsuarioMetricService usuarioMetricService;

    /**
     * Métricas por estado de órdenes del día (PAGO_CORRECTO, PAGO_ERROR, etc.)
     */
    @GetMapping("/ordenes/hoy")
    public ResponseEntity<List<OrdenEstadoMetricDto>> obtenerMetricasOrdenesHoy() {
        return ResponseEntity.ok(metricsService.obtenerMetricasHoy());
    }

    /**
     * Cantidad de órdenes correctas del día
     */
    @GetMapping("/ordenes/hoy/correctas")
    public ResponseEntity<Long> obtenerCorrectasHoy() {
        Long cantidad = metricsService.obtenerCantidadPorEstadoHoy(ESTADO_CORRECTO);
        return ResponseEntity.ok(cantidad);
    }

    /**
     * Usuarios activos del sistema
     */
    @GetMapping("/usuarios/activos")
    public ResponseEntity<Long> obtenerUsuariosActivos() {
        return ResponseEntity.ok(usuarioMetricService.contarUsuariosActivos());
    }
}