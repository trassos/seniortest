package com.trassos.presentation;


import com.trassos.domain.dto.NewOrderItemDTO;
import com.trassos.domain.dto.UpdateOrderItemDTO;
import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Order;
import com.trassos.domain.model.OrderItem;
import com.trassos.domain.service.OrderItemService;
import com.trassos.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;
    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }


    @GetMapping("/find-by-order/{orderId}")
    public List<OrderItem> getAllOrderItemByOrderId(@PathVariable UUID orderId) {
        return orderItemService.getAllOrderItemByOrderId(orderId);
    }

    @GetMapping
    public Page<OrderItem> getAllOrderItem(Pageable pageable) {
        return orderItemService.getAllOrders(pageable);
    }

    @GetMapping("/{orderItemId}")
    public OrderItem getOrderItemById(@PathVariable UUID orderItemId) {
        return orderItemService.getOrderItemById(orderItemId);
    }

    @PostMapping
    public ResponseEntity<?> createOrderItem(@RequestBody NewOrderItemDTO newOrderItem) {
        try {
            OrderItem createdOrderItem = orderItemService.createOrderItem(newOrderItem);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdOrderItem);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao criar o item do pedido");
        }
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<?> updateOrderItem(@PathVariable UUID orderItemId, @RequestBody UpdateOrderItemDTO updatedOrderItem) {
        try {
            OrderItem orderItem = orderItemService.updateOrderItem(orderItemId, updatedOrderItem);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(orderItem);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao atualizar o item do pedido");
        }
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable UUID orderItemId) {
        try {
            boolean deleted = orderItemService.deleteOrderItem(orderItemId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(deleted);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao deletar o item do pedido");
        }
    }

}