package com.example.msordenes.application.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ActualizarCarritoRequest {

    private Long idCarrito;
    private List<CarritoItemResponse> items;
}
