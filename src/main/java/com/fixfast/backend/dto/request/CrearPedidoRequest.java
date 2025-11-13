package com.fixfast.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CrearPedidoRequest(

        @NotBlank(message = "El nombre del comprador es obligatorio")
        String nombreComprador,

        @NotEmpty(message = "Debe incluir al menos un item en el pedido")
        List<@Valid ItemPedidoRequest> items
) {
}

