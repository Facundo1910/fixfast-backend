package com.fixfast.backend.service;

import com.fixfast.backend.mapper.PedidoMapper;
import com.fixfast.backend.dto.CrearPedidoRequest;
import com.fixfast.backend.dto.ItemPedidoDTO;
import com.fixfast.backend.dto.PedidoResponse;
import com.fixfast.backend.dto.PedidoResponseDTO;
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
    private final DescuentoTransaccionalService descuentoTransaccionalService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProductoRepository productoRepository,
                         PedidoMapper pedidoMapper,
                         DescuentoTransaccionalService descuentoTransaccionalService) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.pedidoMapper = pedidoMapper;
        this.descuentoTransaccionalService = descuentoTransaccionalService;
    }

    @Transactional
    public PedidoResponse crearPedido(CrearPedidoRequest request) {
        Pedido pedido = new Pedido(request.nombreComprador());
        BigDecimal totalBruto = BigDecimal.ZERO;

        List<ItemPedidoDTO> items = request.items();

        for (ItemPedidoDTO itemRequest : items) {
            Producto producto = productoRepository.findById(itemRequest.productoId())
                    .orElseThrow(() -> new ProductoNoEncontradoException(itemRequest.productoId()));

            validarStockDisponible(producto, itemRequest.cantidad());

            actualizarStock(producto, itemRequest.cantidad());

            BigDecimal subtotal = producto.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(itemRequest.cantidad()));

            PedidoItem item = new PedidoItem(
                    producto,
                    itemRequest.cantidad(),
                    producto.getPrecioUnitario(),
                    subtotal
            );

            pedido.agregarItem(item);
            totalBruto = totalBruto.add(subtotal);
        }

        BigDecimal descuento = descuentoTransaccionalService.calcularDescuento(totalBruto);
        pedido.calcularTotales(totalBruto, descuento);

        Pedido guardado = pedidoRepository.save(pedido);
        return pedidoMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toResumen)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtenerPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
        return pedidoMapper.toResponse(pedido);
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

