package com.example.msordenes.application.service.impl;

import com.example.msordenes.application.jpa.repository.UsuarioRepository;
import com.example.msordenes.application.service.UsuarioMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioMetricServiceImpl implements UsuarioMetricService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Long contarUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }
}
