package com.microservice.orderapp.client;

import com.microservice.orderapp.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERAPP")
public interface UserServiceClient {

    @GetMapping("/users/getUserById/{id}")
    User getUserById(@PathVariable("id") Long id);
}
