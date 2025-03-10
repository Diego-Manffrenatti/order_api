package com.itau.order_api.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OccurrenceResponse {

    private UUID id;
    private UUID productId;
    private String type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
