package com.itau.order_api.application.service;

import com.itau.order_api.adapters.persistence.OrderRepository;
import com.itau.order_api.application.dto.*;
import com.itau.order_api.adapters.messaging.OrderEventPublisher;

import com.itau.order_api.domain.event.OrderEvent;
import com.itau.order_api.domain.model.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final OrderEventPublisher eventPublisher;
    private final FraudAnalysisService fraudAnalysisService;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository repository, OrderEventPublisher eventPublisher, FraudAnalysisService fraudAnalysisService) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.fraudAnalysisService = fraudAnalysisService;
    }

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .customerId(request.customerId())
                .productId(request.productId())
                .category(Category.valueOf(request.category()))
                .salesChannel(SalesChannel.valueOf(request.salesChannel()))
                .paymentMethod(PaymentMethod.valueOf(request.paymentMethod()))
                .totalMonthlyPremiumAmount(request.totalMonthlyPremiumAmount())
                .insuredAmount(request.insuredAmount())
                .assistances(request.assistances())
                .coverages(request.coverages())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.RECEIVED)
                .build();

        List<OrderHistory> orderHistoryList = new ArrayList<>();

        if (order.getHistory() != null) {
            orderHistoryList.addAll(order.getHistory());
        }

        OrderHistory newHistory = OrderHistory.builder()
                .order(order)
                .status(OrderStatus.RECEIVED)
                .timestamp(LocalDateTime.now())
                .build();

        orderHistoryList.add(newHistory);

        order.setHistory(orderHistoryList);

        repository.save(order);

        eventPublisher.publishOrderEvent(new OrderEvent(order.getOrderId(), order.getStatus().name()));

        RiskClassification classification = fraudAnalysisService.analyzeCustomerRisk(request.customerId(), request.productId()).getClassification();
        boolean isValid = validateInsuranceLimit(classification, request.category(), request.insuredAmount().doubleValue());

        if (!isValid) {
            updateOrderStatus(order.getOrderId(), OrderStatus.REJECTED);
        } else {
            updateOrderStatus(order.getOrderId(), OrderStatus.VALIDATED);
        }

        if (order.getStatus() == OrderStatus.VALIDATED) {
            updateOrderStatus(order.getOrderId(), OrderStatus.PENDING);
        }
        return mapToResponse(order);
    }

    @Override
    public OrderResponse getOrderById(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        order.setStatus(newStatus);

        List<OrderHistory> orderHistoryList = repository.findOrderHistoryByOrderId(order.getOrderId());

        OrderHistory newHistory = OrderHistory.builder()
                .order(order)
                .status(newStatus)
                .timestamp(LocalDateTime.now())
                .build();

        orderHistoryList.add(newHistory);

        order.setHistory(orderHistoryList);

        repository.save(order);

        OrderEvent event = new OrderEvent(orderId, newStatus.name());
        eventPublisher.publishOrderEvent(event);

        logger.info("New status: " + newStatus.name() + " OrderId: " + orderId);

        return mapToResponse(order);
    }

    public List<OrderResponse> getOrdersByCustomerId(UUID customerId) {
        List<Order> orders = repository.findByCustomerId(customerId);
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getProductId(),
                order.getCategory().name(),
                order.getSalesChannel().name(),
                order.getPaymentMethod().name(),
                order.getTotalMonthlyPremiumAmount(),
                order.getInsuredAmount(),
                order.getCoverages(),
                order.getAssistances(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getFinishedAt(),
                Hibernate.isInitialized(order.getHistory()) ?
                        order.getHistory().stream()
                                .map(h -> new OrderHistoryDTO(h.getStatus().name(), h.getTimestamp()))
                                .collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    private boolean validateInsuranceLimit(RiskClassification classification, String category, double insuredAmount) {
        return switch (classification) {
            case REGULAR -> validateRegular(category, insuredAmount);
            case HIGH_RISK -> validateHighRisk(category, insuredAmount);
            case PREFERRED -> validatePreferred(category, insuredAmount);
            case NO_INFO -> validateNoInfo(category, insuredAmount);
            default -> false;
        };
    }

    private boolean validateRegular(String category, double insuredAmount) {
        return switch (category.toUpperCase()) {
            case "LIFE", "RESIDENTIAL" -> insuredAmount <= 500_000.00;
            case "AUTO" -> insuredAmount <= 350_000.00;
            default -> insuredAmount <= 255_000.00;
        };
    }

    private boolean validateHighRisk(String category, double insuredAmount) {
        return switch (category.toUpperCase()) {
            case "AUTO" -> insuredAmount <= 250_000.00;
            case "RESIDENTIAL" -> insuredAmount <= 150_000.00;
            default -> insuredAmount <= 125_000.00;
        };
    }

    private boolean validatePreferred(String category, double insuredAmount) {
        return switch (category.toUpperCase()) {
            case "LIFE" -> insuredAmount < 800_000.00;
            case "AUTO", "RESIDENTIAL" -> insuredAmount < 450_000.00;
            default -> insuredAmount <= 375_000.00;
        };
    }

    private boolean validateNoInfo(String category, double insuredAmount) {
        return switch (category.toUpperCase()) {
            case "LIFE", "RESIDENTIAL" -> insuredAmount <= 200_000.00;
            case "AUTO" -> insuredAmount <= 75_000.00;
            default -> insuredAmount <= 55_000.00;
        };
    }
}
