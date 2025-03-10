package com.itau.order_api.adapters.rest;

import com.itau.order_api.application.dto.FraudAnalysisResponse;
import com.itau.order_api.application.dto.OccurrenceResponse;
import com.itau.order_api.domain.model.RiskClassification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mock-fraud-analysis")
public class FraudAnalysisMockController {

    @GetMapping
    public ResponseEntity<FraudAnalysisResponse> analyzeFraud(
            @RequestParam UUID customerId,
            @RequestParam UUID productId) {

        RiskClassification classification = classifyRisk(productId);

        FraudAnalysisResponse mockResponse = new FraudAnalysisResponse(
                UUID.randomUUID(),
                customerId,
                LocalDateTime.now(),
                classification,
                List.of(
                 new OccurrenceResponse(UUID.randomUUID(), productId, "FRAUD", "Suspected fraud", LocalDateTime.now(), LocalDateTime.now())
                )
        );

        return ResponseEntity.ok(mockResponse);
    }

    private RiskClassification classifyRisk(UUID productId) {
        if (productId.toString().endsWith("1")) return RiskClassification.HIGH_RISK;
        if (productId.toString().endsWith("2")) return RiskClassification.PREFERRED;
        if (productId.toString().endsWith("3")) return RiskClassification.NO_INFO;
        return RiskClassification.REGULAR;
    }
}
