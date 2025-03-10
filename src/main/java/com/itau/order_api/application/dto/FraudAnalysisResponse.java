package com.itau.order_api.application.dto;

import com.itau.order_api.domain.model.RiskClassification;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudAnalysisResponse {

    private UUID orderId;
    private UUID customerId;
    private LocalDateTime analyzedAt;
    private RiskClassification classification;
    private List<OccurrenceResponse> occurrences;
}