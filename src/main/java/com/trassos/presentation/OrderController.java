package com.trassos.presentation;


import com.trassos.domain.dto.NewOrderDTO;
import com.trassos.domain.dto.UpdateOrderDTO;
import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Order;
import com.trassos.domain.model.Product;
import com.trassos.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(order);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao buscar o pedido");
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody NewOrderDTO order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdOrder);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao criar o pedido");
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable UUID orderId, @RequestBody UpdateOrderDTO updatedOrder) {
        try {
            Order order = orderService.updateOrder(orderId, updatedOrder);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(order);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao atualizar o pedido");
        }
    }

    @PatchMapping("/confirm/{orderId}")
    public ResponseEntity<?> closeOrder(@PathVariable UUID orderId) {
        try {
            Order order = orderService.confirmOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(order);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao atualizar o pedido");
        }
    }

    @PatchMapping("/change-discount/{orderId}")
    public ResponseEntity<?> changeDiscount(@PathVariable UUID orderId, @RequestParam BigDecimal discountPercentage) {
        try {
            Order order = orderService.changeDiscount(orderId, discountPercentage);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(order);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao atualizar o pedido");
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID orderId) {
        try {
            boolean deleted = orderService.deleteOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(deleted);
        } catch (SeniorStandardException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao deletar o pedido");
        }
    }
}