package com.example.msordenes.application.usecase;

import com.example.msordenes.application.dto.CarritoResponse;
import com.example.msordenes.application.mapper.CarritoResponseMapper;
import com.example.msordenes.domain.exception.CarritoNoEncontradoException;
import com.example.msordenes.domain.model.Carrito;
import com.example.msordenes.domain.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObtenerCarritoPorClienteUseCase {

    private final CarritoRepository carritoRepository;
    private final CarritoResponseMapper carritoResponseMapper;

    @Transactional(readOnly = true)
    public CarritoResponse ejecutar(Long idCliente) {
        Carrito carrito = carritoRepository
                .buscarCarritoActivoPorCliente(idCliente)
                .orElseThrow(() -> new CarritoNoEncontradoException(
                        "Carrito no encontrado para cliente: " + idCliente
                ));

        return carritoResponseMapper.toResponse(carrito);
    }
}
