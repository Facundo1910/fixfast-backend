package com.fixfast.backend.repository;

import com.fixfast.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

