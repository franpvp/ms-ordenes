package com.example.msordenes.application.feign;

import com.example.msordenes.application.dto.PagoErrorEvent;
import com.example.msordenes.application.dto.ReintentoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ms-reintento-client",
        url = "${app.ms-reintento.url}"   // configurable en application.yaml
)
public interface ReintentoFeignClient {

    @PostMapping("/api/reintento")
    ReintentoResponseDto reintentar(@RequestBody PagoErrorEvent request);

}
