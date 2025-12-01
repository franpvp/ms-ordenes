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
public class ObtenerCarritoPorIdUseCase {

    private final CarritoRepository carritoRepository;
    private final CarritoResponseMapper carritoResponseMapper;

    @Transactional(readOnly = true)
    public CarritoResponse ejecutar(Long idCarrito) {
        Carrito carrito = carritoRepository.buscarCarritoPorId(idCarrito)
                .orElseThrow(() -> new CarritoNoEncontradoException(
                        "Carrito no encontrado con idCarrito: " + idCarrito
                ));

        return carritoResponseMapper.toResponse(carrito);
    }
}

