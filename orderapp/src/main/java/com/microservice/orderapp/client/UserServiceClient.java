package com.microservice.orderapp.client;

import com.microservice.orderapp.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081") // URL of the User Service
public interface UserServiceClient {

    @GetMapping("/users/getUserById/{id}")
    User getUserById(@PathVariable("id") Long id);
}
