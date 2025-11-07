package com.fixfast.backend.exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(Long productoId) {
        super("El producto con id " + productoId + " no tiene stock suficiente para la cantidad solicitada");
    }
}

