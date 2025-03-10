package com.itau.order_api.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

public class OrderEvent implements Serializable {

    @JsonProperty("orderId")
    private UUID orderId;
    @JsonProperty("status")
    private String status;

    public OrderEvent() {
    }

    public OrderEvent(UUID orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public OrderEvent(UUID id, UUID customerId) {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
