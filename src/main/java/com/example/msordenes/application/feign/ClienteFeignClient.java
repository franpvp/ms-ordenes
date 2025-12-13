package com.example.msordenes.application.feign;

import com.example.msordenes.application.dto.ClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-clientes",
        url = "${app.ms-clientes.url}"
)
public interface ClienteFeignClient {

    @GetMapping("/api/v1/clientes/{idCliente}")
    ClienteDto obtenerCliente(@PathVariable("idCliente") Long idCliente);
}
