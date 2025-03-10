package com.itau.order_api.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderStatus {
    RECEIVED,
    VALIDATED,
    PENDING,
    REJECTED,
    APPROVED,
    CANCELLED
}
