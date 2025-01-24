package com.microservice.orderapp.controller;

import com.microservice.orderapp.client.UserServiceClient;
import com.microservice.orderapp.model.Order;
import com.microservice.orderapp.model.User;
import com.microservice.orderapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/createOrder")
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/getOrderById/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/order/{orderId}/user/{userId}")
    public String getOrderAndUserDetails(@PathVariable Long orderId, @PathVariable Long userId) {
        User user = userServiceClient.getUserById(userId);
        return "Order ID: " + orderId + " belongs to " + user.getName();
    }
}
