package com.itau.order_api.application.service;

import com.itau.order_api.adapters.persistence.OrderRepository;
import com.itau.order_api.application.dto.OrderCancelledResponse;
import com.itau.order_api.application.dto.OrderResponse;
import com.itau.order_api.domain.model.Order;
import com.itau.order_api.domain.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderCancellationService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCancellationService.class);
    private final OrderService orderService;
    private final OrderRepository repository;

    public OrderCancellationService(OrderService orderService, OrderRepository repository) {
        this.orderService = orderService;
        this.repository = repository;
    }

    public void processOrderCancellation(OrderCancelledResponse response) {
        logger.info("Processing order cancellation for order: {}", response.getOrderId());

        try {

            Optional<Order> optionalOrder = repository.findById(response.getOrderId());

            if (optionalOrder.isEmpty()) {
                logger.error("Order {} not found. Cannot process cancellation.", response.getOrderId());
                return;
            }

            Order order = optionalOrder.get();

            if (order.getStatus() == OrderStatus.APPROVED || order.getStatus() == OrderStatus.REJECTED) {
                logger.warn("Order {} cannot be cancelled.", response.getOrderId());
                return;
            }

            OrderResponse updatedOrder = orderService.updateOrderStatus(response.getOrderId(), OrderStatus.CANCELLED);
            logger.info("Order {} successfully updated to CANCELLED", response.getOrderId());
        } catch (Exception e) {
            logger.error("Error updating order status to CANCELLED: {}", e.getMessage(), e);
        }
    }
}
