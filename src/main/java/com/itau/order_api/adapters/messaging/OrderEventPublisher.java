package com.itau.order_api.adapters.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.order_api.domain.event.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate,
                               @Value("${RABBITMQ_EXCHANGE_NAME}") String exchange,
                               @Value("${RABBITMQ_ROUTING_KEY}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publishOrderEvent(OrderEvent orderEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(orderEvent);
            logger.info("JSON sent to RabbitMQ: {}", json);
        } catch (Exception e) {
            logger.error("Error to serialize OrderEvent to JSON", e);
        }
        try {
            logger.info("Publishing event to exchange {} with routingKey {}: {}", exchange, routingKey, orderEvent);
            logger.info("Event data: {}", orderEvent);
            rabbitTemplate.convertAndSend(exchange, routingKey, orderEvent);
            logger.info("Event successfully published: {}", orderEvent);
        } catch (Exception e) {
            logger.error("Error publishing event: {}", orderEvent, e);
            throw new RuntimeException("Failed to publish order event", e);
        }
    }
}
