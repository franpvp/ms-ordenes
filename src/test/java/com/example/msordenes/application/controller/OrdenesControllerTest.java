package com.example.msordenes.application.controller;

import com.example.msordenes.application.dto.*;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.service.impl.OrdenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para OrdenesController.
 * Sin contexto web (no MockMvc), se testean directamente los métodos del controller.
 */
@ExtendWith(MockitoExtension.class)
class OrdenesControllerTest {

    @Mock
    private OrdenServiceImpl ordenServiceImpl;

    @InjectMocks
    private OrdenesController ordenesController;

    @Test
    void crearOrdenYIniciarPagoTest() {
        // Arrange
        DespachoDto despachoDto = DespachoDto.builder()
                .nombreDestinatario("Francisca")
                .apellidoDestinatario("Valdivia")
                .telefono("123456789")
                .direccion("Av. Siempre Viva 123")
                .region("RM")
                .ciudadComuna("Santiago")
                .codigoPostal("8320000")
                .build();

        PagoDto pagoDto = PagoDto.builder()
                .idMetodoPago(1L)
                .monto(200000)
                .reprocesado(false)
                .build();

        DetalleOrdenDto detalleOrdenDto = DetalleOrdenDto.builder()
                .idProducto(5L)
                .cantidad(2)
                .build();

        OrdenDto request = OrdenDto.builder()
                .idCliente(123L)
                .despachoDto(despachoDto)
                .pagoDto(pagoDto)
                .listaDetalle(List.of(detalleOrdenDto))
                .build();

        OrdenDto responseService = OrdenDto.builder()
                .idOrden(10L)
                .idCliente(123L)
                .despachoDto(despachoDto)
                .pagoDto(pagoDto)
                .listaDetalle(List.of(detalleOrdenDto))
                .build();

        when(ordenServiceImpl.crearOrden(request)).thenReturn(responseService);

        // Act
        ResponseEntity<OrdenDto> response = ordenesController.crearOrdenYIniciarPago(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdOrden()).isEqualTo(10L);
        assertThat(response.getBody().getIdCliente()).isEqualTo(123L);
        assertThat(response.getBody().getDespachoDto().getNombreDestinatario())
                .isEqualTo("Francisca");

        verify(ordenServiceImpl, times(1)).crearOrden(request);
    }

    @Test
    void crearOrdenYIniciarPagoExceptionTest() {
        // Arrange
        OrdenDto request = OrdenDto.builder()
                .idCliente(123L)
                .despachoDto(DespachoDto.builder().build())
                .pagoDto(PagoDto.builder()
                        .idMetodoPago(1L)
                        .monto(1000)
                        .build())
                .listaDetalle(List.of(
                        DetalleOrdenDto.builder()
                                .idProducto(5L)
                                .cantidad(1)
                                .build()
                ))
                .build();

        when(ordenServiceImpl.crearOrden(request))
                .thenThrow(new RuntimeException("Error al crear orden"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> ordenesController.crearOrdenYIniciarPago(request));

        verify(ordenServiceImpl, times(1)).crearOrden(request);
    }

    @Test
    void obtenerUltimaOrdenPorClienteTest() {
        // Arrange
        Long idCliente = 123L;

        OrdenEstadoDto estadoDto = OrdenEstadoDto.builder()
                .idOrden(10L)
                .estadoOrden("PAGO_PENDIENTE")
                .build();

        when(ordenServiceImpl.buscarOrdenPorCliente(idCliente))
                .thenReturn(estadoDto);

        // Act
        ResponseEntity<OrdenEstadoDto> response =
                ordenesController.obtenerUltimaOrdenPorCliente(idCliente);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdOrden()).isEqualTo(10L);
        assertThat(response.getBody().getEstadoOrden()).isEqualTo("PAGO_PENDIENTE");

        verify(ordenServiceImpl, times(1)).buscarOrdenPorCliente(idCliente);
    }

    @Test
    void obtenerUltimaOrdenPorClienteNoEncontradaTest() {
        // Arrange
        Long idCliente = 999L;

        when(ordenServiceImpl.buscarOrdenPorCliente(idCliente))
                .thenThrow(new OrdenNoEncontradaException("Orden no encontrada"));

        // Act + Assert
        assertThrows(OrdenNoEncontradaException.class,
                () -> ordenesController.obtenerUltimaOrdenPorCliente(idCliente));

        verify(ordenServiceImpl, times(1)).buscarOrdenPorCliente(idCliente);
    }

    @Test
    void buscarOrdenPorIdTest() {
        // Arrange
        Long idOrden = 10L;

        DespachoDto despachoDto = DespachoDto.builder()
                .idDespacho(1L)
                .nombreDestinatario("Francisca")
                .apellidoDestinatario("Valdivia")
                .telefono("123456789")
                .direccion("Av. Siempre Viva 123")
                .region("RM")
                .ciudadComuna("Santiago")
                .codigoPostal("8320000")
                .build();

        PagoDto pagoDto = PagoDto.builder()
                .idOrden(idOrden)
                .idMetodoPago(1L)
                .monto(200000)
                .reprocesado(false)
                .build();

        DetalleOrdenDto detalleOrdenDto = DetalleOrdenDto.builder()
                .idDetalleOrden(100L)
                .idProducto(5L)
                .cantidad(2)
                .build();

        OrdenDto dtoService = OrdenDto.builder()
                .idOrden(idOrden)
                .idCliente(123L)
                .despachoDto(despachoDto)
                .pagoDto(pagoDto)
                .listaDetalle(List.of(detalleOrdenDto))
                .build();

        when(ordenServiceImpl.buscarOrdenById(idOrden))
                .thenReturn(dtoService);

        // Act
        ResponseEntity<OrdenDto> response = ordenesController.buscarOrdenPorId(idOrden);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdOrden()).isEqualTo(idOrden);
        assertThat(response.getBody().getIdCliente()).isEqualTo(123L);
        assertThat(response.getBody().getDespachoDto().getIdDespacho()).isEqualTo(1L);

        verify(ordenServiceImpl, times(1)).buscarOrdenById(idOrden);
    }

    @Test
    void buscarOrdenPorIdNoEncontradaTest() {
        // Arrange
        Long idOrden = 999L;

        when(ordenServiceImpl.buscarOrdenById(idOrden))
                .thenThrow(new OrdenNoEncontradaException("Orden no encontrada"));

        // Act + Assert
        assertThrows(OrdenNoEncontradaException.class,
                () -> ordenesController.buscarOrdenPorId(idOrden));

        verify(ordenServiceImpl, times(1)).buscarOrdenById(idOrden);
    }
}
