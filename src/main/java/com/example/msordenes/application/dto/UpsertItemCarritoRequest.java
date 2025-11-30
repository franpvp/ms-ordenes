package com.example.msordenes.application.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertItemCarritoRequest {

    @NotNull
    private Long idCliente;
    @NotNull
    private Long idProducto;
    @Min(0)
    private int cantidad;
}
