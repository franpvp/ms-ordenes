package com.example.msordenes.application.service.impl;

import com.example.msordenes.application.dto.*;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.jpa.entity.*;
import com.example.msordenes.application.jpa.repository.DespachoRepository;
import com.example.msordenes.application.jpa.repository.DetalleOrdenRepository;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import com.example.msordenes.application.jpa.repository.ProductoRepository;
import com.example.msordenes.application.kafka.producer.PagoPendienteProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenServiceImplTest {

    @Mock
    private PagoPendienteProducer pagoPendienteProducer;

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private DespachoRepository despachoRepository;

    @Mock
    private DetalleOrdenRepository detalleOrdenRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private OrdenServiceImpl ordenServiceImpl;

    // Datos base compartidos
    private CategoriaEntity categoriaEntity;
    private ProductoEntity productoEntity;
    private ClienteEntity clienteEntity;
    private DespachoDto despachoDto;
    private DespachoEntity despachoEntityBase;
    private PagoDto pagoDtoBase;
    private DetalleOrdenDto detalleOrdenDtoBase;

    @BeforeEach
    void setUp() {
        categoriaEntity = CategoriaEntity.builder()
                .id(1L)
                .nombre("Tarjetas de Video")
                .descripcion("GPU y tarjetas gráficas")
                .nombreDirectorio("tarjetas-graficas")
                .build();

        productoEntity = ProductoEntity.builder()
                .id(5L)
                .nombre("RTX 4080")
                .descripcion("GPU")
                .marca("NVIDIA")
                .precio(100000)
                .imagenUrl("http://imagen")
                .categoria(categoriaEntity) // IMPORTANTE para mapear idCategoria en el DTO
                .build();

        clienteEntity = ClienteEntity.builder()
                .id(123L)
                .nombre("Francisca")
                .apellido("Valdivia")
                .telefono("123456789")
                .direccion("Av. Siempre Viva 123")
                .ciudad("Santiago")
                .fechaRegistro(LocalDateTime.now())
                .build();

        despachoDto = DespachoDto.builder()
                .nombreDestinatario("Francisca")
                .apellidoDestinatario("Valdivia")
                .telefono("123456789")
                .direccion("Av. Siempre Viva 123")
                .region("RM")
                .ciudadComuna("Santiago")
                .codigoPostal("8320000")
                .build();

        despachoEntityBase = DespachoEntity.builder()
                .id(1L)
                .nombreDestinatario(despachoDto.getNombreDestinatario())
                .apellidoDestinatario(despachoDto.getApellidoDestinatario())
                .telefono(despachoDto.getTelefono())
                .direccion(despachoDto.getDireccion())
                .region(despachoDto.getRegion())
                .ciudadComuna(despachoDto.getCiudadComuna())
                .codigoPostal(despachoDto.getCodigoPostal())
                .build();

        pagoDtoBase = PagoDto.builder()
                .idMetodoPago(1L)
                .monto(200000)
                .reprocesado(true) // el servicio lo debe cambiar a false
                .build();

        detalleOrdenDtoBase = DetalleOrdenDto.builder()
                .idProducto(productoEntity.getId())
                .cantidad(2)
                .build();
    }

    @Test
    void crearOrdenTest() {
        // Arrange
        OrdenDto request = OrdenDto.builder()
                .idCliente(clienteEntity.getId())
                .despachoDto(despachoDto)
                .pagoDto(pagoDtoBase)
                .listaDetalle(List.of(detalleOrdenDtoBase))
                .build();

        when(despachoRepository.save(any(DespachoEntity.class)))
                .thenReturn(despachoEntityBase);

        OrdenEntity ordenGuardada = OrdenEntity.builder()
                .id(10L)
                .cliente(clienteEntity)
                .despacho(despachoEntityBase)
                .fechaOrden(LocalDateTime.now())
                .estadoOrden("PAGO_PENDIENTE")
                .build();
        when(ordenRepository.save(any(OrdenEntity.class))).thenReturn(ordenGuardada);

        when(productoRepository.findById(productoEntity.getId()))
                .thenReturn(Optional.of(productoEntity));

        DetalleOrdenEntity detalleOrdenEntity = DetalleOrdenEntity.builder()
                .id(100L)
                .orden(ordenGuardada)
                .producto(productoEntity)
                .cantidad(detalleOrdenDtoBase.getCantidad())
                .build();
        when(detalleOrdenRepository.saveAll(anyList()))
                .thenReturn(List.of(detalleOrdenEntity));

        // Act
        OrdenDto result = ordenServiceImpl.crearOrden(request);

        // Assert
        ArgumentCaptor<PagoDto> pagoCaptor = ArgumentCaptor.forClass(PagoDto.class);
        verify(pagoPendienteProducer, times(1)).enviar(pagoCaptor.capture());
        PagoDto enviado = pagoCaptor.getValue();

        // pago enviado al topic
        assertThat(enviado.getIdOrden()).isEqualTo(ordenGuardada.getId());
        assertThat(enviado.isReprocesado()).isFalse();
        assertThat(enviado.getMonto()).isEqualTo(pagoDtoBase.getMonto());
        assertThat(enviado.getIdMetodoPago()).isEqualTo(pagoDtoBase.getIdMetodoPago());

        // respuesta
        assertThat(result.getIdOrden()).isEqualTo(ordenGuardada.getId());
        assertThat(result.getIdCliente()).isEqualTo(request.getIdCliente());
        assertThat(result.getDespachoDto().getNombreDestinatario())
                .isEqualTo(despachoDto.getNombreDestinatario());

        assertThat(result.getListaDetalle()).hasSize(1);
        DetalleOrdenDto detalleResult = result.getListaDetalle().get(0);
        assertThat(detalleResult.getIdDetalleOrden()).isEqualTo(detalleOrdenEntity.getId());
        assertThat(detalleResult.getIdProducto()).isEqualTo(productoEntity.getId());
        assertThat(detalleResult.getCantidad()).isEqualTo(detalleOrdenEntity.getCantidad());

        // producto mapeado dentro del detalle
        assertThat(detalleResult.getProducto().getIdProducto()).isEqualTo(productoEntity.getId());
        assertThat(detalleResult.getProducto().getIdCategoria()).isEqualTo(categoriaEntity.getId());
        assertThat(detalleResult.getProducto().getPrecio()).isEqualTo(productoEntity.getPrecio());
    }

    @Test
    void crearOrdenExceptionTest() {
        // Arrange
        OrdenDto request = OrdenDto.builder()
                .idCliente(clienteEntity.getId())
                .despachoDto(despachoDto)
                .pagoDto(pagoDtoBase)
                .listaDetalle(List.of(detalleOrdenDtoBase))
                .build();

        when(despachoRepository.save(any(DespachoEntity.class)))
                .thenReturn(despachoEntityBase);

        when(ordenRepository.save(any(OrdenEntity.class)))
                .thenThrow(new RuntimeException("Error al guardar orden"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> ordenServiceImpl.crearOrden(request));

        verify(pagoPendienteProducer, never()).enviar(any(PagoDto.class));
    }

    @Test
    void buscarOrdenPorClienteTest() {
        // Arrange
        OrdenEntity ultimaOrden = OrdenEntity.builder()
                .id(10L)
                .estadoOrden("PAGO_PENDIENTE")
                .build();

        when(ordenRepository.findTopByClienteIdOrderByFechaOrdenDesc(clienteEntity.getId()))
                .thenReturn(Optional.of(ultimaOrden));

        // Act
        OrdenEstadoDto result = ordenServiceImpl.buscarOrdenPorCliente(clienteEntity.getId());

        // Assert
        assertThat(result.getIdOrden()).isEqualTo(10L);
        assertThat(result.getEstadoOrden()).isEqualTo("PAGO_PENDIENTE");
        verify(ordenRepository, times(1))
                .findTopByClienteIdOrderByFechaOrdenDesc(clienteEntity.getId());
    }

    @Test
    void buscarOrdenPorClienteNoEncontradaTest() {
        // Arrange
        when(ordenRepository.findTopByClienteIdOrderByFechaOrdenDesc(clienteEntity.getId()))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(OrdenNoEncontradaException.class,
                () -> ordenServiceImpl.buscarOrdenPorCliente(clienteEntity.getId()));

        verify(ordenRepository, times(1))
                .findTopByClienteIdOrderByFechaOrdenDesc(clienteEntity.getId());
    }

    @Test
    void buscarOrdenByIdTest() {
        // Arrange
        Long idOrden = 10L;

        MetodoPagoEntity metodoPago = MetodoPagoEntity.builder()
                .id(99L)
                .tipo("TARJETA")
                .build();

        PagoEntity pago = PagoEntity.builder()
                .id(50L)
                .monto(200000)
                .metodoPago(metodoPago)
                .build();

        OrdenEntity orden = OrdenEntity.builder()
                .id(idOrden)
                .cliente(clienteEntity)
                .despacho(despachoEntityBase)
                .pago(pago)
                .fechaOrden(LocalDateTime.now())
                .estadoOrden("PAGO_PENDIENTE")
                .build();

        when(ordenRepository.findById(idOrden)).thenReturn(Optional.of(orden));

        when(productoRepository.findById(productoEntity.getId()))
                .thenReturn(Optional.of(productoEntity));

        DetalleOrdenEntity detalle = DetalleOrdenEntity.builder()
                .id(100L)
                .orden(orden)
                .producto(productoEntity)
                .cantidad(2)
                .build();
        when(detalleOrdenRepository.findByOrden_Id(idOrden))
                .thenReturn(List.of(detalle));

        // Act
        OrdenDto result = ordenServiceImpl.buscarOrdenById(idOrden);

        // Assert
        assertThat(result.getIdOrden()).isEqualTo(idOrden);
        assertThat(result.getIdCliente()).isEqualTo(clienteEntity.getId());

        // despacho
        assertThat(result.getDespachoDto().getIdDespacho()).isEqualTo(despachoEntityBase.getId());
        assertThat(result.getDespachoDto().getNombreDestinatario())
                .isEqualTo(despachoEntityBase.getNombreDestinatario());

        // pago
        assertThat(result.getPagoDto().getIdOrden()).isEqualTo(idOrden);
        assertThat(result.getPagoDto().getIdMetodoPago()).isEqualTo(metodoPago.getId());
        assertThat(result.getPagoDto().getMonto()).isEqualTo(pago.getMonto());

        // detalle + producto
        assertThat(result.getListaDetalle()).hasSize(1);
        DetalleOrdenDto detResult = result.getListaDetalle().get(0);
        assertThat(detResult.getIdDetalleOrden()).isEqualTo(detalle.getId());
        assertThat(detResult.getIdProducto()).isEqualTo(productoEntity.getId());
        assertThat(detResult.getCantidad()).isEqualTo(detalle.getCantidad());

        assertThat(detResult.getProducto().getIdProducto()).isEqualTo(productoEntity.getId());
        assertThat(detResult.getProducto().getIdCategoria()).isEqualTo(categoriaEntity.getId());
        assertThat(detResult.getProducto().getNombre()).isEqualTo(productoEntity.getNombre());
        assertThat(detResult.getProducto().getPrecio()).isEqualTo(productoEntity.getPrecio());
    }

    @Test
    void buscarOrdenByIdNoEncontradaTest() {
        // Arrange
        Long idOrden = 999L;
        when(ordenRepository.findById(idOrden)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(OrdenNoEncontradaException.class,
                () -> ordenServiceImpl.buscarOrdenById(idOrden));

        verify(detalleOrdenRepository, never()).findByOrden_Id(anyLong());
    }
}
