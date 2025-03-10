package com.itau.order_api.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SalesChannel {
    MOBILE,      // Compra via App
    WEBSITE,     // Compra via Site
    WHATSAPP,    // Compra via WhatsApp
    CALL_CENTER;  // Compra via Call Center

    public static SalesChannel fromString(String value) {
        for (SalesChannel channel : values()) {
            if (channel.name().equalsIgnoreCase(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("SalesChannel inválido: " + value);
    }
}
