package com.trassos.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateOrderItemDTO {

    private UUID id;
    private UUID productId;
    private BigDecimal itemQuantity;

    public UpdateOrderItemDTO() {
    }

    public UpdateOrderItemDTO(UUID id, UUID productId, BigDecimal itemQuantity) {
        this.id = id;
        this.productId = productId;
        this.itemQuantity = itemQuantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
