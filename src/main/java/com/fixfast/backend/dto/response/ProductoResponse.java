package com.fixfast.backend.dto.response;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String nombre,
        BigDecimal precioUnitario,
        Integer stockActual,
        String proveedor
) {
}

