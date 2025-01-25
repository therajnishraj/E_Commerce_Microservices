package com.microservice.orderapp.service;

import com.microservice.orderapp.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrder(Long orderId);
    List<Order> getAllOrders();
}
