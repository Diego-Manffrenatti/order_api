package com.itau.order_api.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PaymentMethod {
    CREDIT_CARD,  // Cartão de Crédito
    DEBIT,        // Débito em Conta
    BOLETO,       // Pagamento via Boleto
    PIX          // Pagamento via PIX
}
