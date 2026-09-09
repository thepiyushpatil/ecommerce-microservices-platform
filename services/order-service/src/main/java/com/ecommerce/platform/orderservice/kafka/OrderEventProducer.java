package com.ecommerce.platform.orderservice.kafka;

import com.ecommerce.platform.orderservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {

    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderPlaced(Order order) {
        String payload = String.format(
                "{\"orderId\":%d,\"productId\":\"%s\",\"quantity\":%d,\"status\":\"%s\"}",
                order.getId(), order.getProductId(), order.getQuantity(), order.getStatus());
        kafkaTemplate.send(TOPIC, order.getProductId(), payload);
    }
}
