package com.example.msordenes.application.usecase;

import com.example.msordenes.application.dto.CrearDespachoRequest;
import com.example.msordenes.domain.model.Despacho;
import com.example.msordenes.domain.repository.DespachoRepository;
import com.example.msordenes.infrastructure.mapper.DespachoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CrearDespachoUseCase {

    private final DespachoRepository despachoRepository;

    public Despacho ejecutar(CrearDespachoRequest request) {
        Despacho despacho = DespachoMapper.toDtoRequest(request);
        return despachoRepository.guardar(despacho);
    }
}
