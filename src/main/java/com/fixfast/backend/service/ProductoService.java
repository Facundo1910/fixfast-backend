package com.fixfast.backend.service;

import com.fixfast.backend.mapper.ProductoMapper;
import com.fixfast.backend.dto.ProductoRequest;
import com.fixfast.backend.dto.ProductoResponse;
import com.fixfast.backend.entity.Producto;
import com.fixfast.backend.exception.ProductoNoEncontradoException;
import com.fixfast.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Transactional
    public ProductoResponse crearProducto(ProductoRequest request) {
        Producto producto = productoMapper.toEntity(request);
        Producto guardado = productoRepository.save(producto);
        return productoMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
        return productoMapper.toResponse(producto);
    }

    @Transactional
    public ProductoResponse actualizarProducto(Long id, ProductoRequest request) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));

        existente.setNombre(request.nombre());
        existente.setPrecioUnitario(request.precioUnitario());
        existente.setStockActual(request.stockActual());
        existente.setProveedor(request.proveedor());

        Producto actualizado = productoRepository.save(existente);
        return productoMapper.toResponse(actualizado);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        productoRepository.deleteById(id);
    }
}

