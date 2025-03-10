package com.itau.order_api.application.service;

import com.itau.order_api.application.dto.OrderResponse;
import com.itau.order_api.application.dto.PaymentApprovedResponse;
import com.itau.order_api.domain.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentApprovalService.class);
    private final OrderService orderService;

    public PaymentApprovalService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void processPaymentApproval(PaymentApprovedResponse response) {
        logger.info("Processing payment approval for order: {}", response.getOrderId());

        try {
            OrderResponse updatedOrder = orderService.updateOrderStatus(response.getOrderId(), OrderStatus.APPROVED);
            logger.info("Order {} successfully updated to APPROVED", response.getOrderId());
        } catch (Exception e) {
            logger.error("Error updating order status to APPROVED: {}", e.getMessage(), e);
        }
    }
}
