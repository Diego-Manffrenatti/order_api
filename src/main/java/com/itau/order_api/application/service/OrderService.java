package com.itau.order_api.application.service;

import com.itau.order_api.application.dto.OrderRequest;
import com.itau.order_api.application.dto.OrderResponse;
import com.itau.order_api.domain.model.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(UUID orderId);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus);
    List<OrderResponse> getOrdersByCustomerId(UUID customerId);
}
