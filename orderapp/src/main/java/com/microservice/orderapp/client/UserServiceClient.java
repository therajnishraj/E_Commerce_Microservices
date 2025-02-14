package com.microservice.orderapp.client;

import com.microservice.orderapp.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * UserServiceClient is a Feign client that communicates with the User microservice.
 *
 * It defines methods for fetching user details using the `USERAPP` microservice,
 * and includes a Circuit Breaker to handle failures gracefully.
 *
 * Circuit Breaker:
 * - If the User Service is down or unresponsive, the fallback method `getUserFallback` is triggered.
 * - This ensures the Order Service remains operational even if the User Service is unavailable.
 *
 * Usage:
 * - This Feign client is automatically implemented by Spring Cloud OpenFeign.
 * - The Circuit Breaker is provided by Resilience4j.
 *
 * @author Rajnish Raj
 */
@FeignClient(name = "USERAPP")
public interface UserServiceClient {

    @GetMapping("/users/getUserById/{id}")
    @CircuitBreaker(name = "UserFallback", fallbackMethod = "getUserFallback")
    User getUserById(@PathVariable("id") Long id);

    /**
     * Fallback method for `getUserById` in case of failure.
     * Returns a default User object when the User Service is unavailable.
     *
     * @param id The ID of the user requested.
     * @param throwable The exception that caused the fallback.
     * @return A fallback User object with default values.
     */
    default User getUserFallback(Long id, Throwable throwable) {
        return new User(id, "Fallback User", "Unavailable");
    }
}
