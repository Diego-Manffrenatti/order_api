package com.itau.order_api.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Category {
    LIFE,
    AUTO,
    RESIDENTIAL,
    BUSINESS
}
