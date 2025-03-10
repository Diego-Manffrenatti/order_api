package com.itau.order_api.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record OrderRequest(
        UUID customerId,
        UUID productId,
        String category,
        String salesChannel,
        String paymentMethod,
        BigDecimal totalMonthlyPremiumAmount,
        BigDecimal insuredAmount,
        Map<String, BigDecimal> coverages,
        List<String> assistances
) {}

