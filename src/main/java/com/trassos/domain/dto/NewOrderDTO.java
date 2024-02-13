package com.trassos.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class NewOrderDTO {

    private BigDecimal discountPercentage;
    private List<NewOrderItemDTO> items;

    public NewOrderDTO() {
    }
    public NewOrderDTO(BigDecimal discountPercentage, List<NewOrderItemDTO> items) {
        this.discountPercentage = discountPercentage;
        this.items = items;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<NewOrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<NewOrderItemDTO> items) {
        this.items = items;
    }


}
