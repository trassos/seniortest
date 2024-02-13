package com.trassos.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trassos.domain.enums.OrderStatus;
import com.trassos.domain.enums.ProductType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;
    private BigDecimal value;
    private BigDecimal discount;
    private BigDecimal total;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;


    public Order() {
    }
    public Order(BigDecimal discount, List<OrderItem> orderItems) {
        this.discount = discount;
        this.items = orderItems;
    }
    public Order(UUID id, BigDecimal value, BigDecimal discount, BigDecimal total, List<OrderItem> items, BigDecimal discountPercentage, OrderStatus status) {
        this.id = id;
        this.value = value;
        this.discount = discount;
        this.total = total;
        this.items = items;
        this.discountPercentage = discountPercentage;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(value, order.value) &&
                Objects.equals(discount, order.discount) &&
                Objects.equals(total, order.total) &&
                Objects.equals(items, order.items) &&
                Objects.equals(discountPercentage, order.discountPercentage) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, discount, total, items, discountPercentage, status);
    }
}
