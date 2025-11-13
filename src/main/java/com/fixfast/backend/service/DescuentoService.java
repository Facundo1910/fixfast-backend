package com.fixfast.backend.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DescuentoService {

    private static final BigDecimal UMBRAL_DESCUENTO_MEDIA = BigDecimal.valueOf(1000);
    private static final BigDecimal UMBRAL_DESCUENTO_ALTA = BigDecimal.valueOf(5000);
    private static final BigDecimal PORCENTAJE_MEDIA = BigDecimal.valueOf(0.05);
    private static final BigDecimal PORCENTAJE_ALTA = BigDecimal.valueOf(0.10);

    public BigDecimal calcularDescuento(BigDecimal totalBruto) {
        if (totalBruto == null || totalBruto.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal porcentaje = BigDecimal.ZERO;

        if (totalBruto.compareTo(UMBRAL_DESCUENTO_ALTA) >= 0) {
            porcentaje = PORCENTAJE_ALTA;
        } else if (totalBruto.compareTo(UMBRAL_DESCUENTO_MEDIA) >= 0) {
            porcentaje = PORCENTAJE_MEDIA;
        }

        return totalBruto.multiply(porcentaje).setScale(2, RoundingMode.HALF_UP);
    }
}

