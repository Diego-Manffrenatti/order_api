package com.itau.order_api.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "order-exchange";
    public static final String QUEUE_NAME = "order-queue";
    public static final String ROUTING_KEY = "order-routing-key";

    private static final String RABBITMQ_PAYMENT_QUEUE = "payment-approved-queue";
    private static final String RABBITMQ_PAYMENT_EXCHANGE = "payment-approved-exchange";
    private static final String RABBITMQ_PAYMENT_ROUTING_KEY = "payment-approved-routing-key";

    private static final String RABBITMQ_CANCELLED_QUEUE = "order-cancelled-queue";
    private static final String RABBITMQ_CANCELLED_EXCHANGE = "order-cancelled-exchange";
    private static final String RABBITMQ_CANCELLED_ROUTING_KEY = "order-cancelled-routing-key";

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(RABBITMQ_PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(RABBITMQ_PAYMENT_QUEUE, true);
    }

    @Bean
    public Binding bindingPayment(Queue paymentQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue).to(paymentExchange).with(RABBITMQ_PAYMENT_ROUTING_KEY);
    }

    @Bean
    public DirectExchange cancelledExchange() {
        return new DirectExchange(RABBITMQ_CANCELLED_EXCHANGE);
    }

    @Bean
    public Queue cancelledQueue() {
        return new Queue(RABBITMQ_CANCELLED_QUEUE, true);
    }

    @Bean
    public Binding bindingCancelled(Queue cancelledQueue, DirectExchange cancelledExchange) {
        return BindingBuilder.bind(cancelledQueue).to(cancelledExchange).with(RABBITMQ_CANCELLED_ROUTING_KEY);
    }

   @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
