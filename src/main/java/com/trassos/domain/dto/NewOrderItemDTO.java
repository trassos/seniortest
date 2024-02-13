package com.trassos.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class NewOrderItemDTO {

    private UUID orderId;
    private UUID productId;
    private BigDecimal itemQuantity;

    public NewOrderItemDTO() {
    }

    public NewOrderItemDTO(UUID productId, BigDecimal itemQuantity, UUID orderId) {
        this.orderId = orderId;
        this.productId = productId;
        this.itemQuantity = itemQuantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(BigDecimal itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
