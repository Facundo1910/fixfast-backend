package com.fixfast.backend.service;

import com.fixfast.backend.mapper.PedidoMapper;
import com.fixfast.backend.dto.request.CrearPedidoRequest;
import com.fixfast.backend.dto.request.ItemPedidoRequest;
import com.fixfast.backend.dto.response.PedidoResponse;
import com.fixfast.backend.dto.response.PedidoResumenResponse;
import com.fixfast.backend.entity.Pedido;
import com.fixfast.backend.entity.PedidoItem;
import com.fixfast.backend.entity.Producto;
import com.fixfast.backend.exception.PedidoNoEncontradoException;
import com.fixfast.backend.exception.ProductoNoEncontradoException;
import com.fixfast.backend.exception.StockInsuficienteException;
import com.fixfast.backend.repository.PedidoRepository;
import com.fixfast.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final PedidoMapper pedidoMapper;
    private final DescuentoService descuentoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProductoRepository productoRepository,
                         PedidoMapper pedidoMapper,
                         DescuentoService descuentoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.pedidoMapper = pedidoMapper;
        this.descuentoService = descuentoService;
    }

    @Transactional
    public PedidoResponse crearPedido(CrearPedidoRequest request) {
        Pedido pedido = new Pedido(request.nombreComprador());

        BigDecimal totalBruto = request.items().stream()
                .map(itemRequest -> procesarItemPedido(pedido, itemRequest))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal descuento = descuentoService.calcularDescuento(totalBruto);
        pedido.calcularTotales(totalBruto, descuento);

        Pedido guardado = pedidoRepository.save(pedido);
        return pedidoMapper.toResponse(guardado);
    }

    public List<PedidoResumenResponse> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toPedidoResumen)
                .toList();
    }

    public PedidoResponse obtenerPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
        return pedidoMapper.toResponse(pedido);
    }

    private BigDecimal procesarItemPedido(Pedido pedido, ItemPedidoRequest itemRequest) {
        Producto producto = productoRepository.findById(itemRequest.productoId())
                .orElseThrow(() -> new ProductoNoEncontradoException(itemRequest.productoId()));

        validarStockDisponible(producto, itemRequest.cantidad());
        actualizarStock(producto, itemRequest.cantidad());

        PedidoItem item = pedidoMapper.toPedidoItemEntity(itemRequest, producto);
        pedido.agregarItem(item);

        return item.getSubtotal();
    }


    private void validarStockDisponible(Producto producto, Integer cantidadSolicitada) {
        if (producto.getStockActual() < cantidadSolicitada) {
            throw new StockInsuficienteException(producto.getId());
        }
    }

    private void actualizarStock(Producto producto, Integer cantidadSolicitada) {
        int nuevoStock = producto.getStockActual() - cantidadSolicitada;
        producto.setStockActual(nuevoStock);
    }
}