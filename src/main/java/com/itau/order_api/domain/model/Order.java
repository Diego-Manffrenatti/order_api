package com.itau.order_api.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesChannel salesChannel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigDecimal totalMonthlyPremiumAmount;

    @Column(nullable = false)
    private BigDecimal insuredAmount;

    @ElementCollection
    private List<String> assistances;

    @ElementCollection
    @CollectionTable(name = "order_coverages", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "coverage_type")
    @Column(name = "coverage_amount")
    private Map<String, BigDecimal> coverages;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderHistory> history;

    public Order(UUID uuid, OrderStatus orderStatus) {
    }
}
