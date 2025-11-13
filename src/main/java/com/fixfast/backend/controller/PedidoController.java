package com.fixfast.backend.controller;

import com.fixfast.backend.dto.request.CrearPedidoRequest;
import com.fixfast.backend.dto.response.PedidoResponse;
import com.fixfast.backend.dto.response.PedidoResumenResponse;
import com.fixfast.backend.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody CrearPedidoRequest request) {
        PedidoResponse pedidoResponse = pedidoService.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoResponse);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResumenResponse>> listarPedidos() {
        List<PedidoResumenResponse> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPedido(@PathVariable Long id) {
        PedidoResponse pedido = pedidoService.obtenerPedido(id);
        return ResponseEntity.ok(pedido);
    }
}

