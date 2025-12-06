package com.example.msordenes.application.mapper;

import com.example.msordenes.application.dto.OrdenDto;
import com.example.msordenes.application.jpa.entity.ClienteEntity;
import com.example.msordenes.application.jpa.entity.DespachoEntity;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.util.Constantes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrdenEntityMapper {

    public static OrdenEntity toEntity(OrdenDto orden, DespachoEntity despachoGuardado) {

        return OrdenEntity.builder()
                .cliente(ClienteEntity.builder().id(orden.getIdCliente()).build())
                .estadoOrden(Constantes.PAGO_PENDIENTE)
                .fechaOrden(LocalDateTime.now())
                .despacho(despachoGuardado)
                .build();
    }


}
