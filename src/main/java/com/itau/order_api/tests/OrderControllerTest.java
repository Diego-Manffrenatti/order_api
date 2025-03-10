package com.itau.order_api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.order_api.application.dto.OrderRequest;
import com.itau.order_api.application.dto.OrderResponse;
import com.itau.order_api.application.service.OrderService;
import com.itau.order_api.adapters.rest.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private OrderRequest mockOrderRequest;
    private OrderResponse mockOrderResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Roubo", new BigDecimal("100000.25"));

        mockOrderRequest = new OrderRequest(
                customerId, productId, "AUTO", "MOBILE", "CREDIT_CARD",
                new BigDecimal("75.25"), new BigDecimal("275000.50"),
                coverages, null);

        mockOrderResponse = new OrderResponse(
                orderId, customerId, productId, "AUTO", "MOBILE", "CREDIT_CARD",
                new BigDecimal("75.25"), new BigDecimal("275000.50"),
                coverages, null, "RECEIVED", null, null, null);
    }

    @Test
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(mockOrderResponse);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(mockOrderResponse.customerId().toString()))
                .andExpect(jsonPath("$.status").value("RECEIVED"));

        verify(orderService, times(1)).createOrder(any(OrderRequest.class));
    }
}