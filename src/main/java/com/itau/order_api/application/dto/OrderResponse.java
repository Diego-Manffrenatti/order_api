package com.itau.order_api.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID customerId,
        UUID productId,
        String category,
        String salesChannel,
        String paymentMethod,
        BigDecimal totalMonthlyPremiumAmount,
        BigDecimal insuredAmount,
        Map<String, BigDecimal> coverages,  // ✅ Agora `coverages` é um `Map<String, BigDecimal>`
        List<String> assistances,
        String status,
        LocalDateTime createdAt,
        LocalDateTime finishedAt,
        List<OrderHistoryDTO> history
) {}

