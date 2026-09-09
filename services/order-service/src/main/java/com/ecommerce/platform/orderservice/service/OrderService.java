package com.ecommerce.platform.orderservice.service;

import com.ecommerce.platform.orderservice.kafka.OrderEventProducer;
import com.ecommerce.platform.orderservice.model.Order;
import com.ecommerce.platform.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderEventProducer eventProducer;

    @Autowired
    public OrderService(OrderRepository repository, OrderEventProducer eventProducer) {
        this.repository = repository;
        this.eventProducer = eventProducer;
    }

    public Order placeOrder(Order order) {
        Order saved = repository.save(order);
        eventProducer.publishOrderPlaced(saved);
        return saved;
    }

    public Order getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
    }

    public List<Order> getAll() {
        return repository.findAll();
    }
}
