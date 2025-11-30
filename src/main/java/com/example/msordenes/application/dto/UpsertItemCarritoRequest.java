package com.example.msordenes.application.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpsertItemCarritoRequest {

    @NotNull
    private Long idCliente;
    @NotNull
    private Long idProducto;
    @Min(0)
    private int cantidad;
}
