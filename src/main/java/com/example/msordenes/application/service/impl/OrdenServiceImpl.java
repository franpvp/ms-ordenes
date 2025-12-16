package com.example.msordenes.application.service.impl;

import com.example.msordenes.application.dto.*;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.jpa.entity.DespachoEntity;
import com.example.msordenes.application.jpa.entity.DetalleOrdenEntity;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.entity.ProductoEntity;
import com.example.msordenes.application.jpa.repository.DespachoRepository;
import com.example.msordenes.application.jpa.repository.DetalleOrdenRepository;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import com.example.msordenes.application.jpa.repository.ProductoRepository;
import com.example.msordenes.application.kafka.producer.PagoPendienteProducer;
import com.example.msordenes.application.mapper.DespachoEntityMapper;
import com.example.msordenes.application.mapper.DetalleOrdenEntityMapper;
import com.example.msordenes.application.mapper.OrdenEntityMapper;
import com.example.msordenes.application.service.OrdenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private final PagoPendienteProducer pagoPendienteProducer;
    private final OrdenRepository ordenRepository;
    private final DespachoRepository despachoRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    @Override
    public OrdenDto crearOrden(OrdenDto request) {
        DespachoEntity despachoGuardado = guardarDespacho(request);
        OrdenEntity orden = guardarOrden(request, despachoGuardado);
        List<DetalleOrdenEntity> detalles = guardarDetalle(request, orden);

        PagoDto pago = request.getPagoDto().toBuilder()
                .idOrden(orden.getId())
                .reprocesado(false)
                .build();

        pagoPendienteProducer.enviar(pago);

        return OrdenDto.builder()
                .idOrden(orden.getId())
                .idCliente(request.getIdCliente())
                .despachoDto(request.getDespachoDto())
                .pagoDto(pago)
                .listaDetalle(mapearDetallesResponse(detalles))
                .build();
    }

    @Override
    public OrdenEstadoDto buscarOrdenPorCliente(Long idCliente) {

        OrdenEntity ultimaOrden = ordenRepository.findTopByClienteIdOrderByFechaOrdenDesc(idCliente).orElseThrow(() ->
                new OrdenNoEncontradaException("No se pudo encontrar la orden"));

        return OrdenEstadoDto.builder()
                .idOrden(ultimaOrden.getId())
                .estadoOrden(ultimaOrden.getEstadoOrden())
                .build();

    }

    @Override
    public OrdenDto buscarOrdenById(Long id) {
        OrdenEntity orden = ordenRepository.findById(id)
                .orElseThrow(() -> new OrdenNoEncontradaException("Orden con id " + id + " no encontrada"));

        // obtener detalles de la orden
        List<DetalleOrdenEntity> detalleEntities = detalleOrdenRepository.findByOrden_Id(id);

        // mapear detalles a DTO
        List<DetalleOrdenDto> listaDetalle = mapearDetallesResponse(detalleEntities);

        // mapear despacho
        DespachoDto despachoDto = DespachoDto.builder()
                .idDespacho(orden.getDespacho().getId())
                .nombreDestinatario(orden.getDespacho().getNombreDestinatario())
                .apellidoDestinatario(orden.getDespacho().getApellidoDestinatario())
                .telefono(orden.getDespacho().getTelefono())
                .direccion(orden.getDespacho().getDireccion())
                .region(orden.getDespacho().getRegion())
                .ciudadComuna(orden.getDespacho().getCiudadComuna())
                .codigoPostal(orden.getDespacho().getCodigoPostal())
                .build();

        PagoDto pagoDto = new PagoDto();
        if (orden.getPago() != null) {
            pagoDto = PagoDto.builder()
                    .idOrden(orden.getId())
                    .idMetodoPago(orden.getPago().getMetodoPago().getId())
                    .monto(orden.getPago().getMonto())
                    .build();
        }
        return OrdenDto.builder()
                .idOrden(orden.getId())
                .idCliente(orden.getCliente().getId())
                .despachoDto(despachoDto)
                .pagoDto(pagoDto)
                .listaDetalle(listaDetalle)
                .build();
    }

    private DespachoEntity guardarDespacho(OrdenDto ordenDto) {
        DespachoEntity despachoMapeado = DespachoEntityMapper.mapearAEntity(ordenDto.getDespachoDto());
        return despachoRepository.save(despachoMapeado);
    }

    private OrdenEntity guardarOrden(OrdenDto ordenDto, DespachoEntity despachoGuardado) {
        OrdenEntity ordenMapeada = OrdenEntityMapper.toEntity(ordenDto, despachoGuardado);
        return ordenRepository.save(ordenMapeada);
    }

    private List<DetalleOrdenEntity> guardarDetalle(OrdenDto ordenDto, OrdenEntity ordenGuardada) {
        List<DetalleOrdenEntity> detalleOrdenEntities = DetalleOrdenEntityMapper.mapearAListaEntity(ordenDto.getListaDetalle(), ordenGuardada.getId());
        return detalleOrdenRepository.saveAll(detalleOrdenEntities);
    }

    private List<DetalleOrdenDto> mapearDetallesResponse(List<DetalleOrdenEntity> detalles) {
        return detalles.stream()
                .map(det -> {

                    ProductoEntity producto = productoRepository.findById(det.getProducto().getId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    ProductoDto productoDto = ProductoDto.builder()
                            .idProducto(producto.getId())
                            .idCategoria(producto.getCategoria().getId())
                            .nombre(producto.getNombre())
                            .descripcion(producto.getDescripcion())
                            .marca(producto.getMarca())
                            .precio(producto.getPrecio())
                            .imagenUrl(producto.getImagenUrl())
                            .build();

                    return DetalleOrdenDto.builder()
                            .idDetalleOrden(det.getId())
                            .idProducto(producto.getId())
                            .cantidad(det.getCantidad())
                            .producto(productoDto)
                            .build();
                })
                .toList();
    }

    public List<OrdenDto> obtenerHistorialCliente(Long idCliente) {

        List<OrdenEntity> ordenes =
                ordenRepository.findByCliente_IdOrderByFechaOrdenDesc(idCliente);

        return ordenes.stream()
                .filter(o ->
                        o.getPago() != null &&
                                o.getPago().getEstadoPago() != null
                )
                .map(OrdenEntityMapper::toDto)
                .toList();
    }





}
