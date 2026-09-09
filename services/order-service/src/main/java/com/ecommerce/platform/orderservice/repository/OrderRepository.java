package com.ecommerce.platform.orderservice.repository;

import com.ecommerce.platform.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
