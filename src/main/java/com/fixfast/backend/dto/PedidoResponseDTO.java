package com.fixfast.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponseDTO(
        Long id,
        String nombreComprador,
        LocalDateTime fechaCreacion,
        BigDecimal totalFinal
) {
}

