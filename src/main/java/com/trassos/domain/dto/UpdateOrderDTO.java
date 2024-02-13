package com.trassos.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class UpdateOrderDTO {


    private BigDecimal discountPercentage;
    private List<UpdateOrderItemDTO> items;

    public UpdateOrderDTO() {}

    public UpdateOrderDTO(BigDecimal discountPercentage, List<UpdateOrderItemDTO> items) {
        this.discountPercentage = discountPercentage;
        this.items = items;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<UpdateOrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<UpdateOrderItemDTO> items) {
        this.items = items;
    }
}
