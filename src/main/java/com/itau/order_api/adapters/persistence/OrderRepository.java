package com.itau.order_api.adapters.persistence;

import com.itau.order_api.domain.model.Order;
import com.itau.order_api.domain.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId")
    List<Order> findByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT o.history FROM Order o WHERE o.orderId = :orderId")
    List<OrderHistory> findOrderHistoryByOrderId(@Param("orderId") UUID orderId);
}
