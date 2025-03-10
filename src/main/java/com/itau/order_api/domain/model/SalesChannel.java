package com.itau.order_api.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SalesChannel {
    MOBILE,
    WEBSITE,
    WHATSAPP,
    CALL_CENTER;

    public static SalesChannel fromString(String value) {
        for (SalesChannel channel : values()) {
            if (channel.name().equalsIgnoreCase(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Invalid SalesChannel: " + value);
    }
}
