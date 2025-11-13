package com.fixfast.backend.mapper;

import com.fixfast.backend.dto.request.ItemPedidoRequest;
import com.fixfast.backend.dto.response.ItemPedidoResponse;
import com.fixfast.backend.dto.response.PedidoResponse;
import com.fixfast.backend.dto.response.PedidoResumenResponse;
import com.fixfast.backend.entity.Pedido;
import com.fixfast.backend.entity.PedidoItem;
import com.fixfast.backend.entity.Producto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        List<ItemPedidoResponse> items = pedido.getItems()
                .stream()
                .map(this::toItemPedidoResponse)
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

    public PedidoResumenResponse toPedidoResumen(Pedido pedido) {
        return new PedidoResumenResponse(
                pedido.getId(),
                pedido.getNombreComprador(),
                pedido.getFechaCreacion(),
                pedido.getTotalFinal()
        );
    }

    public PedidoItem toPedidoItemEntity(ItemPedidoRequest itemDTO, Producto producto) {
        BigDecimal subtotal = producto.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(itemDTO.cantidad()));

        return new PedidoItem(
                producto,
                itemDTO.cantidad(),
                producto.getPrecioUnitario(),
                subtotal
        );
    }

    private ItemPedidoResponse toItemPedidoResponse(PedidoItem item) {
        return new ItemPedidoResponse(
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getSubtotal()
        );
    }
}