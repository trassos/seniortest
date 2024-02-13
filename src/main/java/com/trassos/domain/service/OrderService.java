package com.trassos.domain.service;

import com.trassos.domain.dto.NewOrderDTO;
import com.trassos.domain.dto.UpdateOrderDTO;
import com.trassos.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {

    Order createOrder(NewOrderDTO order);

    Order getOrderById(UUID orderId);

    Page<Order> getAllOrders(Pageable pageable);

    Order updateOrder(UUID orderId, UpdateOrderDTO updatedOrder);

    boolean deleteOrder(UUID orderId);

    Order confirmOrder(UUID orderId);

    Order changeDiscount(UUID orderId, BigDecimal discountPercentage);
}
