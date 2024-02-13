package com.trassos.domain.service.impl;

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
import com.trassos.domain.service.OrderProcessingService;
import com.trassos.persistence.OrderItemJpaRepository;
import com.trassos.persistence.OrderJpaRepository;
import com.trassos.persistence.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderProcessingServiceImpl implements OrderProcessingService {

    private final OrderJpaRepository orderJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    @Autowired
    public OrderProcessingServiceImpl(OrderJpaRepository orderJpaRepository, ProductJpaRepository productJpaRepository, OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }


    @Override
    @Transactional
    public Order createOrder(NewOrderDTO order) {
        Order newOrder = initializeOrder(order);

        validateNewOrderItems(newOrder.getItems());
        populateOrderItems(newOrder.getItems());

        processOrder(newOrder);

        Order savedOrder = orderJpaRepository.save(newOrder);
        return savedOrder;
    }


    @Override
    @Transactional
    public Order updateOrder(UUID orderId, UpdateOrderDTO updatedOrder) {
        Order savedOrder = getOrderById(orderId);
        validateOrder(savedOrder);

        validateUpdateOrderItems(updatedOrder.getItems());
        savedOrder.setDiscountPercentage(updatedOrder.getDiscountPercentage());

        manageOrderItems(savedOrder, updatedOrder.getItems());

        processOrder(savedOrder);

        Order order = orderJpaRepository.save(savedOrder);
        return order;
    }

    @Override
    public Order changeDiscount(Order order) {
        validateOrder(order);
        processOrder(order);
        Order savedOrder = orderJpaRepository.save(order);
        return savedOrder;
    }


    @Override
    public OrderItem createOrderItem(NewOrderItemDTO newOrderItem) {
        validateOrderItem(newOrderItem);

        Product product = getProductById(newOrderItem.getProductId());
        validateProductStatus(product, 0);

        Order order = getOrderById(newOrderItem.getOrderId());
        validateOrder(order);

        OrderItem orderItem = populateNewOrderItem(newOrderItem, product, order);
        order.getItems().add(orderItem);
        resolveOrderItems(order);
        processOrder(order);

        Order savedOrder = orderJpaRepository.save(order);
        OrderItem savedOrderItem = savedOrder.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(newOrderItem.getProductId()))
                .findFirst()
                .orElseThrow(() -> new SeniorStandardException("Item do pedido não encontrado."));
        return savedOrderItem;
    }

    @Override
    public OrderItem updateOrderItem(UUID orderItemId, UpdateOrderItemDTO updatedOrderItem) {
        OrderItem orderItem = getOrderItemById(orderItemId);
        Product product = getProductById(updatedOrderItem.getProductId());
        validateProductStatus(product, 0);

        Order order = orderItem.getOrder();
        validateOrder(order);

        orderItem = populateUpdateOrderItem(updatedOrderItem, product, order);
        order.getItems().add(orderItem);
        resolveUpdateOrderItems(order);
        processOrder(order);
        Order savedOrder = orderJpaRepository.save(order);
        OrderItem savedOrderItem = savedOrder.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(updatedOrderItem.getProductId()))
                .findFirst()
                .orElseThrow(() -> new SeniorStandardException("Item do pedido não encontrado."));

        return savedOrderItem;
    }

    private OrderItem getOrderItemById(UUID orderItemId) {
        OrderItem orderItem = orderItemJpaRepository.findById(orderItemId).orElseThrow(() -> new SeniorStandardException("Item do pedido não encontrado."));
        return orderItem;
    }

    @Override
    public boolean deleteOrderItem(UUID orderItemId) {
        OrderItem orderItem = getOrderItemById(orderItemId);
        Order order = orderItem.getOrder();
        validateOrder(order);
        order.getItems().remove(orderItem);
        resolveOrderItems(order);
        processOrder(order);
        orderJpaRepository.save(order);
        orderItemJpaRepository.deleteById(orderItemId);
        return true;
    }


    private Product getProductById(UUID productId) {
        Product product = productJpaRepository.findById(productId).orElseThrow(() -> new SeniorStandardException("Produto não encontrado."));
        return product;
    }

    private Order getOrderById(UUID orderId) {
        Order savedOrder = orderJpaRepository.findById(orderId).orElseThrow(() -> new SeniorStandardException("Pedido não encontrado."));
        return savedOrder;
    }

    private void processOrder(Order newOrder) {
        populateOrder(newOrder);
        applyDiscountOnItems(newOrder);
        associateOrderItemsWithOrder(newOrder);
    }

    private static void associateOrderItemsWithOrder(Order newOrder) {
        for (OrderItem item : newOrder.getItems()) {
            item.setOrder(newOrder);
        }
    }

    private Order initializeOrder(NewOrderDTO order) {
        Order newOrder = new Order();
        newOrder.setDiscountPercentage(order.getDiscountPercentage().setScale(2, RoundingMode.HALF_UP));
        newOrder.setItems(adaptOrderItems(order));
        return newOrder;
    }

    private BigDecimal calculateOrderValue(Order order) {
        return order.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(item.getItemQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDiscount(Order order, BigDecimal value) {
        BigDecimal discountPercentage = BigDecimal.ZERO;
        if (order.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0) {
            discountPercentage = order.getDiscountPercentage().divide(BigDecimal.valueOf(100));
        }
        return value.multiply(discountPercentage);
    }


    private List<OrderItem> adaptOrderItems(NewOrderDTO order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (NewOrderItemDTO item : order.getItems()) {
            OrderItem newItem = new OrderItem();
            newItem.setProductId(item.getProductId());
            newItem.setItemQuantity(item.getItemQuantity());
            orderItems.add(newItem);
        }
        return orderItems;
    }

    private void manageOrderItems(Order savedOrder, List<UpdateOrderItemDTO> orderItems) {
        List<OrderItem> itemsToRemove = new ArrayList<>();
        for (OrderItem item : savedOrder.getItems()) {
            boolean found = false;
            for (UpdateOrderItemDTO updateItem : orderItems) {
                if (item.getId().equals(updateItem.getId())) {
                    found = true;
                    item.setItemQuantity(updateItem.getItemQuantity());
                    item.setProductId(updateItem.getProductId());
                }
            }
            if (!found) {
                itemsToRemove.add(item);
            }
        }

        savedOrder.getItems().removeAll(itemsToRemove);
        orderItemJpaRepository.deleteAll(itemsToRemove);
        AtomicInteger index = new AtomicInteger(0);
        for (UpdateOrderItemDTO item : orderItems) {
            index.incrementAndGet();
            boolean found = false;
            for (OrderItem savedItem : savedOrder.getItems()) {
                if (savedItem.getId().equals(item.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                OrderItem newItem = new OrderItem();
                newItem.setId(item.getId());
                newItem.setProduct(productJpaRepository.findById(item.getProductId()).orElseThrow(() -> new SeniorStandardException("Produto não encontrado.")));
                validateProductStatus(newItem.getProduct(), index.get());
                newItem.setItemQuantity(item.getItemQuantity());
                if (savedOrder.getItems() == null) {
                    savedOrder.setItems(new ArrayList<>());
                }
                savedOrder.getItems().add(newItem);
                newItem.setOrder(savedOrder);
            }
        }
    }

    private Order populateOrder(Order order) {
        BigDecimal value = calculateOrderValue(order);
        BigDecimal discount = calculateDiscount(order, value).setScale(2, RoundingMode.HALF_UP);

        order.setValue(value);
        order.setDiscount(discount);
        order.setTotal(value.subtract(discount));
        return order;
    }

    private OrderItem populateNewOrderItem(NewOrderItemDTO newOrderItemDTO, Product product, Order order) {
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setOrder(order);
        newOrderItem.setProduct(product);
        newOrderItem.setItemQuantity(newOrderItemDTO.getItemQuantity());
        return newOrderItem;
    }
    private OrderItem populateUpdateOrderItem(UpdateOrderItemDTO updateOrderItemDTO, Product product, Order order) {
        OrderItem orderItem = order.getItems().stream().filter((item) -> item.getId().equals(updateOrderItemDTO.getId())).findFirst().orElseThrow(() -> new SeniorStandardException("Item do pedido não encontrado."));
        orderItem.setId(updateOrderItemDTO.getId());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setItemQuantity(updateOrderItemDTO.getItemQuantity());
        return orderItem;
    }

    public List<OrderItem> populateOrderItems(List<OrderItem> orderItems) {
        AtomicInteger index = new AtomicInteger(0);
        for (OrderItem item : orderItems) {
            index.incrementAndGet();
            Product product = productJpaRepository.findById(item.getProductId()).orElseThrow(() -> new SeniorStandardException("O "+index +"º produto não foi encontrado. Certifique que o ID está correto."));
            validateProductStatus(product, index.get());
            item.setProduct(product);
        }
        return orderItems;
    }

    private Order resolveOrderItems(Order order) {
        BigDecimal discountPercentage = order.getDiscount().divide(order.getValue(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        order.setDiscountPercentage(discountPercentage);

        List<OrderItem> itemsToRemove = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            for (OrderItem i : order.getItems()) {
                if (i.getProduct().getId().equals(item.getProduct().getId())) {
                    if (item.getId() != null && i.getId() == null) {
                        item.setItemQuantity(item.getItemQuantity().add(i.getItemQuantity()));
                        itemsToRemove.add(i);
                    }
                }
            }
        }

        order.getItems().removeAll(itemsToRemove);

        return order;
    }

    private Order resolveUpdateOrderItems(Order order) {
        BigDecimal discountPercentage = order.getDiscount().divide(order.getValue(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        order.setDiscountPercentage(discountPercentage);

        List<OrderItem> itemsToRemove = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            for (OrderItem i : order.getItems()) {
                if (i.getProduct().getId().equals(item.getProduct().getId())) {
                    if (item.getId() != null && i.getId() == null) {
                        item.setItemQuantity(item.getItemQuantity().add(i.getItemQuantity()));
                        itemsToRemove.add(i);
                    }
                    if (item.getId().equals(i.getId()) && !item.getItemQuantity().equals(i.getItemQuantity())) {
                        item.setItemQuantity(i.getItemQuantity());
                    }
                } else {
                    if (item.getId().equals(i.getId()) && !item.getItemQuantity().equals(i.getItemQuantity())) {
                        item.setProduct(i.getProduct());
                        item.setItemQuantity(i.getItemQuantity());
                    }
                }
            }
        }

        order.getItems().removeAll(itemsToRemove);

        return order;
    }

    private Order applyDiscountOnItems(Order order) {
        BigDecimal directDiscount = BigDecimal.ZERO;
        BigDecimal productsValue = order.getItems().stream()
                .filter(item -> item.getProduct().getType() == ProductType.PRODUCT)
                .map(item -> item.getProduct().getPrice().multiply(item.getItemQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        validateDiscount(order, productsValue);

        if (order.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            for (OrderItem item : order.getItems()) {
                if (item.getProduct().getType() == ProductType.PRODUCT) {
                    BigDecimal proportion = item.getProduct().getPrice()
                            .multiply(item.getItemQuantity())
                            .divide(productsValue, 10, RoundingMode.HALF_UP);


                    BigDecimal itemDiscount = order.getDiscount()
                            .multiply(proportion).setScale(2, RoundingMode.HALF_UP);

                    item.setDiscount(itemDiscount);
                    directDiscount = directDiscount.add(itemDiscount);
                }
            }

            BigDecimal difference = order.getDiscount().subtract(directDiscount);
            if (difference.compareTo(BigDecimal.ZERO) > 0) {
                for (OrderItem item : order.getItems()) {
                    if (item.getProduct().getType() == ProductType.PRODUCT) {
                        item.setDiscount(item.getDiscount().add(difference));
                    }
                }
            }
        }

        for (OrderItem item : order.getItems()) {
            item.setTotal(item.getProduct().getPrice()
                    .multiply(item.getItemQuantity())
                    .subtract(item.getDiscount()));
        }

        return order;
    }

    private void validateOrder(Order savedOrder) {
        if (savedOrder.getStatus() == OrderStatus.CONFIRMED) {
            throw new SeniorStandardException("Não é possível alterar um pedido confirmado.");
        }
    }

    public void validateNewOrderItems(List<OrderItem> orderItems) {
        AtomicInteger index = new AtomicInteger(0);
        for (OrderItem item : orderItems) {
            index.incrementAndGet();
            if (item.getItemQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SeniorStandardException("A quantidade do " + index.get() + "º item deve ser maior que zero.");
            }
            if (item.getProductId() == null) {
                throw new SeniorStandardException("O ID do " + index.get() + "º produto é obrigatório.");
            }
        }
    }

    public void validateUpdateOrderItems(List<UpdateOrderItemDTO> orderItems) {
        AtomicInteger index = new AtomicInteger(0);
        for (UpdateOrderItemDTO item : orderItems) {
            index.incrementAndGet();
            if (item.getItemQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SeniorStandardException("A quantidade do " + index.get() + "º item deve ser maior que zero.");
            }
            if (item.getProductId() == null) {
                throw new SeniorStandardException("O ID do " + index.get() + "º produto é obrigatório.");
            }
            if (item.getId() == null) {
                throw new SeniorStandardException("O ID do " + index.get() + "º item é obrigatório.");
            }
        }
    }

    private void validateOrderItem(NewOrderItemDTO orderItem) {
        if (orderItem.getItemQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new SeniorStandardException("A quantidade do item do pedido deve ser maior que zero.");
        }
        if (orderItem.getProductId() == null) {
            throw new SeniorStandardException("O ID do produto é obrigatório.");
        }
        if (orderItem.getOrderId() == null) {
            throw new SeniorStandardException("O ID do pedido é obrigatório.");
        }

    }

    private static void validateDiscount(Order order, BigDecimal productsValue) {
        if (order.getDiscountPercentage().compareTo(new BigDecimal(100)) > 0) {
            throw new SeniorStandardException("A porcentagem de desconto não pode ser superior a 100.");
        }
        if (productsValue.compareTo(order.getDiscount()) < 0) {
            throw new SeniorStandardException("O valor dos produtos não pode ser inferior ao desconto.");
        }
        if (order.getDiscountPercentage().compareTo(BigDecimal.ZERO) < 0) {
            throw new SeniorStandardException("A porcentagem de desconto não pode ser menor que zero.");
        }
    }

    private void validateProductStatus(Product product, int index) {
        if (!product.isActive()) {
            if (index == 0) {
                throw new SeniorStandardException("O Produto se encontra inativo.");
            }
            throw new SeniorStandardException("O "+index+"º Produto, "+product.getName()+", se encontra inativo.");
        }
    }

}
