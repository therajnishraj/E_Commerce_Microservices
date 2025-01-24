package com.microservice.orderapp.service;

import com.microservice.orderapp.model.Order;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrder(Long orderId);
}
