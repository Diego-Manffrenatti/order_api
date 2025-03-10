package com.itau.order_api.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCancelledResponse {

    private UUID orderId;
}