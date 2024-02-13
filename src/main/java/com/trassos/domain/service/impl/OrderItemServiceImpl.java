package com.trassos.domain.service.impl;

import com.trassos.domain.dto.NewOrderItemDTO;
import com.trassos.domain.dto.UpdateOrderItemDTO;
import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Order;
import com.trassos.domain.model.OrderItem;
import com.trassos.domain.service.OrderItemService;
import com.trassos.domain.service.OrderProcessingService;
import com.trassos.persistence.OrderItemJpaRepository;
import com.trassos.persistence.OrderJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderProcessingService orderProcessingService;
    private final OrderItemJpaRepository orderItemJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    @Autowired
    public OrderItemServiceImpl(OrderItemJpaRepository orderItemJpaRepository, OrderProcessingService orderProcessingService, OrderJpaRepository orderJpaRepository) {
        this.orderItemJpaRepository = orderItemJpaRepository;
        this.orderProcessingService = orderProcessingService;
        this.orderJpaRepository = orderJpaRepository;
    }


    @Override
    @Transactional
    public OrderItem createOrderItem(NewOrderItemDTO orderItem) {
        return orderProcessingService.createOrderItem(orderItem);
    }

    @Override
    @Transactional
    public OrderItem updateOrderItem(UUID orderItemId, UpdateOrderItemDTO updatedOrderItem) {
        return orderProcessingService.updateOrderItem(orderItemId, updatedOrderItem);
    }

    @Override
    @Transactional
    public boolean deleteOrderItem(UUID orderItemId) {
        return orderProcessingService.deleteOrderItem(orderItemId);
    }

    @Override
    public Page<OrderItem> getAllOrders(Pageable pageable) {
        return orderItemJpaRepository.findAll(pageable);
    }

    @Override
    public OrderItem getOrderItemById(UUID orderItemId) {
        return orderItemJpaRepository.findById(orderItemId).orElseThrow(() -> new SeniorStandardException("Item do pedido não encontrado."));
    }

    @Override
    public List<OrderItem> getAllOrderItemByOrderId(UUID orderId) {
        Order order = orderJpaRepository.findById(orderId).orElseThrow(() -> new SeniorStandardException("Pedido não encontrado."));
        return orderItemJpaRepository.findAllByOrder(order);
    }

}

