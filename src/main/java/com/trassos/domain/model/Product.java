package com.trassos.domain.model;

import com.trassos.domain.enums.ProductType;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = false, updatable = false, unique = true)
    private UUID id;
    @Column(nullable = false)
    private String name;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private ProductType type;

    private boolean active = true;

    public Product() {
    }
    public Product(UUID id, String name, BigDecimal price, ProductType type, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return active == product.active &&
                id.equals(product.id) &&
                name.equals(product.name) &&
                price.equals(product.price) &&
                type == product.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, type, active);
    }
}
