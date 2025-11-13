package com.fixfast.backend.mapper;

import com.fixfast.backend.dto.request.ProductoRequest;
import com.fixfast.backend.dto.response.ProductoResponse;
import com.fixfast.backend.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        return new Producto(
                request.nombre(),
                request.precioUnitario(),
                request.stockActual(),
                request.proveedor()
        );
    }

    public ProductoResponse toResponse(Producto entity) {
        return new ProductoResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getPrecioUnitario(),
                entity.getStockActual(),
                entity.getProveedor()
        );
    }
}