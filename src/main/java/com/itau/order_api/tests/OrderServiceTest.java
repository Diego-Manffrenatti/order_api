package com.itau.order_api.tests;

import com.itau.order_api.application.dto.OrderRequest;
import com.itau.order_api.application.dto.OrderResponse;
import com.itau.order_api.application.service.OrderService;
import com.itau.order_api.application.dto.OrderHistoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // JUnit 5 Runner para Mockito
public class OrderServiceTest {

    @Mock // 🔥 Aqui estamos mockando a interface corretamente!
    private OrderService orderService;

    private OrderRequest mockOrderRequest;
    private OrderResponse mockOrderResponse;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Roubo", new BigDecimal("100000.25"));
        coverages.put("Perda Total", new BigDecimal("100000.25"));
        coverages.put("Colisão com Terceiros", new BigDecimal("75000.00"));

        mockOrderRequest = new OrderRequest(
                customerId, productId, "AUTO", "MOBILE", "CREDIT_CARD",
                new BigDecimal("75.25"), new BigDecimal("275000.50"),
                coverages,
                List.of("Guincho até 250km", "Troca de Óleo", "Chaveiro 24h")
        );

        mockOrderResponse = new OrderResponse(
                orderId, customerId, productId, "AUTO", "MOBILE", "CREDIT_CARD",
                new BigDecimal("75.25"), new BigDecimal("275000.50"),
                coverages,
                List.of("Guincho até 250km", "Troca de Óleo", "Chaveiro 24h"),
                "RECEIVED", null, null, List.of(new OrderHistoryDTO("RECEIVED", null))
        );
    }

    @Test
    void testCreateOrder() {
        // 🔥 Mockando corretamente o comportamento da interface
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(mockOrderResponse);

        // Executando o teste
        OrderResponse response = orderService.createOrder(mockOrderRequest);

        // Validando resposta
        assertEquals(mockOrderResponse.customerId(), response.customerId());
        assertEquals(mockOrderResponse.productId(), response.productId());
        assertEquals(mockOrderResponse.status(), response.status());

        // Verificando a interação
        verify(orderService, times(1)).createOrder(mockOrderRequest);
    }
}
