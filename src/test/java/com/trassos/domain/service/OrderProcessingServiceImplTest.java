package com.trassos.domain.service;

import com.trassos.domain.dto.NewOrderDTO;

import com.trassos.domain.dto.NewOrderItemDTO;
import com.trassos.domain.dto.UpdateOrderDTO;
import com.trassos.domain.dto.UpdateOrderItemDTO;
import com.trassos.domain.enums.OrderStatus;
import com.trassos.domain.enums.ProductType;
import com.trassos.domain.exception.SeniorStandardException;
import com.trassos.domain.model.Order;
import com.trassos.domain.model.OrderItem;
import com.trassos.domain.model.Product;
import com.trassos.domain.service.impl.OrderProcessingServiceImpl;
import com.trassos.persistence.OrderItemJpaRepository;
import com.trassos.persistence.OrderJpaRepository;
import com.trassos.persistence.ProductJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderProcessingServiceImplTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;
    @Mock
    private ProductJpaRepository productJpaRepository;
    @Mock
    private OrderItemJpaRepository orderItemJpaRepository;
    @InjectMocks
    private OrderProcessingServiceImpl orderProcessingService;

    private static OrderItem orderItem1;
    private static OrderItem orderItem2;
    private static OrderItem orderItem3;
    private static NewOrderItemDTO newOrderItemDTO1;
    private static NewOrderItemDTO newOrderItemDTO2;
    private static NewOrderItemDTO newOrderItemDTO3;
    private static NewOrderItemDTO updateOrderItemDTO1;
    private static NewOrderItemDTO updateOrderItemDTO2;
    private static NewOrderItemDTO updateOrderItemDTO3;

    private static List<OrderItem> listOrderItem;
    private static List<OrderItem> listOrderItemWithDiscount;
    private static List<NewOrderItemDTO> listNewOrderItemDTO;
    private static List<NewOrderItemDTO> listNewOrderItemDTOWithDiscount;
    private static List<UpdateOrderItemDTO> listUpdateOrderItemDTO;
    private static List<UpdateOrderItemDTO> listUpdateOrderItemDTOWithDiscount;



    @BeforeAll
    public static void setUp() {

        listOrderItem = new ArrayList<>();
        listOrderItemWithDiscount = new ArrayList<>();
        listNewOrderItemDTO = new ArrayList<>();
        listNewOrderItemDTOWithDiscount = new ArrayList<>();
        listUpdateOrderItemDTO = new ArrayList<>();
        listUpdateOrderItemDTOWithDiscount = new ArrayList<>();

        Product product1 = new Product();
        product1.setId(UUID.fromString("6fce2e1b-f54b-4416-9b1e-0b083f80efd0"));
        product1.setName("Lavação Completa");
        product1.setPrice(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
        product1.setActive(true);
        product1.setType(ProductType.SERVICE);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(UUID.fromString("73a38d00-a900-4654-85e6-f04af47ff831"));
        orderItem1.setOrderId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        orderItem1.setProductId(UUID.fromString("6fce2e1b-f54b-4416-9b1e-0b083f80efd0"));
        orderItem1.setItemQuantity(BigDecimal.valueOf(3).setScale(2, RoundingMode.HALF_UP));
        orderItem1.setDiscount(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP));
        orderItem1.setTotal(BigDecimal.valueOf(300).setScale(2, RoundingMode.HALF_UP));
        orderItem1.setProduct(product1);

        NewOrderItemDTO newOrderItemDTO1 = new NewOrderItemDTO();
        newOrderItemDTO1.setOrderId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        newOrderItemDTO1.setProductId(UUID.fromString("6fce2e1b-f54b-4416-9b1e-0b083f80efd0"));
        newOrderItemDTO1.setItemQuantity(BigDecimal.valueOf(3).setScale(2, RoundingMode.HALF_UP));

        UpdateOrderItemDTO updateOrderItemDTO1 = new UpdateOrderItemDTO();
        updateOrderItemDTO1.setId(UUID.fromString("73a38d00-a900-4654-85e6-f04af47ff831"));
        updateOrderItemDTO1.setProductId(UUID.fromString("6fce2e1b-f54b-4416-9b1e-0b083f80efd0"));
        updateOrderItemDTO1.setItemQuantity(BigDecimal.valueOf(3).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        product2.setName("Cerveja 300ml");
        product2.setPrice(BigDecimal.valueOf(4).setScale(2, RoundingMode.HALF_UP));
        product2.setActive(true);
        product2.setType(ProductType.PRODUCT);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(UUID.fromString("9b1543b0-b42c-453b-a77e-a5708524ef8b"));
        orderItem2.setOrderId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        orderItem2.setProductId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        orderItem2.setItemQuantity(BigDecimal.valueOf(33).setScale(2, RoundingMode.HALF_UP));
        orderItem2.setDiscount(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP));
        orderItem2.setTotal(BigDecimal.valueOf(132).setScale(2, RoundingMode.HALF_UP));
        orderItem2.setProduct(product2);

        NewOrderItemDTO newOrderItemDTO2 = new NewOrderItemDTO();
        newOrderItemDTO2.setOrderId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        newOrderItemDTO2.setProductId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        newOrderItemDTO2.setItemQuantity(BigDecimal.valueOf(33).setScale(2, RoundingMode.HALF_UP));

        UpdateOrderItemDTO updateOrderItemDTO2 = new UpdateOrderItemDTO();
        updateOrderItemDTO2.setId(UUID.fromString("9b1543b0-b42c-453b-a77e-a5708524ef8b"));
        updateOrderItemDTO2.setProductId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        updateOrderItemDTO2.setItemQuantity(BigDecimal.valueOf(33).setScale(2, RoundingMode.HALF_UP));

        // Test com desconto
        Product product3 = new Product();
        product3.setId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        product3.setName("Cerveja 300ml");
        product3.setPrice(BigDecimal.valueOf(4).setScale(2, RoundingMode.HALF_UP));
        product3.setActive(true);
        product3.setType(ProductType.PRODUCT);

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setId(UUID.fromString("9b1543b0-b42c-453b-a77e-a5708524ef8b"));
        orderItem3.setOrderId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        orderItem3.setProductId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        orderItem3.setItemQuantity(BigDecimal.valueOf(33).setScale(2, RoundingMode.HALF_UP));
        orderItem3.setDiscount(BigDecimal.valueOf(43.2).setScale(2, RoundingMode.HALF_UP));
        orderItem3.setTotal(BigDecimal.valueOf(88.80).setScale(2, RoundingMode.HALF_UP));
        orderItem3.setProduct(product2);

        NewOrderItemDTO newOrderItemDTO3 = new NewOrderItemDTO();
        newOrderItemDTO3.setOrderId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        newOrderItemDTO3.setProductId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        newOrderItemDTO3.setItemQuantity(BigDecimal.valueOf(33).setScale(2, RoundingMode.HALF_UP));

        UpdateOrderItemDTO updateOrderItemDTO3 = new UpdateOrderItemDTO();
        updateOrderItemDTO3.setId(UUID.fromString("9b1543b0-b42c-453b-a77e-a5708524ef8b"));
        updateOrderItemDTO3.setProductId(UUID.fromString("92415a5a-beb6-41c4-82d2-0d8a0d6fa1df"));
        updateOrderItemDTO3.setItemQuantity(BigDecimal.valueOf(33).setScale(2, RoundingMode.HALF_UP));
        //

        listOrderItem.add(orderItem1);
        listOrderItem.add(orderItem2);

        listOrderItemWithDiscount.add(orderItem1);
        listOrderItemWithDiscount.add(orderItem3);

        listNewOrderItemDTO.add(newOrderItemDTO1);
        listNewOrderItemDTO.add(newOrderItemDTO2);

        listNewOrderItemDTOWithDiscount.add(newOrderItemDTO1);
        listNewOrderItemDTOWithDiscount.add(newOrderItemDTO3);

        listUpdateOrderItemDTO.add(updateOrderItemDTO1);
        listUpdateOrderItemDTO.add(updateOrderItemDTO2);

        listUpdateOrderItemDTOWithDiscount.add(updateOrderItemDTO1);
        listUpdateOrderItemDTOWithDiscount.add(updateOrderItemDTO3);

    }

    @Test
    public void createOrderTest() {
        NewOrderDTO newOrderDTO = new NewOrderDTO();
        newOrderDTO.setDiscountPercentage(BigDecimal.ZERO);
        newOrderDTO.setItems(listNewOrderItemDTO);

        Order order = new Order();
        order.setDiscountPercentage(BigDecimal.ZERO);
        order.setDiscount(BigDecimal.ZERO);
        order.setItems(listOrderItem);

        for (NewOrderItemDTO newOrderItemDTO : listNewOrderItemDTO) {
            Product product = order.getItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(newOrderItemDTO.getProductId()))
                    .findFirst()
                    .map(OrderItem::getProduct)
                    .orElseThrow(() -> new IllegalStateException("Produto não encontrado para o ID do item do pedido."));
            when(productJpaRepository.findById(newOrderItemDTO.getProductId())).thenReturn(Optional.of(product));
        }
        when(orderJpaRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderProcessingService.createOrder(newOrderDTO);

        assertAll("order",
                () -> assertEquals(new BigDecimal("432").setScale(2, RoundingMode.HALF_UP), createdOrder.getTotal().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(new BigDecimal("432").setScale(2, RoundingMode.HALF_UP), createdOrder.getValue().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), createdOrder.getDiscount().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), createdOrder.getDiscountPercentage().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(OrderStatus.PENDING, createdOrder.getStatus()),
                () -> assertEquals(2, createdOrder.getItems().size())
        );

        for (int i = 0; i < createdOrder.getItems().size(); i++) {
            int finalI = i; // Final variable needed for lambda expression
            assertAll("orderItem",
                    () -> assertEquals(listOrderItem.get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP), createdOrder.getItems().get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItem.get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP), createdOrder.getItems().get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItem.get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP), createdOrder.getItems().get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItem.get(finalI).getProductId(), createdOrder.getItems().get(finalI).getProductId())
            );
        }
        verify(orderJpaRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void createOrderTestWithDiscount() {
        NewOrderDTO newOrderDTO = new NewOrderDTO();
        newOrderDTO.setDiscountPercentage(BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP));
        newOrderDTO.setItems(listNewOrderItemDTOWithDiscount);

        Order order = new Order();
        order.setDiscountPercentage(BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP));
        order.setDiscount(BigDecimal.valueOf(43.20).setScale(2, RoundingMode.HALF_UP));
        order.setItems(listOrderItemWithDiscount);

        for (NewOrderItemDTO newOrderItemDTO : listNewOrderItemDTOWithDiscount) {
            Product product = order.getItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(newOrderItemDTO.getProductId()))
                    .findFirst()
                    .map(OrderItem::getProduct)
                    .orElseThrow(() -> new IllegalStateException("Produto não encontrado para o ID do item do pedido."));
            when(productJpaRepository.findById(newOrderItemDTO.getProductId())).thenReturn(Optional.of(product));
        }
        when(orderJpaRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderProcessingService.createOrder(newOrderDTO);

        assertAll("order",
                () -> assertEquals(new BigDecimal("388.8").setScale(2, RoundingMode.HALF_UP), createdOrder.getTotal().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(new BigDecimal("432").setScale(2, RoundingMode.HALF_UP), createdOrder.getValue().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(43.2).setScale(2, RoundingMode.HALF_UP), createdOrder.getDiscount().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP), createdOrder.getDiscountPercentage().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(OrderStatus.PENDING, createdOrder.getStatus()),
                () -> assertEquals(2, createdOrder.getItems().size())
        );

        for (int i = 0; i < createdOrder.getItems().size(); i++) {
            int finalI = i; // Final variable needed for lambda expression
            assertAll("orderItem",
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP), createdOrder.getItems().get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP), createdOrder.getItems().get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP), createdOrder.getItems().get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getProductId(), createdOrder.getItems().get(finalI).getProductId())
            );
        }

        verify(orderJpaRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void updateOrderTest() {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        UUID orderId = UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824");
        updateOrderDTO.setDiscountPercentage(BigDecimal.ZERO);
        updateOrderDTO.setItems(listUpdateOrderItemDTO);

        Order order = new Order();
        order.setId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        order.setDiscountPercentage(BigDecimal.ZERO);
        order.setDiscount(BigDecimal.ZERO);
        order.setItems(listOrderItem);

        for (UpdateOrderItemDTO updateOrderItemDTO : listUpdateOrderItemDTO) {
            Product product = order.getItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(updateOrderItemDTO.getProductId()))
                    .findFirst()
                    .map(OrderItem::getProduct)
                    .orElseThrow(() -> new IllegalStateException("Produto não encontrado para o ID do item do pedido."));
            when(productJpaRepository.findById(updateOrderItemDTO.getProductId())).thenReturn(Optional.of(product));

            OrderItem orderItem = order.getItems()
                    .stream()
                    .filter(item -> item.getId().equals(updateOrderItemDTO.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Item do pedido não encontrado para o ID do item do pedido."));
            when(orderItemJpaRepository.findById(updateOrderItemDTO.getId())).thenReturn(Optional.of(orderItem));
        }
        when(orderJpaRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderJpaRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order updatedOrder = orderProcessingService.updateOrder(orderId, updateOrderDTO);

        assertAll("order",
                () -> assertEquals(new BigDecimal("432").setScale(2, RoundingMode.HALF_UP), updatedOrder.getTotal().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(new BigDecimal("432").setScale(2, RoundingMode.HALF_UP), updatedOrder.getValue().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), updatedOrder.getDiscount().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), updatedOrder.getDiscountPercentage().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(OrderStatus.PENDING, updatedOrder.getStatus()),
                () -> assertEquals(2, updatedOrder.getItems().size())
        );

        for (int i = 0; i < updatedOrder.getItems().size(); i++) {
            int finalI = i; // Final variable needed for lambda expression
            assertAll("orderItem",
                    () -> assertEquals(listOrderItem.get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP), updatedOrder.getItems().get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItem.get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP), updatedOrder.getItems().get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItem.get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP), updatedOrder.getItems().get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItem.get(finalI).getProductId(), updatedOrder.getItems().get(finalI).getProductId())
            );
        }
        verify(orderJpaRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void updateOrderTestWithDiscount() {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        UUID orderId = UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824");
        updateOrderDTO.setDiscountPercentage(BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP));
        updateOrderDTO.setItems(listUpdateOrderItemDTOWithDiscount);

        Order order = new Order();
        order.setId(UUID.fromString("5448f2a2-9ac0-4e81-8131-86401e3bc824"));
        order.setDiscountPercentage(BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP));
        order.setDiscount(BigDecimal.valueOf(43.20).setScale(2, RoundingMode.HALF_UP));
        order.setItems(listOrderItemWithDiscount);

        for (UpdateOrderItemDTO updateOrderItemDTO : listUpdateOrderItemDTOWithDiscount) {
            Product product = order.getItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(updateOrderItemDTO.getProductId()))
                    .findFirst()
                    .map(OrderItem::getProduct)
                    .orElseThrow(() -> new IllegalStateException("Produto não encontrado para o ID do item do pedido."));
            when(productJpaRepository.findById(updateOrderItemDTO.getProductId())).thenReturn(Optional.of(product));

            OrderItem orderItem = order.getItems()
                    .stream()
                    .filter(item -> item.getId().equals(updateOrderItemDTO.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Item do pedido não encontrado para o ID do item do pedido."));
            when(orderItemJpaRepository.findById(updateOrderItemDTO.getId())).thenReturn(Optional.of(orderItem));
        }
        when(orderJpaRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderJpaRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order updatedOrder = orderProcessingService.updateOrder(orderId, updateOrderDTO);

        assertAll("order",
                () -> assertEquals(new BigDecimal("388.8").setScale(2, RoundingMode.HALF_UP), updatedOrder.getTotal().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(new BigDecimal("432").setScale(2, RoundingMode.HALF_UP), updatedOrder.getValue().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(43.2).setScale(2, RoundingMode.HALF_UP), updatedOrder.getDiscount().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP), updatedOrder.getDiscountPercentage().setScale(2, RoundingMode.HALF_UP)),
                () -> assertEquals(OrderStatus.PENDING, updatedOrder.getStatus()),
                () -> assertEquals(2, updatedOrder.getItems().size())
        );

        for (int i = 0; i < updatedOrder.getItems().size(); i++) {
            int finalI = i; // Final variable needed for lambda expression
            assertAll("orderItem",
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP), updatedOrder.getItems().get(finalI).getTotal().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP), updatedOrder.getItems().get(finalI).getDiscount().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP), updatedOrder.getItems().get(finalI).getItemQuantity().setScale(2, RoundingMode.HALF_UP)),
                    () -> assertEquals(listOrderItemWithDiscount.get(finalI).getProductId(), updatedOrder.getItems().get(finalI).getProductId())
            );
        }

        verify(orderJpaRepository, times(1)).save(any(Order.class));
    }

}




