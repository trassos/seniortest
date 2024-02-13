package com.trassos.domain.service;

import com.trassos.domain.dto.NewOrderDTO;
import com.trassos.domain.dto.NewOrderItemDTO;
import com.trassos.domain.dto.UpdateOrderDTO;
import com.trassos.domain.dto.UpdateOrderItemDTO;
import com.trassos.domain.enums.ProductType;
import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Order;
import com.trassos.domain.model.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderProcessingService {
    Order createOrder(NewOrderDTO order);

    OrderItem createOrderItem(NewOrderItemDTO orderItem);

    Order updateOrder(UUID orderId, UpdateOrderDTO updatedOrder);

    OrderItem updateOrderItem(UUID orderItemId, UpdateOrderItemDTO updatedOrderItem);

    boolean deleteOrderItem(UUID orderItemId);

    Order changeDiscount(Order order);
}
