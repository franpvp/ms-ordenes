package com.example.msordenes.application.usecase;

import com.example.msordenes.application.dto.CarritoResponse;
import com.example.msordenes.application.dto.UpsertItemCarritoRequest;
import com.example.msordenes.infrastructure.mapper.CarritoEntityMapper;
import com.example.msordenes.domain.exception.ProductoNoEncontradoException;
import com.example.msordenes.domain.model.Carrito;
import com.example.msordenes.domain.model.CarritoItem;
import com.example.msordenes.domain.model.Producto;
import com.example.msordenes.domain.repository.CarritoRepository;
import com.example.msordenes.domain.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpsertItemCarritoUseCase {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final CarritoEntityMapper carritoEntityMapper;

    @Transactional

    public CarritoResponse ejecutar(UpsertItemCarritoRequest request) {
        log.info("[ms-ordenes] Upsert item carrito: {}", request);

        Carrito carrito = carritoRepository
                .buscarCarritoActivoPorCliente(request.getIdCliente())
                .orElseGet(() -> crearNuevoCarritoParaCliente(request.getIdCliente()));

        Long idProducto = request.getIdProducto();
        int cantidad = request.getCantidad();

        if (cantidad == 0) {
            carrito.eliminarItem(idProducto);
        } else {
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new ProductoNoEncontradoException(idProducto));

            CarritoItem item = CarritoItem.builder()
                    .idProducto(producto.getIdProducto())
                    .nombreProducto(producto.getNombre())
                    .precioUnitario(producto.getPrecioUnitario())
                    .cantidad(cantidad)
                    .build();

            carrito.agregarOActualizarItem(item);
        }

        Carrito guardado = carritoRepository.guardarCarrito(carrito);
        return carritoEntityMapper.toResponse(guardado);
    }

    private Carrito crearNuevoCarritoParaCliente(Long idCliente) {
        return Carrito.builder()
                .idCarrito(null)
                .idCliente(idCliente)
                .estado(ESTADO_ACTIVO)
                .fechaCreacion(OffsetDateTime.now())
                .items(new ArrayList<>())
                .build();
    }
}
