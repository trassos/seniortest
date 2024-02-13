package com.trassos.persistence;

import com.trassos.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    @Modifying
    @Query("Update Order o SET o.status = 'CONFIRMED' WHERE o.id = :orderId")
    void confirmOrder(@Param("orderId") UUID orderId);
}
