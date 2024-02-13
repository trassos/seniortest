package com.trassos.domain.service.impl;

import com.trassos.domain.dto.NewOrderDTO;
import com.trassos.domain.dto.UpdateOrderDTO;
import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Order;
import com.trassos.domain.service.OrderProcessingService;
import com.trassos.domain.service.OrderService;
import com.trassos.persistence.OrderJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderJpaRepository;

    private final OrderProcessingService orderProcessingService;

    @Autowired
    public OrderServiceImpl(OrderJpaRepository orderJpaRepository, OrderProcessingService orderProcessingService) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderProcessingService = orderProcessingService;
    }


    @Override
    @Transactional
    public Order createOrder(NewOrderDTO order) {
        return orderProcessingService.createOrder(order);
    }

    @Override
    @Transactional
    public Order updateOrder(UUID orderId, UpdateOrderDTO updatedOrder) {
        return orderProcessingService.updateOrder(orderId, updatedOrder);
    }

    @Override
    @Transactional
    public boolean deleteOrder(UUID orderId) {
        Optional<Order> order = orderJpaRepository.findById(orderId);
        if (order.isPresent()) {
            orderJpaRepository.delete(order.get());
            return true;
        } else {
            throw new SeniorStandardException("Pedido não encontrado");
        }
    }

    @Override
    @Transactional
    public Order confirmOrder(UUID orderId) {
        orderJpaRepository.confirmOrder(orderId);
        Order order = getOrderById(orderId);
        return order;
    }

    @Override
    public Order changeDiscount(UUID orderId, BigDecimal discountPercentage) {
        Order order = getOrderById(orderId);
        order.setDiscountPercentage(discountPercentage);
        order = orderProcessingService.changeDiscount(order);
        return order;
    }

    @Override
    public Order getOrderById(UUID orderId) {
        return orderJpaRepository.findById(orderId).orElseThrow(() -> new SeniorStandardException("Pedido não encontrado."));
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderJpaRepository.findAll(pageable);
    }
}
