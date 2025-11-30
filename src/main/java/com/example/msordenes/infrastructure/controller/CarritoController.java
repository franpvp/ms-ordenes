package com.example.msordenes.infrastructure.controller;

import com.example.msordenes.application.dto.CarritoResponse;
import com.example.msordenes.application.dto.UpsertItemCarritoRequest;
import com.example.msordenes.application.usecase.ObtenerCarritoPorClienteUseCase;
import com.example.msordenes.application.usecase.ObtenerCarritoPorIdUseCase;
import com.example.msordenes.application.usecase.UpsertItemCarritoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carritos")
@Tag(
        name = "Carritos",
        description = "Operaciones para gestionar el carrito de compras del cliente"
)
public class CarritoController {

    private final UpsertItemCarritoUseCase upsertItemCarritoUseCase;
    private final ObtenerCarritoPorClienteUseCase obtenerCarritoPorClienteUseCase;
    private final ObtenerCarritoPorIdUseCase obtenerCarritoPorIdUseCase;

    @Operation(
            summary = "Agregar/actualizar/eliminar item del carrito ACTIVO",
            description = """
                - Si cantidad > 0: agrega o actualiza el item en el carrito ACTIVO del cliente.
                - Si cantidad = 0: elimina el producto del carrito ACTIVO.
                - Si el cliente no tiene carrito ACTIVO, se crea uno nuevo automáticamente.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Request inválido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/items")
    public CarritoResponse upsertItemCarrito(
            @RequestBody @Valid UpsertItemCarritoRequest request
    ) {
        log.info("[ms-ordenes] POST /api/v1/carritos/items request={}", request);
        return upsertItemCarritoUseCase.ejecutar(request);
    }

    @Operation(
            summary = "Obtener carrito por ID",
            description = "Devuelve el carrito (y sus items) a partir del idCarrito."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito encontrado"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{idCarrito}")
    public CarritoResponse obtenerCarrito(@PathVariable Long idCarrito) {
        log.info("[ms-ordenes] GET /api/v1/carritos/{}", idCarrito);
        return obtenerCarritoPorIdUseCase.ejecutar(idCarrito);
    }

    @Operation(
            summary = "Obtener carrito ACTIVO por cliente",
            description = "Devuelve el carrito ACTIVO asociado al cliente, si existe."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito activo encontrado"),
            @ApiResponse(responseCode = "404", description = "El cliente no tiene carrito activo"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{idCliente}")
    public CarritoResponse obtenerCarritoPorCliente(@PathVariable Long idCliente) {
        log.info("[ms-ordenes] GET /api/v1/carritos/cliente/{}", idCliente);
        return obtenerCarritoPorClienteUseCase.ejecutar(idCliente);
    }



}
