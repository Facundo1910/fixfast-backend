package com.fixfast.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResumenResponse(
        Long id,
        String nombreComprador,
        LocalDateTime fechaCreacion,
        BigDecimal totalFinal
) {
}

