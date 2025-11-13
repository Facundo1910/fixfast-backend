package com.fixfast.backend.dto.response;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        Long productoId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}

