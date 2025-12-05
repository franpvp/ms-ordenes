package com.example.msordenes.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class IngresarCarritoRequest {

    @NotNull
    private Long idCliente;
    private List<CarritoItemResponse> items;
}
