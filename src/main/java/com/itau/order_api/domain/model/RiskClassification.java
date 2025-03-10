package com.itau.order_api.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum RiskClassification {
    HIGH_RISK,
    SUSPICION,
    REGULAR,
    PREFERRED,
    NO_INFO
}
