package com.itau.order_api.adapters.rest;

import com.itau.order_api.application.dto.OrderRequest;
import com.itau.order_api.application.dto.OrderResponse;
import com.itau.order_api.application.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable UUID customerId) {
        List<OrderResponse> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public List<OrderResponse> listOrders() {
        return orderService.getAllOrders();
    }
}