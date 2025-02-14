package com.microservice.orderapp.client;

import com.microservice.orderapp.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERAPP")
public interface UserServiceClient {

    @GetMapping("/users/getUserById/{id}")
    @CircuitBreaker(name = "UserFallback", fallbackMethod = "getUserFallback")
    User getUserById(@PathVariable("id") Long id);

    default User getUserFallback(Long id, Throwable throwable) {
        return new User(id, "Fallback User", "Unavailable");
    }
}
