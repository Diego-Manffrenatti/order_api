package com.itau.order_api.application.service;

import com.itau.order_api.application.dto.FraudAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class FraudAnalysisService {

    private final RestTemplate restTemplate;

    @Value("${fraud.api.url}")
    private String fraudApiUrl;

    public FraudAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public FraudAnalysisResponse analyzeCustomerRisk(UUID customerId, UUID productId) {
        String url = fraudApiUrl + "?customerId=" + customerId + "&productId=" + productId;

        try {
            ResponseEntity<FraudAnalysisResponse> response = restTemplate.getForEntity(url, FraudAnalysisResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Fraud API", e);
        }
    }
}

