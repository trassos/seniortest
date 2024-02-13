package com.trassos.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private BigDecimal itemQuantity;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID productId;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID orderId;

    public OrderItem() {
    }
    public OrderItem(BigDecimal itemQuantity, UUID productId) {
        this.itemQuantity = itemQuantity;
        this.productId = productId;
    }
    public OrderItem(BigDecimal itemQuantity, Product product) {
        this.itemQuantity = itemQuantity;
        this.product = product;
    }
    public OrderItem(UUID id, Order order, Product product, BigDecimal itemQuantity, BigDecimal discount, BigDecimal total) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.itemQuantity = itemQuantity;
        this.discount = discount;
        this.total = total;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(BigDecimal itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id.equals(orderItem.id) &&
                Objects.equals(order, orderItem.order) &&
                Objects.equals(product, orderItem.product) &&
                itemQuantity.equals(orderItem.itemQuantity) &&
                discount.equals(orderItem.discount) &&
                total.equals(orderItem.total) &&
                Objects.equals(productId, orderItem.productId) &&
                Objects.equals(orderId, orderItem.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, product, itemQuantity, discount, total, productId, orderId);
    }
}
