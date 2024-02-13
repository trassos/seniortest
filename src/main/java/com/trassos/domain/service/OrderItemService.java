package com.trassos.domain.service;

import com.trassos.domain.dto.NewOrderItemDTO;
import com.trassos.domain.dto.UpdateOrderItemDTO;
import com.trassos.domain.model.Order;
import com.trassos.domain.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderItemService {

    OrderItem createOrderItem(NewOrderItemDTO orderItem);

    OrderItem getOrderItemById(UUID orderItemId);

    List<OrderItem> getAllOrderItemByOrderId(UUID orderId);

    OrderItem updateOrderItem(UUID orderItemId, UpdateOrderItemDTO updatedOrderItem);

    boolean deleteOrderItem(UUID orderItemId);

    Page<OrderItem> getAllOrders(Pageable pageable);
}
