package com.example.msordenes.application.service.impl;

import com.example.msordenes.application.dto.DespachoDto;
import com.example.msordenes.application.jpa.entity.DespachoEntity;
import com.example.msordenes.application.jpa.repository.DespachoRepository;
import com.example.msordenes.application.mapper.DespachoEntityMapper;
import com.example.msordenes.application.service.DespachoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class DespachoServiceImpl implements DespachoService {

    private final DespachoRepository despachoRepository;

    @Override
    public void guardarDespacho(DespachoDto request) {
        DespachoEntity despacho = DespachoEntityMapper.mapearAEntity(request);
        despachoRepository.save(despacho);
    }
}
