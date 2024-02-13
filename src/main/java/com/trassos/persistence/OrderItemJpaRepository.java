package com.trassos.persistence;

import com.trassos.domain.model.Order;
import com.trassos.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findAllByOrder(Order order);
}
