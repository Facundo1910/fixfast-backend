package com.fixfast.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String nombreComprador,
        BigDecimal totalBruto,
        BigDecimal descuentoAplicado,
        BigDecimal totalFinal,
        LocalDateTime fechaCreacion,
        List<ItemPedidoResponse> items
) {
}

