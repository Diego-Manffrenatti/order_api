package com.itau.order_api.application.dto;

import java.time.LocalDateTime;

public record OrderHistoryDTO(
        String status,
        LocalDateTime timestamp
) {}
