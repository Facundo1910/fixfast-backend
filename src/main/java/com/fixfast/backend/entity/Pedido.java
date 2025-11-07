package com.fixfast.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_comprador", nullable = false)
    private String nombreComprador;

    @Column(name = "total_bruto", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalBruto = BigDecimal.ZERO;

    @Column(name = "descuento_aplicado", nullable = false, precision = 19, scale = 2)
    private BigDecimal descuentoAplicado = BigDecimal.ZERO;

    @Column(name = "total_final", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalFinal = BigDecimal.ZERO;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();

    protected Pedido() {
        // Constructor requerido por JPA
    }

    public Pedido(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public void agregarItem(PedidoItem item) {
        item.setPedido(this);
        this.items.add(item);
    }

    public void calcularTotales(BigDecimal totalBruto, BigDecimal descuentoAplicado) {
        this.totalBruto = totalBruto.setScale(2, RoundingMode.HALF_UP);
        this.descuentoAplicado = descuentoAplicado.setScale(2, RoundingMode.HALF_UP);
        this.totalFinal = this.totalBruto.subtract(this.descuentoAplicado).setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    public BigDecimal getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public BigDecimal getTotalFinal() {
        return totalFinal;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public List<PedidoItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

