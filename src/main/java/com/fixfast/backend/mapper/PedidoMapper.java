package com.fixfast.backend.mapper;

import com.fixfast.backend.dto.ItemPedidoResponse;
import com.fixfast.backend.dto.PedidoResponse;
import com.fixfast.backend.dto.PedidoResponseDTO;
import com.fixfast.backend.entity.Pedido;
import com.fixfast.backend.entity.PedidoItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        List<ItemPedidoResponse> items = pedido.getItems()
                .stream()
                .map(this::mapItem)
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getNombreComprador(),
                pedido.getTotalBruto(),
                pedido.getDescuentoAplicado(),
                pedido.getTotalFinal(),
                pedido.getFechaCreacion(),
                items
        );
    }

    public PedidoResponseDTO toResumen(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getNombreComprador(),
                pedido.getFechaCreacion(),
                pedido.getTotalFinal()
        );
    }

    private ItemPedidoResponse mapItem(PedidoItem item) {
        return new ItemPedidoResponse(
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getSubtotal()
        );
    }
}

