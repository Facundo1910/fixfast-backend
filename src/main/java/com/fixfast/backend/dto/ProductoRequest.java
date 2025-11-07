package com.fixfast.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotNull(message = "El precio unitario es obligatorio")
        @Positive(message = "El precio unitario debe ser mayor a cero")
        BigDecimal precioUnitario,

        @NotNull(message = "El stock actual es obligatorio")
        @PositiveOrZero(message = "El stock actual debe ser mayor o igual a cero")
        Integer stockActual,

        String proveedor
) {
}

