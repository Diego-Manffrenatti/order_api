package com.itau.order_api.adapters.messaging;

import com.itau.order_api.application.dto.OrderCancelledResponse;
import com.itau.order_api.application.dto.PaymentApprovedResponse;
import com.itau.order_api.application.service.OrderCancellationService;
import com.itau.order_api.application.service.OrderService;
import com.itau.order_api.application.service.PaymentApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "${rabbitmq.queue.name}")
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private final OrderService orderService;
    private final PaymentApprovalService paymentApprovalService;
    private final OrderCancellationService orderCancellationService;

    @Autowired
    public OrderConsumer(OrderService orderService, PaymentApprovalService paymentApprovalService, OrderCancellationService orderCancellationService) {
        this.orderService = orderService;
        this.paymentApprovalService = paymentApprovalService;
        this.orderCancellationService = orderCancellationService;
    }

    @RabbitListener(queues = "payment-approved-queue")
    public void receivePaymentApprovedEvent(@Payload PaymentApprovedResponse response) {
        logger.info("Received payment approved event: {}", response);
        paymentApprovalService.processPaymentApproval(response);
    }

    @RabbitListener(queues = "order-cancelled-queue")
    public void receiveOrderCancelledEvent(@Payload OrderCancelledResponse response) {
        logger.info("Received order cancelled event: {}", response);
        orderCancellationService.processOrderCancellation(response);
    }
}
