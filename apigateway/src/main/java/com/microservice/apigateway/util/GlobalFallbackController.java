package com.microservice.apigateway.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;
/**
 * GlobalFallbackController handles fallback responses for API Gateway when a downstream service is unavailable.
 *
 * This controller provides a generic fallback mechanism that returns a standardized response when a microservice fails.
 *
 * Key Features:
 * - Supports multiple HTTP methods (GET, POST, PUT, DELETE).
 * - Retrieves the failing route ID from the request attributes.
 * - Returns a structured JSON response with error details.
 * - Helps in monitoring and debugging service failures.
 *
 * Usage:
 * - Used in conjunction with Resilience4j Circuit Breaker or Spring Cloud Gateway failure handling.
 * - Clients will receive a fallback response when a service is down instead of a direct error.
 *
 * @author Rajnish Raj
 */
@RestController
public class GlobalFallbackController {
    /**
     * Global fallback method that returns a structured error response when a service is unavailable.
     *
     * @param exchange The current server exchange containing request details.
     * @param error (Optional) A custom error message passed in the request.
     * @return A Map containing the error details.
     */
    @RequestMapping(value = "/fallback/global", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public Map<String, Object> globalFallback(
            ServerWebExchange exchange,
            @RequestParam(value = "error", required = false) String error) {
        String routeId = (String) exchange.getAttributes().get("routeId");
        Map<String, Object> response = new HashMap<>();
        response.put("message", routeId+" Service is currently unavailable. Please try again later.");
        response.put("status", 503); // Service Unavailable
        response.put("timestamp", System.currentTimeMillis());
        response.put("routeId", routeId != null ? routeId : "Unknown"); // Identifies the failing route
        response.put("error", error != null ? error : "Unknown error");

        return response;
    }
}
