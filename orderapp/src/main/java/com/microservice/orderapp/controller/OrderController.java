package com.microservice.orderapp.controller;

import com.microservice.orderapp.client.UserServiceClient;
import com.microservice.orderapp.model.Order;
import com.microservice.orderapp.model.User;
import com.microservice.orderapp.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAllOrders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/order/{orderId}/user/{userId}")
    public String getOrderAndUserDetails(@PathVariable Long orderId, @PathVariable Long userId) {
        User user = userServiceClient.getUserById(userId); // Calls UserService
        return "Order ID: " + orderId + " belongs to " + user.getName();
    }

}
