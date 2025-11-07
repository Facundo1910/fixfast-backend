package com.fixfast.backend.exception;

public class PedidoNoEncontradoException extends RuntimeException {

    public PedidoNoEncontradoException(Long id) {
        super("El pedido con id " + id + " no fue encontrado");
    }
}

